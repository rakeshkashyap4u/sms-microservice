
package com.rakesh.sms.main;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.beans.Response;
import com.rakesh.sms.cdr.ReceivedSmsBean;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class SMPPMessageListener implements MessageReceiverListener {

	private String circle;

	public SMPPMessageListener(String circle) {
		this.circle = circle != null ? circle.trim() : "";
	}// End Of Constructor

	public String getCircle() {
		return this.circle;
	}

	public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {

		// ayu comment
		Logger.sysLog(LogValues.info, this.getClass().getName(),
				"1. Inside onAcceptDeliverSM" + "DeliverSM " + deliverSm);

		if (deliverSm == null)
			return;

		String eventJson = CoreUtils.GSON.toJson(deliverSm).trim();

		// ayu comment
		Logger.sysLog(LogValues.info, this.getClass().getName(), " 2. eventJson recieved " + eventJson);

		try {

			boolean deliveryReport = deliverSm.isSmscDeliveryReceipt();
			ReceivedSmsBean cdr = new ReceivedSmsBean();
			String messageId = "", receivedSMS = "";

			String sourceAddr = deliverSm.getSourceAddr();
			String destAddr = deliverSm.getDestAddress();
			String serviceType = deliverSm.getServiceType();
			int sequenceNumber = deliverSm.getSequenceNumber();

			//Logger.sysLog(LogValues.info, this.getClass().getName(), " 3. Event Received: " + eventJson);

			if (deliveryReport || MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {

				try {

					DeliveryReceipt receipt = deliverSm.getShortMessageAsDeliveryReceipt();
					receivedSMS = SMPPMessageListener.getTrimmedMessage(receipt.getText());
					messageId = SMPPMessageListener.getMessageId(receipt.getId());
					cdr.setTime(CoreUtils.getTimeStamp(receipt.getDoneDate()));
					cdr.setStatus(receipt.getFinalStatus().toString());
					cdr.setMessageId(messageId);
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"4. DeliveryReport Received :: [" + sourceAddr + "] ::  MessageId=" + messageId
							+ "  |  Message=" + receivedSMS + " | status=" + cdr.getStatus() + " | deliverAdress= "+destAddr);

				} catch (Exception e) {
					//Logger.sysLog(LogValues.error, this.getClass().getName(),"Exception: "+Logger.getStack(e));
					cdr.setTime(CoreUtils.getCurrentTimeStamp());
					receivedSMS = new String(deliverSm.getShortMessage());
					receivedSMS = SMPPMessageListener.getTrimmedMessage(receivedSMS);
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							e.getMessage() + " :: DeliveryReport Received :: " + receivedSMS);
				} finally {
					deliveryReport = true;
				}
				
				String chargingShortCode = CoreUtils.getProperty("chargingShortCode");
				String url = CoreUtils.getProperty("DLR_provisoningUrl");
				
				if(chargingShortCode != null && url!= null) {
					
				String[] chargingShortCodes = chargingShortCode.split(",");
				
				boolean ischargingSC = false; 
				for(int i = 0 ; i<chargingShortCodes.length ; i++) {
					if(chargingShortCodes[i].equals(destAddr.toString())) {
						ischargingSC = true;
						break;
					}
				}

				if(ischargingSC) {
					
					Response respSm = SmsValidation.parseRegEx(receivedSMS);
					Logger.sysLog(LogValues.info, this.getClass().getName(), "Response: " + respSm);
					if(respSm!= null && respSm.getStat()!= null && respSm.getStat().equalsIgnoreCase("DELIVRD")) {
						
						Logger.sysLog(LogValues.info, this.getClass().getName(), " , status: "+respSm.getStat());
						String requestData = "&msisdn="+sourceAddr+"&transactionID="+sequenceNumber+"&subServiceId="+destAddr+"&status=active&action=act";
						url +=  requestData;

						Logger.sysLog(LogValues.info, this.getClass().getName(), "Hitting DLR_provisoningUrl: for success");
						
						Message msg = new Message(destAddr,sourceAddr,0,"DR", 3, 1);
						HttpGateway g = new HttpGateway();
						g.sendGETRequest(url, msg);
					}
					else {
						Logger.sysLog(LogValues.info, this.getClass().getName(), "Hitting DLR_provisoningUrl: for failure");
						
						String requestData = "&msisdn="+sourceAddr+"&transactionID="+sequenceNumber+"&subServiceId="+destAddr+"&status=parking&action=grace";
						url +=  requestData;
						Message msg = new Message(destAddr,sourceAddr,0,"DR", 3, 1);
						HttpGateway g = new HttpGateway();
						g.sendGETRequest(url, msg);
					}
					
					Logger.sysLog(LogValues.info, this.getClass().getName(), "DLR_provisoningUrl: "+url );
				}
			}

			} else {
				cdr.setStatus("Success");
				cdr.setTime(CoreUtils.getCurrentTimeStamp());

				receivedSMS = new String(deliverSm.getShortMessage());

				// ayu comment
				Logger.sysLog(LogValues.info, this.getClass().getName(), "5. Received SMS: " + receivedSMS);

				OptionalParameter[] parameters = deliverSm.getOptionalParameters();

				cdr.setParameters(parameters);

				if (receivedSMS.equals("") && parameters != null) 
				{

					for (int i = 0; i < parameters.length; i++) {
						byte[] param = parameters[i].serialize();
						
						Logger.sysLog(LogValues.info , this.getClass().getName() , "parameters.length" +parameters.length);
						

						int tag = CoreUtils.getInteger(param, 0, 2);
						Logger.sysLog(LogValues.info , this.getClass().getName() ,"int tag"+tag);
						int length = CoreUtils.getInteger(param, 2, 2);
						Logger.sysLog(LogValues.info , this.getClass().getName() , "we are sending in CoreUtils.getInteger(param, 2, 2) where  param = "+param);
						Logger.sysLog(LogValues.info , this.getClass().getName() ,"int length"+length);
						String value = CoreUtils.getString(param, 4, length);
						
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"here we get tag:" + tag + " | length :" + length + " | value:" + value + " | " + eventJson);


						Logger.sysLog(LogValues.debug, this.getClass().getName(),
								"tag:" + tag + " | length :" + length + " | value:" + value + " | " + eventJson);

						if (tag == Tag.MESSAGE_PAYLOAD.code()) {
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"tag:" + tag + " | length :" + length + " | value:" + value + " | " + eventJson);
							receivedSMS = value;
						} else {
							Logger.sysLog(LogValues.debug, this.getClass().getName(),
									"Short message empty and Message payload parameter not received!");
							
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"Short message empty and Message payload parameter not received!");
						}
					}
				}

				if (receivedSMS.length() > 0)

					receivedSMS = URLDecoder.decode(SMPPMessageListener.getTrimmedMessage(receivedSMS, true), "UTF-8")
					.trim();

				if (cdr.getParameters() != null)
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"Got Optional parameters : Length: " + parameters.length);

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" MO Received :: [" + sourceAddr + "][" + destAddr + "] ::  Message: " + receivedSMS);
			} // End Of If Else

			cdr.setDeliveryReport(deliveryReport);
			cdr.setCircle(this.circle);

			if (sourceAddr != null && sourceAddr.length() > 0)
				cdr.setSender(sourceAddr.trim());
			if (destAddr != null && destAddr.length() > 0)
				cdr.setReceiverMsisdn(destAddr.trim());
			if (serviceType != null && serviceType.length() > 0)
				cdr.setServiceType(serviceType.trim());

			if (receivedSMS == null || receivedSMS.length() == 0)
				receivedSMS = "";

			SmsValidation validator = new SmsValidation();
			validator.parseAndValidate(receivedSMS, cdr);

		} catch (ArrayIndexOutOfBoundsException aiobe) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Parsing Event from SMSC :: " + eventJson);
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Error Receiving SMSC Event :: " + eventJson + "\n" + Logger.getStack(e));
		} // End Of Try Catch

	}// End Of Method

	public void onAcceptAlertNotification(AlertNotification alertNotification) {
	}// End Of Method

	public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
		return null;
	}// End Of Method

	public static String getTrimmedMessage(String receivedSMS) {
		return SMPPMessageListener.getTrimmedMessage(receivedSMS, false);
	}// End Of Method

	public static String getTrimmedMessage(String receivedSMS, boolean checkUnicode) {

		try {
			String encodedString;

			// ayu
			Logger.sysLog(LogValues.info, SMPPMessageListener.class.getName(),
					"recieved sms: " + receivedSMS + "checkUnicode: " + checkUnicode);

			if (receivedSMS != null) {
				receivedSMS = receivedSMS.replaceAll("\n", "");
				encodedString = URLEncoder.encode(receivedSMS, "UTF-16BE").replaceAll("%00", "");

				Logger.sysLog(LogValues.info, SMPPMessageListener.class.getName(), "Encoded String : " + encodedString);

				if (encodedString.endsWith("+"))
					encodedString = encodedString.substring(0, encodedString.length() - 1);

				if (checkUnicode && CoreUtils.isArabic(receivedSMS) == false) {
					// receivedSMS = URLDecoder.decode(encodedString, "UTF-8").trim();

					receivedSMS = encodedString.trim();
					Logger.sysLog(LogValues.info, SMPPMessageListener.class.getName(),
							"Message is arabic. Returning encoded string."  +receivedSMS);
				} else {
					Logger.sysLog(LogValues.info, SMPPMessageListener.class.getName(),
							"Returning string encoded in UTF-8.");
					receivedSMS = URLDecoder.decode(encodedString, "UTF-8").trim();
				}
			} else
				receivedSMS = "";

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SMPPMessageListener.class.getName(),
					" Error Reading Received SMS Content :: " + e.getMessage());
			receivedSMS = "";
		}

		Logger.sysLog(LogValues.info, SMPPMessageListener.class.getName(),
				"Returning "+receivedSMS);
		return receivedSMS;

	}// End Of Method

	protected static String getMessageId(String msgId) {

		String messageId = "";

		try {

			long receiptId = Long.parseLong(msgId) & 0xffffffff;
			messageId = Long.toString(receiptId, 16).toUpperCase().trim();

		} catch (NumberFormatException nbe) {
			/** Assumes Message ID already in HEX */
			if (msgId != null) {
				messageId = msgId.toUpperCase();
			}
		} catch (Exception e) {
			if (msgId != null) {
				/** Assumes Message ID already in HEX */
				messageId = msgId.toUpperCase();
			} else {
				Logger.sysLog(LogValues.warn, SMPPMessageListener.class.getName(),
						" Unable to Read MessageID :: " + e.getMessage());
				messageId = SMSController.DefaultMessageID;
			}
		}

		/*
		 * if( messageId.length() > 0 ) { messageId = messageId.replaceAll("/",
		 * "").replaceAll("\\\\", ""); }
		 */
		return messageId;

	}// End Of Method

}
