
package com.rakesh.sms.main;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RawDataCoding;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;

import com.rakesh.sms.beans.Callback;
import com.rakesh.sms.beans.LongMessageParameters;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.beans.SessionParameters;
import com.rakesh.sms.bo.GatewayBo;
import com.rakesh.sms.bo.ReportsBo;

import com.rakesh.sms.cdr.SmsCdrBean;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.entity.CallbackDetails;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SmsLogs;
import com.rakesh.sms.queue.QueueManager;
import com.rakesh.sms.util.AppContext;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class ESME implements Gateway {

	private static Map<String, String> bindParams;
	private Integer liveThreads, smsMaxLength;
	private BindParameter socketParam;
	private SMSCConfigs configDetails;
	private static boolean isJsonCDR;
	private SMPPSession session;
	private ReportsBo reportsbo;
	private boolean connected;
	private Object tpsLock;
	private String circle;
	
	

	public ESME() {
		/**
		 * (Required for Bean Initialization of 'BindParams')
		 * 
		 * 'serviceType' "" (Default) CMT (Cellular Messaging) CPT (Cellular Paging) VMN
		 * (Voice Mail Notification) VMA (Voice Mail Alerting) WAP (Wireless Application
		 * Protocol) USSD (Unstructured Supplementary Services Data)
		 * 
		 * 'smsMaxLength' For GSM 03.38 you get 1072/7 = 153 GSM (7-bit) chars + 1
		 * filling unused bit For Latin you get 1072/7 = 134 (8-bit) chars For UCS-2 you
		 * get 1072/16 = 67 (16-bit) chars.
		 * 
		 * 'priorityFlag' (Value) (GSMa) (ANSI-136) (IS-95) 0 non-priority Bulk Normal 1
		 * priority Normal Interactive 2 priorityv Urgent Urgent 3 priority Very Urgent
		 * Emergency
		 */
	}// End Of Constructor

	public ESME(SMSCConfigs details) {

		try {

			this.configDetails = details;
			this.circle = details.getCircle();
			//ESME.isJsonCDR = CdrCreator.isJsonCDR();
			this.reportsbo = (ReportsBo) AppContext.getBean("reportsBo");
			Logger.sysLog(LogValues.debug, this.getClass().getName(),
					" SMPP Server Configuration Details ::  Circle: " + details.getCircle() + " | UserID: "
							+ details.getUserid() + "  |  Password: " + details.getPassword() + "  |  SystemType: "
							+ details.getSystemType() + "  |  ServerIP: " + details.getServerIp() + "  |  Port: "
							+ details.getServerPort() + " ");

			this.socketParam = new BindParameter(BindType.values()[details.getBindMode()], details.getUserid(),
					details.getPassword(), details.getSystemType(), ESME.parseTON(bindParams.get("addr_ton")),
			
			ESME.parseNPI(bindParams.get("addr_npi")), "");
			this.session = new SMPPSession();
			this.session.setPduProcessorDegree(QueueManager.TPS + 3); // +3 for Buffer
			this.session.connectAndBind(details.getServerIp(), details.getServerPort(), this.socketParam,
					details.getTimeout());
			Logger.sysLog(LogValues.debug, this.getClass().getName(),
					" SMPP Current Session Timout: " + this.session.getEnquireLinkTimer());
			Logger.sysLog(LogValues.info, this.getClass().getName(), " SMPP SessionID: " + this.session.getSessionId());

			this.initParams();
			this.liveThreads = new Integer(0);
			this.tpsLock = new Object();
			this.connected = true;

		} catch (IllegalStateException ise) {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					ise.getMessage() + ":: Connection already Stopped... ");
			this.connected = false;
		} catch (NoRouteToHostException nrhe) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					nrhe.getMessage() + ":: No Connection to SMSC |  Circle=" + this.circle
					+ " | SMSC Not in Network Range | Connection Stopped... ");
			this.connected = false;
		} catch (ConnectException ce) {
			this.close();
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					ce.getMessage() + ":: Connection refused by SMSC [" + this.circle + "] ");
			this.connected = false;
		} catch (IOException ioe) {
			this.close();
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					ioe.getMessage() + ":: Bind Timeout with SMSC [" + this.circle + "] ");
			this.connected = false;
		} catch (Exception e) {
			this.close();
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Unable to establish SMSC [" + this.circle + "] Connection \n" + Logger.getStack(e));
			this.connected = false;
		} // End Of Try Catch

	}// End Of Constructor

	private void initParams() {

		try {
			int timeout = Integer.parseInt(CoreUtils.getProperty("transactionTimer"));
			this.session.setTransactionTimer(timeout);
		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, this.getClass().getName(),
					" Error Parsing Transaction Timer | Check global.sms_properties | Using default value: "
							+ this.session.getTransactionTimer());
		}

		String maxLength = CoreUtils.getProperty("smsMaxLength");
		try {
			this.smsMaxLength = Integer.parseInt(maxLength);
		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, this.getClass().getName(),
					" Error Parsing smsMaxLength | Check global.sms_properties | Using default value: 67 characters");
			this.smsMaxLength = 67;
		}
		this.smsMaxLength *= 2; // 1Char = 2Bytes

		if (this.socketParam.getBindType().isReceivable()) {
			MessageReceiverListener messageListener = new SMPPMessageListener(this.circle);
			this.session.setMessageReceiverListener(messageListener);
			SmsValidation.init();
		}

	}// End Of Method

	public static long getReconnectionTimeout() {

		try 
		{
			long endtimeout = Long.parseLong(bindParams.get("reconnectionTimeout"));
			return (endtimeout * 1000L);
		} 
		
		catch (Exception e) 
		{
			Logger.sysLog(LogValues.warn, ESME.class.getName(),
					" Error Parsing Reconnection Timeout | Check settings | Using default value: 5mins ");
			return (300 * 1000L); // 5Mins
		}

	}// End Of Method

	public void setBindParams(Map<String, String> bindParams) {
		ESME.bindParams = bindParams;
	}

	public void setReportsbo(ReportsBo reportsbo) {
		this.reportsbo = reportsbo;
	}

	protected static Map<String, String> getBindParams() {
		return ESME.bindParams;
	}

	public SMPPSession getSmppSession() {
		return this.session;
	}

	public String getSessionId() {
		return this.session.getSessionId();
	}

	public boolean isConnected() 
	{
		if (this.connected == true && this.session.getSessionState().isBound() == false) {
			Logger.sysLog(LogValues.info, QueueManager.class.getName(),
					"ESME.isConnected() - Removing SMSC from Hashtable for circle: " + circle);
			QueueManager.removeSMSC(circle);

			this.close();
		}
		return this.connected;
	}// End Of Method

	
	public synchronized Integer getRunningThreadsCount() {
		return this.liveThreads;
	}

	public synchronized Integer reduceThreadCount() {
		return --this.liveThreads;
	}

	public synchronized void increaseThreadCount() {
		++this.liveThreads;
	}

	public Object getTPSLock() {
		return this.tpsLock;
	}// End Of Method

	public void notifyTPS() {

		try {
			synchronized (this.tpsLock) {
				this.tpsLock.notify();
			}
		} catch (IllegalMonitorStateException imse) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Illegal State to Notify TPS Lock ");
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Notifying TPS Lock :: " + e.getMessage());
		} // End Of Try Catch

	}// End of Method

	/**
	 * Asynchronous SMS Request :: SMSC Response Not returned to Controller, local
	 * Response is returned
	 */
	public void sendMessage(Message msg) {

		if (this.socketParam.getBindType().isTransmittable()) {
			AsyncThread athread = new AsyncThread(msg);
			this.increaseThreadCount();
			Thread thread = new Thread(athread);
			thread.start();
		} else {
			BindType bType = this.socketParam.getBindType();
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Transmission of SMS not allowed  |  BindMode(" + bType.ordinal() + ") =" + bType.toString());
		}

	}// End Of Method

	/**
	 * Synchronous SMS Request :: SMSC Response returned to Controller
	 */
	public String sendSyncMessage(Message msg) {

		String resp = "NULL";

		if (this.socketParam.getBindType().isTransmittable()) {
			AsyncThread athread = new AsyncThread(msg);
			this.increaseThreadCount();
			Thread thread = new Thread(athread);
			thread.start();

			try {
				thread.join();
				resp = athread.getResponse();
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			}

		} else {
			BindType bType = this.socketParam.getBindType();
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Transmission of SMS not allowed  |  BindMode(" + bType.ordinal() + ") =" + bType.toString());
		}

		return resp;

	}// End Of Method

	private class BlankMessageException extends Exception {

		private static final long serialVersionUID = 4185089443841091982L;
		private String message;

		public BlankMessageException() {
			super();
			this.message = new String("Found SMS with Blank Content");
		}// End Of Constructor

		@Override
		public String toString() {
			return message;
		}

		@Override
		public String getMessage() {
			return message;
		}

	}// End Of Inner Exception Class

	private class AsyncThread implements Runnable {

		private String response = new String("");
		private Message sms;

		public AsyncThread(Message sms) {
			this.sms = sms;
		}// End Of Constructor

		public String getResponse() {
			return response;
		}

		public void run() 
		{

			SmsCdrBean cdr = CoreUtils.getSmsCDR(sms);
			ESMClass esmClass = new ESMClass();
			byte protocolId = 0, priorityFlag = 1;
			RegisteredDelivery regDelv;
			Random rand = new Random();
			long startTime, endTime;
			DataCoding dataCoding;

			startTime = System.currentTimeMillis();

			if (bindParams.get("udhi").equals("1")) 
			{
				esmClass.setSpecificFeature(GSMSpecificFeature.UDHI);
			
				smsMaxLength = 200;
				
				
			}
			

			/**
			 * Operator & Country Specific Code
			 */
			if (sms.isSilent() && sms.isMobileTerminating()) {
				protocolId = 64; // 0x40
				dataCoding = new RawDataCoding((byte) 192);
				regDelv = new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE);
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" SILENT_MT Flag found True | Registered Delivery: 0x01  | protocolId:" + protocolId + " | "
								+ dataCoding);

				
			} 
			else 
			
			{
				/**
				 * General/Default Values
				 */
				protocolId = 0;
				regDelv = new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE);

				if (sms.containsHyperlink() && sms.isMobileTerminating()) {
					dataCoding = new RawDataCoding((byte) 245);
					esmClass.setSpecificFeature(GSMSpecificFeature.UDHI);
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" Flag URL_LINK Flag Found True |  ESMClass Value:" + esmClass.value() + "  |  "
									+ dataCoding);
				}

				/** Override DataCoding on Demand */

				if (sms.isFlashSMS()) {
					dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS0, false);
					
				}
				else {
					if(sms.getDataCoding() == 8 )
					{
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"we are using sms.getDataCoding=8");
					
				         if(CoreUtils.getProperty("country").equalsIgnoreCase("GB") && CoreUtils.getProperty("operator").equalsIgnoreCase("orange") )
				         {
				        	 Logger.sysLog(LogValues.info, this.getClass().getName(),
										"we are in orange gb"); 
				        	 dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, null, false); 
				        	 Logger.sysLog(LogValues.info, this.getClass().getName(),
										"dataCoding" +dataCoding.toString());
				         }
				         else
				         {
						dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2, null, false);
						
				         }
					}
					
					else
						dataCoding = new RawDataCoding(sms.getDataCoding());
				}
			} 

			/*if (sms.getDataCoding() == 8) {
				if (sms.isFlashSMS())
					dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS0, false);
				else 
					dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2, null, false);
			}
			else {
				dataCoding = new RawDataCoding(sms.getDataCoding());
			}*/

			if (sms.isUrgentSMS()) {
				priorityFlag = 2;
			} // End Of IF

			// TimeFormatter parser = new AbsolutefTimeFormatter();
			// String scheduledTimeDelivery = parser.format(new Date());
			String subLog = " [" + sms.getCli() + "][" + sms.getMsisdn() + "]";

			String scheduledTimeDelivery = "";

			try {

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" ~Pushing SMS to SMSC~ | DataCoding= " + dataCoding.toByte());

				// System.out.println(subLog + " Pushing SMS to SMSC");

				String finalEncoding = "UTF-16BE";
				String enc = sms.getEncoding();
				byte[] messageInBytes;
				String response = "";

				if (enc.length() > 0 && enc.equalsIgnoreCase("NA"))
					messageInBytes = sms.getMessage().getBytes();
				else if (enc.length() > 0 && enc.equalsIgnoreCase("HEXCODE"))
				{
					String hex = CoreUtils.toHex(sms.getMessage());
					messageInBytes = hex.getBytes(finalEncoding);
				} else if (enc.length() > 0 && enc.equalsIgnoreCase("UNICODE"))
				{
					String uni = CoreUtils.getUnicode(sms.getMessage());
					messageInBytes = uni.getBytes(finalEncoding);
				} 
				else if (enc.length() > 0 && enc.equalsIgnoreCase("GSM7"))
				{
					Logger.sysLog(LogValues.info, this.getClass().getName(),"we have gsm 7");
					String gsm = CoreUtils.gsmencode(sms.getMessage());
					Logger.sysLog(LogValues.info, this.getClass().getName(),"and gsm "+gsm);
					messageInBytes = gsm.getBytes(finalEncoding);
				} 
				
				else if (enc.length() > 0)
				{
					messageInBytes = sms.getMessage().getBytes(enc);
					finalEncoding = enc;
				} 
				else 
				{
					finalEncoding = "UTF-8";
					messageInBytes = sms.getMessage().getBytes(finalEncoding);
				}


				String validationPeriod = null;

				/*if(CoreUtils.getProperty("country").equals("IRQ") && CoreUtils.getProperty("operator").equals("ASL")
						&& CoreUtils.getProperty("protocol").equals("SMPP") &&
						CoreUtils.getProperty("chargingShortCode") != null 
						&& CoreUtils.getProperty("chargingShortCode").length() > 0) {

					String[] chargingSC = CoreUtils.getProperty("chargingShortCode").split(",");
					boolean charging = false;

					for(int p =0 ; p<chargingSC.length ; p++) {
						if(sms.getCli().equals(chargingSC[p])) {
							charging = true;
							break;
						}
					}

					if(charging && CoreUtils.getProperty("esme_validation_period") != null && 
							CoreUtils.getProperty("esme_validation_period").equalsIgnoreCase("true")) {

						SimpleDateFormat dformat = new SimpleDateFormat("YYMMdd");
						String d = dformat.format(new Date());

						validationPeriod = d+"23"+"59"+"59"+"000"+"+";

						Logger.sysLog(LogValues.info, this.getClass().getName(),"validity period : " + validationPeriod);

					}
				}*/

				StringBuffer bytesConcatenated = new StringBuffer("");

				for (int i = 0; i < messageInBytes.length; i++) 
				{
					bytesConcatenated.append(String.valueOf(messageInBytes[i]));
					bytesConcatenated.append(" ");
				}

				Logger.sysLog(LogValues.debug, this.getClass().getName(),
						" Pushing Encoded Content(" + messageInBytes.length + ") = " + bytesConcatenated.toString()
						+ " | serviceType= " + sms.getServiceType());

				if (sms.isLongMessage() && sms.getOptionalLongParams().isRescheduled()) {
					/** Retry Long Messages Segment once Failed */

					LongMessageParameters lmParams = sms.getOptionalLongParams();
					int currentSegment = lmParams.getSarSequenceNumber();
					int totalSegments = lmParams.getSarTotalSegments();
					int index = lmParams.getSegmentIndex();

					OptionalParameter msgRefNum = lmParams.getMessageReferenceNumberObject();
					OptionalParameter sarTotalSegments = lmParams.getSarTotalSegmentsObject();

					for (int i = currentSegment; i <= totalSegments; i++) {

						byte[] segmentedMsg = this.getMessageSegment(messageInBytes, finalEncoding, index,
								index + smsMaxLength);
						String smsPart = new String(segmentedMsg, finalEncoding);
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								subLog + " -Retrying- Long Message Segment " + i + " = " + smsPart);

						lmParams.setSegmentIndex(index);
						lmParams.setSarSequenceNumber(i);
						sms.setOptionalLongParams(lmParams);
						OptionalParameter sarSegmentSeqnum = lmParams.getSarSequenceNumberObject();

						if (smsPart != null && smsPart.trim().length() >= 0) {

							if (sms.getOptionalSessParams() == null) 
							{
//								response = session.submitShortMessage(sms.getServiceType(),
//										ESME.parseTON(bindParams.get("source_ton")),
//										ESME.parseNPI(bindParams.get("source_npi")), sms.getCli(),
//										ESME.parseTON(bindParams.get("destination_ton")),
//										ESME.parseNPI(bindParams.get("destination_npi")), sms.getMsisdn(), esmClass,
//										protocolId, priorityFlag, scheduledTimeDelivery, validationPeriod, regDelv, (byte) 0,
//										dataCoding, (byte) 0, segmentedMsg, msgRefNum, sarTotalSegments,
//										sarSegmentSeqnum);

								Logger.sysLog(LogValues.info, this.getClass().getName(), subLog
										+ " SMSC Response Received for Retried Long Message[" + i + "]: " + response);
								// System.out.println(subLog + " SMSC response received for part ["+i+"] :"+
								// response);

								index += smsMaxLength;

								/** Long Messages CDRs */
								if (i < totalSegments)
									this.createCDRofSegments(cdr, response);
							}
							else 
							{

								SessionParameters sessParams = sms.getOptionalSessParams();

								OptionalParameter ussdServiceOp = sessParams.getUssdServiceOpObject();
								OptionalParameter itsSessionInfo = sessParams.getItsSessionInfoObject();

//								response = session.submitShortMessage(sms.getServiceType(),
//										ESME.parseTON(bindParams.get("source_ton")),
//										ESME.parseNPI(bindParams.get("source_npi")), sms.getCli(),
//										ESME.parseTON(bindParams.get("destination_ton")),
//										ESME.parseNPI(bindParams.get("destination_npi")), sms.getMsisdn(), esmClass,
//										protocolId, priorityFlag, scheduledTimeDelivery, validationPeriod, regDelv, (byte) 0,
//										dataCoding, (byte) 0, segmentedMsg, msgRefNum, sarTotalSegments,
//										sarSegmentSeqnum, ussdServiceOp, itsSessionInfo);

								Logger.sysLog(LogValues.info, this.getClass().getName(), subLog
										+ " SMSC Response Received for Retried Long Message[" + i + "]: " + response);
								index += smsMaxLength;

								/** Long Messages CDRs */
								if (i < totalSegments)
									this.createCDRofSegments(cdr, response);
							}

						} else 
						{
							Logger.sysLog(LogValues.warn, this.getClass().getName(),
									subLog + " Blank Retried Long Message[" + i + "] Segment found... (Ignored) ");
							throw new BlankMessageException();
						} // End Of If Else

						
						try 
						{
							/** To Maintain TPS in case of Long Messages sequential pushes */
							Thread.sleep(Pusher.ThreadBurstTime);
						} 
						catch (Exception e) 
						{
						}

					} // End Of Loop

					/** Retry Successful */
					sms.disableLongSegmentRetry();

				} else if (messageInBytes.length > smsMaxLength) {
					/** Push Long Messages */
					int index = 0;
					int totalSegments = (messageInBytes.length / smsMaxLength) + 1;
					LongMessageParameters lmParams = new LongMessageParameters();

					lmParams.setSarTotalSegments(totalSegments);
					lmParams.setMessageReferenceNumber((short) rand.nextInt());

					OptionalParameter msgRefNum = lmParams.getMessageReferenceNumberObject();
					OptionalParameter sarTotalSegments = lmParams.getSarTotalSegmentsObject();

					for (int i = 1; i <= totalSegments; i++) {

						byte[] segmentedMsg = this.getMessageSegment(messageInBytes, finalEncoding, index,
								index + smsMaxLength);
						String smsPart = new String(segmentedMsg, finalEncoding);
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								subLog + " Long Message Segment " + i + " = " + smsPart);

						lmParams.setSegmentIndex(index);
						
						lmParams.setSarSequenceNumber(i);
						sms.setOptionalLongParams(lmParams);
						OptionalParameter sarSegmentSeqnum = lmParams.getSarSequenceNumberObject();

						if (smsPart != null && smsPart.trim().length() >= 0) {

							if (sms.getOptionalSessParams() != null) {
								SessionParameters sessParams = sms.getOptionalSessParams();

								OptionalParameter ussdServiceOp = sessParams.getUssdServiceOpObject();
								OptionalParameter itsSessionInfo = sessParams.getItsSessionInfoObject();

//								response = session.submitShortMessage(sms.getServiceType(),
//										ESME.parseTON(bindParams.get("source_ton")),
//										ESME.parseNPI(bindParams.get("source_npi")), sms.getCli(),
//										ESME.parseTON(bindParams.get("destination_ton")),
//										ESME.parseNPI(bindParams.get("destination_npi")), sms.getMsisdn(), esmClass,
//										protocolId, priorityFlag, scheduledTimeDelivery, validationPeriod, regDelv, (byte) 0,
//										dataCoding, (byte) 0, segmentedMsg, msgRefNum, sarTotalSegments,
//										sarSegmentSeqnum, ussdServiceOp, itsSessionInfo);

								Logger.sysLog(LogValues.info, this.getClass().getName(),
										subLog + " SMSC Response Received for Long Message[" + i + "]: " + response);

								// System.out.println(subLog + " SMSC response received for part["+i+"] : "+
								// response);
								index += smsMaxLength;

								/** Long Messages CDRs */
								if (i < totalSegments)
									this.createCDRofSegments(cdr, response);
							} else {

//								response = session.submitShortMessage(sms.getServiceType(),
//										ESME.parseTON(bindParams.get("source_ton")),
//										ESME.parseNPI(bindParams.get("source_npi")), sms.getCli(),
//										ESME.parseTON(bindParams.get("destination_ton")),
//										ESME.parseNPI(bindParams.get("destination_npi")), sms.getMsisdn(), esmClass,
//										protocolId, priorityFlag, scheduledTimeDelivery, validationPeriod, regDelv, (byte) 0,
//										dataCoding, (byte) 0, segmentedMsg, msgRefNum, sarTotalSegments,
//										sarSegmentSeqnum);

								Logger.sysLog(LogValues.info, this.getClass().getName(),
										subLog + " SMSC Response Received for Long Message[" + i + "]: " + response);

								// System.out.println(subLog + " SMSC response received for part["+i+"] : "+
								// response);

								index += smsMaxLength;

								/** Long Messages CDRs */
								if (i < totalSegments)
									this.createCDRofSegments(cdr, response);
							}

						} else {
							Logger.sysLog(LogValues.warn, this.getClass().getName(),
									subLog + " Blank Long Message[" + i + "] Segment found... (Ignored) ");
							throw new BlankMessageException();
						} // End Of If Else

						try {
							/** To Maintain TPS in case of Long Messages sequential pushes */
							Thread.sleep(Pusher.ThreadBurstTime);
						} catch (Exception e) {
						}

					} // End Of Loop

					/**
					 * After all the Segments of Long SMS is sent successfully; Disable the Retry In
					 * case of any SubmitSm Failure, this statement will not be executed due of
					 * Exception
					 */
					sms.disableLongSegmentRetry();

				} else {

					if (sms.getOptionalSessParams() == null) {

						/** Short Messages */

//						response = session.submitShortMessage(sms.getServiceType(),
//								ESME.parseTON(bindParams.get("source_ton")),
//								ESME.parseNPI(bindParams.get("source_npi")), sms.getCli(),
//								ESME.parseTON(bindParams.get("destination_ton")),
//								ESME.parseNPI(bindParams.get("destination_npi")), sms.getMsisdn(), esmClass, protocolId,
//								priorityFlag, scheduledTimeDelivery, validationPeriod, regDelv, (byte) 0, dataCoding, (byte) 0,
//								messageInBytes);
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								subLog + " SMSC Response Received for Short Message: " + response);
					} else {
						SessionParameters sessParams = sms.getOptionalSessParams();

						OptionalParameter ussdServiceOp = sessParams.getUssdServiceOpObject();
						OptionalParameter itsSessionInfo = sessParams.getItsSessionInfoObject();

//						        response = 
//						        		session.submitShortMessage(sms.getServiceType(),
//								ESME.parseTON(bindParams.get("source_ton")),
//								ESME.parseNPI(bindParams.get("source_npi")), sms.getCli(),
//								ESME.parseTON(bindParams.get("destination_ton")),
//								ESME.parseNPI(bindParams.get("destination_npi")), sms.getMsisdn(), esmClass, protocolId,
//								priorityFlag, scheduledTimeDelivery, validationPeriod, regDelv, (byte) 0, dataCoding, (byte) 0,
//								messageInBytes, ussdServiceOp, itsSessionInfo);
//						        Logger.sysLog(LogValues.info, this.getClass().getName(),
//								subLog + " SMSC Response Received for Short Message: " + response);

					}

				} // End Of Message Split IF ELSE

				String responseType = configDetails.getResponseType();

				cdr.setResponsetime(CoreUtils.getCurrentTimeStamp());
				cdr.setStatus("Success");

				if (responseType != null && responseType.equalsIgnoreCase("HEX"))
				{
					response = SMPPMessageListener.getMessageId(response.trim());
					cdr.setMessageId(response);
				} else {
					// response = response.replaceAll("/", "").replaceAll("\\\\", "");
					cdr.setMessageId(response.trim().toUpperCase());
				}

				if (sms.isCallback())
				{
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"Saving callback details..." + sms.toString());
					saveCallbackDetails(sms, cdr.getMessageId());
				} // End Of CallBack TRUE

			} catch (BlankMessageException bme) {
				cdr.setMessageId(SMSController.DefaultMessageID);
				//Logger.sysLog(LogValues.info, this.getClass().getName(), "Rakesh BlankMessageException so setting setStatus is null ");
				//System.out.println("Rakesh BlankMessageException so setting setStatus is null");
				cdr.setStatus(null); /** Prevents CDR Creation **/
				sms.disableRetry();
			} catch (IOException e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), " IO Error | Broken SMSC Connection ");
				cdr.setMessageId(SMSController.DefaultMessageID);
				cdr.setStatus("Failure " + e.getMessage());
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						subLog + " :SubmitSm Error: \n" + Logger.getStack(e));
				cdr.setMessageId(SMSController.DefaultMessageID);
				cdr.setStatus("Failure InternalError");
			} finally {

				
				
				
				
				this.response = cdr.getStatus();
				
				
				if (session == null || session.getSessionState().isBound() == false) 
				{

					Logger.sysLog(LogValues.info, this.getClass().getName(), "seesion: " + session);
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							" UnExpected Expired SMSC [" + circle + "] Connection Detected ");

					if (cdr.getStatus() == null || cdr.getStatus().length() == 0) {
						cdr.setStatus("Failure ConnectionFailure");
					}

					/** Retrying Connection */
					QueueManager.removeSMSC(circle);
					close();
					ReConnector.reconnect(circle);
				} // End Of Connection Check

				if (this.response != null && this.response.length() != 0) {
					//CdrCreator.saveAsXML(cdr);
				}

				if (this.response == null || this.response.toLowerCase().contains("failure")) 
				{
				//	System.out.println("we get reponse is null for "+sms.getMsisdn());
					
					Logger.sysLog(LogValues.info, this.getClass().getName(),"we get reponse  null for "+sms.getMsisdn());
					/** Reschedule */
					if (this.sms.isReschedule()) 
					{
						Logger.sysLog(LogValues.info, this.getClass().getName(), " Retry Enabled | Rescheduling... ");
						CoreUtils.retrySms(this.sms);
					} 
					else if (this.sms.getRemainingRetries() == 0 && this.sms.isCallback()) 
					{
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								subLog + "  SMS Submitting failed | Sending Callback (Retry Timeout) !!! ");
						this.sms.getCallbackDetails().setCallbackStatus("Failure");
						this.sms.getCallbackDetails().setFailureReason("SubmitSmFailure");
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"Sending callback..." + sms.toString());
						CoreUtils.sendCallback(this.sms);
						this.sms.setCallback(false);
					}
					else if (this.sms.getRemainingRetries() > 0 && this.sms.isCallback() == false) 
					{
						Logger.sysLog(LogValues.warn, this.getClass().getName(), " SubmitSM Failure | Retrying... ");
						if (this.sms.isLongMessage()) {
							CoreUtils.retrySmsNow(this.sms);
						} else {
							CoreUtils.retrySms(this.sms);
						}
					} 
					else
						Logger.sysLog(LogValues.info, this.getClass().getName(), " SubmitSM Failed ");
				} // End Of Failure Scenario

			} // End Of Try Catch

			/** Calculating ProcessTime for TPS */
			endTime = System.currentTimeMillis();

			// System.out.println(subLog + " end time:" + endTime);

			// System.out.println("Thread burst time : " + Pusher.ThreadBurstTime );

			long duration = endTime - startTime + 1;
			long remaining = 0L;

			if (duration < Pusher.ThreadBurstTime) {
				remaining = Pusher.ThreadBurstTime - duration;
			}

			// System.out.println("Remaining time for the thread: "+ remaining + " ms");

			// System.out.println("SMS Process time: "+ duration + " ms");

			Logger.sysLog(LogValues.debug, this.getClass().getName(), subLog + " SMS ProcessTime= " + duration + "ms");

			if (remaining > 0) {
				try {
					Thread.sleep(remaining);
				} catch (Exception e) {
				}
			} else {
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						subLog + " SMS ProcessTime= " + duration + "ms");
			}

			int tps = reduceThreadCount();
			Logger.sysLog(LogValues.debug, this.getClass().getName(), subLog + " TPS Reduced: " + tps);

			// System.out.println("TPS reduced : " + tps);

			notifyTPS();
			Logger.sysLog(LogValues.debug, this.getClass().getName(), subLog + " Submit_sm thread Finished!!!  ");

			// System.out.println(subLog + " Submit_sm thread finished! " +
			// CoreUtils.getTimeStamp(new Date()));

		}// End Of Thread

		/** Segmenting Long Messages */
		private byte[] getMessageSegment(byte[] msg, String encoding, int start, int end) {

			int i;
			int totalLength = msg.length;
			StringBuffer bytesConcatenated = new StringBuffer("");
			final int length = end < totalLength ? (end - start + 1) : (totalLength - start + 1);

			if (length > 0) {

				List<Byte> newMsg = new ArrayList<Byte>();
				i = start;

				for (; i < end && i < totalLength; i++)
					newMsg.add(msg[i]);

				byte[] segment = new byte[newMsg.size()];

				for (i = 0; i < newMsg.size(); i++) {
					segment[i] = newMsg.get(i); // DO NOT DELETE
					bytesConcatenated.append(String.valueOf(segment[i]));
					bytesConcatenated.append(" ");
				}

				Logger.sysLog(LogValues.debug, this.getClass().getName(),
						" Segment Encoded Content(" + newMsg.size() + ") = " + bytesConcatenated.toString());
				newMsg.clear();

				return segment;

			} else {
				return new byte[0];
			}

		}// End Of Method

		private void createCDRofSegments(SmsCdrBean cdr, String responseId) {

			try {

				String msgId = SMPPMessageListener.getMessageId(responseId);
				SmsCdrBean segmentsCdr = (SmsCdrBean) cdr.clone();
				segmentsCdr.setResponsetime(CoreUtils.getCurrentTimeStamp());
				segmentsCdr.setMessageId(msgId);
				segmentsCdr.setStatus("Success");
				//CdrCreator.saveAsXML(segmentsCdr);

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" Error Cloning and Creating CDR for LongMessage :: " + e.getMessage());
			}

		}// End Of Method

	}// End Of Inner Class

	private static TypeOfNumber parseTON(String value) {

		int val = 0;

		try {
			val = Integer.parseInt(value);
			return TypeOfNumber.values()[val];
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, ESME.class.getName(), " Unable to parse Enum of Type= TypeOfNumber");
		} // End Of Try Catch

		return TypeOfNumber.UNKNOWN;

	}// End Of Method

	private static NumberingPlanIndicator parseNPI(String value) {

		int val = 0;

		try {
			val = Integer.parseInt(value);
			return NumberingPlanIndicator.values()[val];
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, ESME.class.getName(),
					" Unable to parse Enum of Type= NumberingPlanIndicator");
		} // End Of Try Catch

		return NumberingPlanIndicator.UNKNOWN;

	}// End Of Method

	/** Adding SMS Logs and Callback Details in Database */
	private void saveCallbackDetails(Message sms, String messageid) {

		try {

			SmsLogs log = new SmsLogs();
			log.setSender(sms.getCli());
			log.setReceiver(sms.getMsisdn());
			log.setCircle(sms.getCircle());
			log.setMessageId(messageid);

			String transId = sms.getCallbackDetails().getTransactionId();

			if (transId != null && transId.length() > 0)
				log.setTransId(sms.getCallbackDetails().getTransactionId());
			else {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" Callback details::  No transactionId found ");
				log.setTransId("ERR123");
			}

			try {
				Double price = Double.parseDouble(sms.getCallbackDetails().getPrice());
				log.setPrice(price);
			} catch (Exception e) {
				Logger.sysLog(LogValues.warn, this.getClass().getName(), " Unable to parse Callback Price ");
				log.setPrice(0D);
			}

			Callback callback = sms.getCallbackDetails();
			if (callback != null) {

				sms.getCallbackDetails().setMessageId(messageid);
				Logger.sysLog(LogValues.debug, this.getClass().getName(), " CallBack Object :: " + callback.toString());
				CallbackDetails cbdetails = reportsbo.getCallbackDetails(callback.getAction(), callback.getServiceid(),
						callback.getSubServiceid());

				if (cbdetails != null) {
					log.setCallback(cbdetails.getId());
				} else {
					cbdetails = new CallbackDetails();
					cbdetails.setAction(callback.getAction());
					cbdetails.setServiceid(callback.getServiceid());
					cbdetails.setSubServiceid(callback.getSubServiceid());
					cbdetails.setAdditionals(callback.getAdditionals());
					int id = reportsbo.addCallbackDetails(cbdetails);
					log.setCallback(id);
				} // End Of If Else

				reportsbo.addLog(log);
			} else {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" [" + sms.getCli() + "][" + sms.getMsisdn() + "]  CallBack Object Not Found");
			} // End Of CallBack Mechanism

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		} // End Of Try Catch

	}// End Of Method

	public void close() {

		if (this.session != null) {
			try {
				this.session.unbindAndClose();
				this.connected = false;
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" [" + circle + "] Unable to Close Session Manually :: " + e.getMessage());
			} // End Of Try Catch
		} // End Of IF

		SmsValidation.stop();

	}// End Of Method

}
