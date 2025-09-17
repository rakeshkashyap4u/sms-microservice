package com.rakesh.sms.main;

import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakesh.sms.beans.Header;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.beans.RequestFormat;
import com.rakesh.sms.beans.SMSC;
import com.rakesh.sms.bo.GatewayBo;
import com.rakesh.sms.cdr.SmsCdrBean;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;
import com.rakesh.sms.queue.QueueManager;
import com.rakesh.sms.queue.SmsQueue;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.Expression;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.util.XmlParser;

import jakarta.annotation.PostConstruct;
import jakarta.jms.Connection;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Session;


@Component
public class Pusher extends Thread {

	private static Connection consumerConnection;
	public static int ThreadBurstTime, WaitTime;
	private static Session consumerSession;
	private static GatewayBo gatewayBoImpl;
	private static String defaultCircle;
	private static boolean start;

	private MessageConsumer consumer;
	private Object tpsLock, lock;
	private long id, smsCount;
	private boolean push;
	private int type;
	
	
	
	

	public Pusher() {

	}

	public Pusher(int queue) {
		this.type = queue;
		this.consumer = SmsQueue.createConsumer(Pusher.consumerSession, this.type);
		this.lock = new Object();
		this.id = this.getId();
		Pusher.start = true;
		this.push = false;
	}// End Of Constructor

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	  @Autowired
	public void setGatewayBoImpl(GatewayBo gatewayBoImpl) {
		Pusher.gatewayBoImpl = gatewayBoImpl;
		Logger.sysLog(LogValues.info, Pusher.class.getName(), " SMSClient HTTP Gateway Initialized. ");
	}

	synchronized public static GatewayBo getGatewayBoImpl() {
		return Pusher.gatewayBoImpl;
	}

	public static String getDefaultCircle() {
		return Pusher.defaultCircle;
	}
	
	@JmsListener(destination = "smsqueue1", concurrency = "5-10")
	public void receiveMessage(String json) throws InterruptedException, JsonMappingException, JsonProcessingException {
	    // Update circle info
		ObjectMapper mapper = new ObjectMapper();
	    Message msg = mapper.readValue(json, Message.class);
		if (msg == null) {
            // No message available, short sleep to avoid CPU spin
            Thread.sleep(5);
            
        }

        String response = "";

        // Check expiry
        if (msg.isExpired()) {
            Logger.sysLog(LogValues.info, this.getClass().getName(),
                    "SMS EXPIRED :: " + msg.toString());
            response = "false: SmsExpired";
        } else {
            Logger.sysLog(LogValues.info, this.getClass().getName(),
                    " [" + this.id + "] SMS Popped | Remaining= " + this.smsCount);

            this.updateCircle(msg);
            Logger.sysLog(LogValues.info, this.getClass().getName(),
                    " [" + this.id + "] Circle Updated to: " + msg.getCircle());

            // Reconnect SMSC if inactive
            if (!CoreUtils.isSMSCActive(msg.getCircle())) {
                ReConnector connector = ReConnector.getReconnector(msg.getCircle());
                try {
                    connector.safeStart();
                    Logger.sysLog(LogValues.info, this.getClass().getName(),
                            "Waiting for SMSC [" + msg.getCircle() + "] to be Reconnected...");
                    connector.join();
                } catch (Exception fce) {
                    Logger.sysLog(LogValues.error, this.getClass().getName(),
                            fce.getMessage() + " :: Unable to connect to SMSC [" + msg.getCircle() + "]");
                }
            }

//            // Maintain TPS
//            while (this.checkTPS(msg) && this.tpsLock != null) {
//                Logger.sysLog(LogValues.info, this.getClass().getName(),
//                        " [" + this.id + "] TPS Achieved for current slot");
//                synchronized (this.tpsLock) {
//                    this.tpsLock.wait();
//                }
//                Logger.sysLog(LogValues.debug, this.getClass().getName(),
//                        " [" + this.id + "] TPS Available");
//            }

            // Push the message
            try {
				response = this.push(msg, false);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            this.smsCount--;
        }

        // Handle failed push / callback / retry
        if (response.toLowerCase().startsWith("false")) {
            SmsCdrBean cdr = CoreUtils.getSmsCDR(msg);
            cdr.setMessageId(SMSController.DefaultMessageID);

            if (response.contains(":")) {
                response = response.replaceAll("false:", "Failure");
                cdr.setStatus(response);
            } else {
                cdr.setStatus("Failure");
            }

            if (msg.isCallback()) {
                Logger.sysLog(LogValues.info, this.getClass().getName(),
                        " [" + this.id + "][" + msg.getRequiredMsisdn() +
                                "] SMS Sending Failed | Sending Failure Callback...");
                msg.getCallbackDetails().setCallbackStatus("Failure");
                msg.getCallbackDetails().setFailureReason("Others");
                Logger.sysLog(LogValues.info, this.getClass().getName(),
                        "Sending callback: " + msg.toString());
                CoreUtils.sendCallback(msg);
            } else if (!msg.isExpired()) {
                CoreUtils.retrySms(msg);
            }

            //////////CdrCreator.saveAsXML(cdr);
        }

	}

	public static HttpGateway getHttpGateway(String circle) {

		SMSC smsc = QueueManager.getSMSC(circle);

		if (smsc != null)
			return smsc.getHttpGateway();
		else {
			return null;
		}

	}// End Of Method

	
	
	
	public static HttpGateway getRemovedHttpGateway(String circle) {

		SMSC smsc = QueueManager.getRemovedSMSC(circle);

		if (smsc != null)
			return smsc.getHttpGateway();
		else
			return null;

	}// End Of Method

	  @PostConstruct
	public static void init() {

//		try {
//			
//			try {
//			    // Create a new connection (example for ActiveMQ)
//				// Username and password only
//				
//
//			    // Start the connection
//			    Pusher.consumerConnection.start();
//
//			    // Create session
//			    Pusher.consumerSession = Pusher.consumerConnection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
//
//			} catch (NullPointerException e) {
//			    Logger.sysLog(LogValues.error, Pusher.class.getName(), " ERROR Creating Consumer Session ");
//			} catch (Exception e) {
//			    Logger.sysLog(LogValues.error, Pusher.class.getName(),
//			            " ActiveMQ Consumer Connection Initialization ERROR \n" + Logger.getStack(e));
//			}
//
//		
//
//		} catch (NullPointerException e) {
//			Logger.sysLog(LogValues.error, Pusher.class.getName(), " ERROR Creating Consumer Session ");
//		} catch (Exception e) {
//			Logger.sysLog(LogValues.error, Pusher.class.getName(),
//					" ActiveMQ Consumer Connection Initialization ERROR \n" + Logger.getStack(e));
//		}

	//	Pusher.ThreadBurstTime = 1000 / QueueManager.TPS;

//		try {
//			Pusher.WaitTime = Integer.parseInt(CoreUtils.getProperty("transactionTimer"));
//			Pusher.WaitTime += 500; // In milliseconds
//		} catch (Exception e) {
//			Pusher.WaitTime = 5000;
//		} // End Of Try Catch

		//try {

//			if (QueueManager.smscList.isEmpty() || QueueManager.smscList.size() == 0) 
//			{
//
//				SMSC smsc = new SMSC();
//				SMSCConfigs config = Pusher.gatewayBoImpl.getConfigDetails();
//				config.setCircle(config.getCircle().toUpperCase().trim());
//				smsc.setConfig(config);
//				smsc.setFormat(Pusher.gatewayBoImpl.getFormatDetails(config.getCid().toString()));
//				Pusher.defaultCircle = config.getCircle();
//				Logger.sysLog(LogValues.info, Pusher.class.getName(), " Default Circle= " + Pusher.defaultCircle);
//
//				if (CoreUtils.getProtocol() == CoreEnums.Protocol.SMPP) {
//
//					ESME esme = new ESME(config);
//
//					if (esme.isConnected()) {
//						smsc.setSmppGateway(esme);
//						smsc.setHttpGateway(new HttpGateway(config.getTimeout()));
//						QueueManager.addSMSC(Pusher.defaultCircle, smsc);
//					} else {
//						Logger.sysLog(LogValues.info, Pusher.class.getName(),
//								" SMSC Connection Failed ---(Retrying)---");
//						ReConnector.reconnect(config.getCircle());
//					} // End Of Connected ESME
//
//				} else if (CoreUtils.getProtocol() == CoreEnums.Protocol.HTTP) {
//
//					smsc.setHttpGateway(new HttpGateway(config.getTimeout(), true));
//					QueueManager.addSMSC(Pusher.defaultCircle, smsc);
//
//					System.out.println("here you are now");
//
//					if (smsc.getFormat().getRegister().equals("1")) {
//
//						SMSCFormats format = smsc.getFormat();
//						RegistrationRequest request = null;
//						List<Header> headers = new ArrayList<Header>();
//
//						if (CoreUtils.getProperty("country").equalsIgnoreCase("airtel")
//								&& CoreUtils.getProperty("operator").equalsIgnoreCase("smart")) {
//							// specific header for Smart Philipines
//							String password[] = CoreUtils.generateEncryptedPassword(smsc.getConfig().getPassword())
//									.split(",");
//
//							Header header = new Header("X-WSSE",
//									"UsernameToken Username=\"" + smsc.getConfig().getUserid() + "\", PasswordDigest=\""
//											+ password[0] + "\", Nonce=\"" + password[1] + "\", Created=\""
//											+ password[2] + "\"");
//
//							Logger.sysLog(LogValues.info, Pusher.class.getName(),
//									"Adding header: " + header.toString());
//
//							headers.add(header);
//						}
//
//						/*
//						 * if (CoreUtils.getProperty("country").equalsIgnoreCase("tanz") &&
//						 * CoreUtils.getProperty("operator").equalsIgnoreCase("tigo")) { // specific
//						 * header for Tigo Tanzania
//						 * 
//						 * 
//						 * Header header = new Header("Content-Type:application/json"
//						 * 
//						 * Logger.sysLog(LogValues.info, Pusher.class.getName(), "Adding header: " +
//						 * header.toString());
//						 * 
//						 * headers.add(header); }
//						 */
//
//						if (format.getMoRegisterFormat() != null && format.getMoRegisterFormat() != "") {
//							request = (RegistrationRequest) XmlParser.parseXml(format.getMoRegisterFormat(),
//									new RegistrationRequest());
//							request.setHeaders(headers);
//							smsc.setRequestMO(request);
//							String deregisterMoUrl = smsc.getHttpGateway().registerMO(request);
//
//							if (deregisterMoUrl != null && deregisterMoUrl.length() > 0)
//								smsc.getRequestMO().setDeregistrationURL(deregisterMoUrl);
//						} else {
//							Logger.sysLog(LogValues.info, Pusher.class.getName(), "No MO Registration format defined!");
//						}
//
//						if (format.getMtRegisterFormat() != null && format.getMtRegisterFormat() != "") {
//							request = (RegistrationRequest) XmlParser.parseXml(format.getMtRegisterFormat(),
//									new RegistrationRequest());
//							request.setHeaders(headers);
//							smsc.setRequestMT(request);
//							String deregisterMtUrl = smsc.getHttpGateway().registerMT(request);
//
//							if (deregisterMtUrl != null && deregisterMtUrl.length() > 0)
//								smsc.getRequestMT().setDeregistrationURL(deregisterMtUrl);
//						} else {
//							Logger.sysLog(LogValues.info, Pusher.class.getName(), "No MT Registration format defined!");
//						}
//
//					} // End Of Registration Process
//				} else if (CoreUtils.getProtocol() == CoreEnums.Protocol.SOAP) {
//					smsc.setHttpGateway(new HttpGateway(config.getTimeout(), true));
//					QueueManager.addSMSC(Pusher.defaultCircle, smsc);
//
//					if (smsc.getFormat().getRegister().equals("1")) {
//
//						SMSCFormats format = smsc.getFormat();
//						RegistrationRequest request = null;
//
//						if (format.getMoRegisterFormat() != null && format.getMoRegisterFormat() != "") {
//							request = (RegistrationRequest) XmlParser.parseXml(format.getMoRegisterFormat(),
//									new RegistrationRequest());
//							smsc.setRequestMO(request);
//							smsc.getHttpGateway().registerMO(request);
//						} else {
//							Logger.sysLog(LogValues.info, Pusher.class.getName(), "No MO Registration format defined!");
//						}
//
//						if (format.getMtRegisterFormat() != null && format.getMtRegisterFormat() != "") {
//							request = (RegistrationRequest) XmlParser.parseXml(format.getMtRegisterFormat(),
//									new RegistrationRequest());
//							smsc.setRequestMT(request);
//							smsc.getHttpGateway().registerMT(request);
//						} else {
//							Logger.sysLog(LogValues.info, Pusher.class.getName(), "No MT Registration format defined!");
//						}
//
//					} // End Of Registration Process
//
//				} // End Of Protocol Check
//
//			} // End Of Default SMSC Loader
//
//		} catch (Exception e) {
//			Logger.sysLog(LogValues.error, Pusher.class.getName(),
//					" Default Gateway Initialization ERROR \n" + Logger.getStack(e));
//		}

	}// End Of Method

	@Override
	public void run() {
	    Logger.sysLog(LogValues.trace, Pusher.class.getName(), "ZZZ Inside run of Pusher");
	    System.out.println("here pusher run");

	    try {
	        while (Pusher.start) { // Outer loop for thread life

	            // Pop message from JMS queue (blocks up to 1 second)
	            Message msg = SmsQueue.pop(consumer, this.type);

	            if (msg == null) {
	                // No message available, short sleep to avoid CPU spin
	                Thread.sleep(5);
	                continue;
	            }

	            String response = "";

	            // Check expiry
	            if (msg.isExpired()) {
	                Logger.sysLog(LogValues.info, this.getClass().getName(),
	                        "SMS EXPIRED :: " + msg.toString());
	                response = "false: SmsExpired";
	            } else {
	                Logger.sysLog(LogValues.info, this.getClass().getName(),
	                        " [" + this.id + "] SMS Popped | Remaining= " + this.smsCount);

	                this.updateCircle(msg);
	                Logger.sysLog(LogValues.info, this.getClass().getName(),
	                        " [" + this.id + "] Circle Updated to: " + msg.getCircle());

	                // Reconnect SMSC if inactive
	                if (!CoreUtils.isSMSCActive(msg.getCircle())) {
	                    ReConnector connector = ReConnector.getReconnector(msg.getCircle());
	                    try {
	                        connector.safeStart();
	                        Logger.sysLog(LogValues.info, this.getClass().getName(),
	                                "Waiting for SMSC [" + msg.getCircle() + "] to be Reconnected...");
	                        connector.join();
	                    } catch (Exception fce) {
	                        Logger.sysLog(LogValues.error, this.getClass().getName(),
	                                fce.getMessage() + " :: Unable to connect to SMSC [" + msg.getCircle() + "]");
	                    }
	                }

	                // Maintain TPS
	                while (this.checkTPS(msg) && this.tpsLock != null) {
	                    Logger.sysLog(LogValues.info, this.getClass().getName(),
	                            " [" + this.id + "] TPS Achieved for current slot");
	                    synchronized (this.tpsLock) {
	                        this.tpsLock.wait();
	                    }
	                    Logger.sysLog(LogValues.debug, this.getClass().getName(),
	                            " [" + this.id + "] TPS Available");
	                }

	                // Push the message
	                response = this.push(msg, false);
	                this.smsCount--;
	            }

	            // Handle failed push / callback / retry
	            if (response.toLowerCase().startsWith("false")) {
	                SmsCdrBean cdr = CoreUtils.getSmsCDR(msg);
	                cdr.setMessageId(SMSController.DefaultMessageID);

	                if (response.contains(":")) {
	                    response = response.replaceAll("false:", "Failure");
	                    cdr.setStatus(response);
	                } else {
	                    cdr.setStatus("Failure");
	                }

	                if (msg.isCallback()) {
	                    Logger.sysLog(LogValues.info, this.getClass().getName(),
	                            " [" + this.id + "][" + msg.getRequiredMsisdn() +
	                                    "] SMS Sending Failed | Sending Failure Callback...");
	                    msg.getCallbackDetails().setCallbackStatus("Failure");
	                    msg.getCallbackDetails().setFailureReason("Others");
	                    Logger.sysLog(LogValues.info, this.getClass().getName(),
	                            "Sending callback: " + msg.toString());
	                    CoreUtils.sendCallback(msg);
	                } else if (!msg.isExpired()) {
	                    CoreUtils.retrySms(msg);
	                }

	                //////////CdrCreator.saveAsXML(cdr);
	            }

	        } // End of while(Pusher.start)

	    } catch (InterruptedException e) {
	        Logger.sysLog(LogValues.info, this.getClass().getName(),
	                " [" + this.id + "][" + this.type + "] SMS Listener Interrupted");
	        Thread.currentThread().interrupt(); // Restore interrupted status
	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(),
	                " [" + this.id + "][" + this.type + "] Exception while Pushing SMS \n" + Logger.getStack(e));
	        Logger.sysLog(LogValues.fatal, this.getClass().getName(),
	                " [" + this.id + "][" + this.type + "] SMSClient has STOPPED | Please RESTART the client ASAP");
	    }

	} // End of run()
	private void updateCircle(Message msg) {

		if (msg == null)
			return;

		if (msg.getCircle().length() == 0) {

			if (CoreUtils.getCircleForNumberSeries(msg.getRequiredMsisdn()) != null) {

				SMSCConfigs config = CoreUtils.getCircleForNumberSeries(msg.getRequiredMsisdn());

				if (config != null && QueueManager.containsSMSC(config.getCircle()) == false) {
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" SeriesToCircle Mapping Found :: " + config.getCircle());
					// ReConnector.reconnect( config.getCircle() );
					msg.setCircle(config.getCircle());
				} else if (config != null) {
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" SeriesToCircle Mapping Found :: " + config.getCircle());
					msg.setCircle(config.getCircle());
				} else {
					Logger.sysLog(LogValues.info, Pusher.class.getName(),
							" No Configuration found for this Msisdn Series | Using Default Circle ");
					msg.setCircle(Pusher.defaultCircle);
				}

			} else {
				msg.setCircle(Pusher.defaultCircle);
			}

		}

	}// End Of Method

	/**
	 * if synced is TRUE => Directly Push SMS to ESME and return the String response
	 * to Controller if synced is FALSE => Push SMS to Queue, and return String
	 * response of successful queue submission to Controller
	 * 
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public String push(Message msg, boolean synced) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		String response = String.valueOf(true);
		final String FAIL = String.valueOf(false);

		/**
		 * Do Not Remove the checks, Even though it has been checked before. As it must
		 * be checked in SYNC mode
		 */
		if (msg == null) {
			Logger.sysLog(LogValues.debug, this.getClass().getName(),
					" Queue Empty | No Message(s) Found | Ending Alotted Slot ");
			this.push = false;
			return FAIL;
		} else if (msg.isExpired()) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), " SMS EXPIRED :: " + msg.toString());
			return FAIL + ": SmsExpired";
		} else if (msg.getMessage().length() > 0) 
		{

			String sublog = "[" + msg.getMsisdn() + "][" + msg.getCli() + "]";

			if (synced) 
			{
				this.updateCircle(msg);
			}

			SMSC smsc = QueueManager.getSMSC(msg.getCircle());
			Logger.sysLog(LogValues.info, Pusher.class.getName(), " Circle " + msg.getCircle());

			if (smsc == null) {
				ReConnector.reconnect(msg.getCircle());
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" No SMSC Configuration for the Circle " + msg.getCircle() + " | Cannot Push SMS ");
				return FAIL + ": NoSmscConfiguration";
			} else if (smsc.getFormat() == null) {

				SMSCFormats defaultFormat = new SMSCFormats();
				defaultFormat.setCid(String.valueOf(smsc.getConfig().getCid()));
				String fid = Pusher.gatewayBoImpl.addFormatDetails(defaultFormat);

				if (fid != null && fid.equals("-1") == false) {
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" NO Message Format | Inserting default format... ");
					defaultFormat.setRid(fid);
					smsc.setFormat(defaultFormat);
				} else {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							" Message Format Configuration for the Circle NOT Found ");
					return FAIL + ": WrongConfiguration";
				}
			}

			String request = CoreUtils.getRequiredMessage(smsc.getFormat(), msg);
			Logger.sysLog(LogValues.debug, this.getClass().getName(),
					smsc.getFormat().getMode().toUpperCase() + " Formatted SMS Request= " + request);
			msg.setMessage(request);

			if (request != null && request.length() > 0) {

				/*
				 * if(msg.getFlag()==CoreEnums.SMSFlag.FLASH_SMS) { //create a session }
				 */

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" [" + this.id + "]  Sending Message to Local Gateway:: " + CoreEnums.SMSType.values[this.type]
								+ " Circle= " + msg.getCircle() + " |  To= " + msg.getMsisdn() + " | From= "
								+ msg.getCli() + " | Content= " + msg.getMessage());

				if (msg.getFlag() == CoreEnums.SMSFlag.FLASH_SMS
						&& CoreUtils.getProperty("country").equalsIgnoreCase("NER")
						&& CoreUtils.getProperty("operator").equalsIgnoreCase("AIRTEL")) {
					SmsCdrBean cdr = CoreUtils.getSmsCDR(msg);
					smsc.getHttpGateway().sendGETRequest(CoreUtils.getProperty("ussdUrl"), msg, null);
					//////////CdrCreator.saveAsXML(cdr);

				}

				else if (msg.getMode() == CoreEnums.Protocol.SMPP) {
					if (synced) {
						response = smsc.getSmppGateway().sendSyncMessage(msg);
					} else {
						smsc.getSmppGateway().sendMessage(msg);
					}
				}
				
				

				else if (msg.getMode() == CoreEnums.Protocol.HTTP) {

					// System.out.println("HTTP PROTOCOL");

					SmsCdrBean cdr = CoreUtils.getSmsCDR(msg);

					if (smsc.getFormat().getMode().equalsIgnoreCase("POST")) {

						 System.out.println("POST METHOD");

						if (smsc.getFormat().getRequestFormat() != null
								&& !smsc.getFormat().getRequestFormat().equals("")) {

							System.out.println("RequestFormat is not null");
							String resp = null;
							String messageId = "";

							/* change for auth key generation TIGO TANZANIA */

							msg.setAuthkey(CoreUtils.getAuthKey());
							msg.setCorelatorId(CoreUtils.getcorelatorId());

							// System.out.println("CoreUtils.getAuthKey()"+CoreUtils.getAuthKey());

							// Logger.sysLog(LogValues.info, this.getClass().getName(), "authkey(Tigo
							// Tanzania): " + msg.getAuthkey() + " msgid(Mexico): " + msg.getMsgid() + "
							// corelatorId(BurkinaFaso): " + msg.getCorelatorId() + "
							// senderName(BurkinaFaso): " + msg.getSenderName());

							/* end of change for auth key generation TIGO TANZANIA */

							RequestFormat format = (RequestFormat) XmlParser
									.parseXml(smsc.getFormat().getRequestFormat(), new RequestFormat());
							Properties p = null;
							if (smsc.getFormat().getOptions() != null && !smsc.getFormat().getOptions().equals("")) {
								String option = smsc.getFormat().getOptions();

								// System.out.println("option"+option);

								Logger.sysLog(LogValues.info, this.getClass().getName(),
										"options in smsformat id " + smsc.getFormat().getCid() + " " + option);
								Expression exp = new Expression();
								p = exp.msgAdditionParameter(option);

								// String Smsauthkey = exp.generateAuthKey(p.getProperty("PServiceId") ,
								// p.getProperty("PpresharedKey")); //uncomment for batelco or mtn yemen or
								// omantel_oman

								if (CoreUtils.getProperty("country").equalsIgnoreCase("peru")
										&& CoreUtils.getProperty("operator").equalsIgnoreCase("entel")) {
									String Smsauthkey = exp.generateAuthKey("2062", "Wo3Iyh3jNyzkRDNJ"); // its for
																											// entel
																											// peru
									p.put("authKey", Smsauthkey);
								}

								// System.out.println("country "+CoreUtils.getProperty("country"));
								// System.out.println("operator "+CoreUtils.getProperty("operator"));

								if (CoreUtils.getProperty("country").equalsIgnoreCase("TUN")
										&& CoreUtils.getProperty("operator").equalsIgnoreCase("ORN")) {
									String key = exp.getTokenAPIforOT();
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											"Token generated:  " + key);
									String token = "Bearer " + key;
									Logger.sysLog(LogValues.info, this.getClass().getName(), "Final Token:  " + token);
									p.put("token", token);
								}

								if (CoreUtils.getProperty("country").equalsIgnoreCase("TNZ")
										&& CoreUtils.getProperty("operator").equalsIgnoreCase("TIGO")) {
									String key = exp.TokenAPI();
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											"Token generated:  " + key);
									String token = "Bearer " + key;
									Logger.sysLog(LogValues.info, this.getClass().getName(), "Final Token:  " + token);
									p.put("token", token);

									Date date = new Date();
									Timestamp timestamp = new Timestamp(System.currentTimeMillis());
									String requestTimeStamp = Long.toString(timestamp.getTime());

									p.put("reqtime", requestTimeStamp);

									String part2 = msg.getMsisdn().substring(0, 8);

									String part1 = requestTimeStamp.substring(0, 6);

									String requestid = part1 + part2;

									p.put("requestid", requestid);

								}

								if (CoreUtils.getProperty("country").equalsIgnoreCase("oman")
										&& CoreUtils.getProperty("operator").equalsIgnoreCase("omantel")) {
									String sendingTime = exp.getTimeZone("Asia/Muscat");
									p.put("sendDate", sendingTime);
								}
							}

									Logger.sysLog(LogValues.info, this.getClass().getName(),
									"format request: " + format.getRequest());
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"format header: " + format.getHeader());
							format.setRequest(CoreUtils.parseUrl(format.getRequest(), msg, p));
							format.setHeader(CoreUtils.parseHeader(format.getHeader(), msg, p));

							// System.out.println("request "+CoreUtils.parseUrl(format.getRequest(),
							// msg,p));

							// System.out.println("header "+CoreUtils.parseHeader(format.getHeader(),
							// msg,p));

							/*
							 * specific header for Smart Philippines
							 */

							if (smsc.getConfig().getPassword() != null) {
								String password[] = CoreUtils.generateEncryptedPassword(smsc.getConfig().getPassword())
										.split(",");
								Header header = new Header("X-WSSE",
										"UsernameToken Username=\"" + smsc.getConfig().getUserid()
												+ "\", PasswordDigest=\"" + password[0] + ", Nonce=\"" + password[1]
												+ "\", Created=\"" + password[2] + "\"");

								if (format.getHeader() != null)
									format.getHeader().add(header);
								else {
									List<Header> headers = new ArrayList<Header>();
									headers.add(header);
									format.setHeader(headers);
								}
							}

							
							
							// msg.setMessage("ATest");
							String serviceURI = CoreUtils.parseUrl(smsc.getConfig().getServiceUri(), msg, p);
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"ServiceURI: " + smsc.getConfig().getServiceUri() + ", parsedURI: " + serviceURI);

							// Mexico
							Logger.sysLog(LogValues.info, this.getClass().getName(), "Request " + format.toString());

							resp = smsc.getHttpGateway().sendSyncPOSTRequest(serviceURI, format.getRequest(),
									format.getHeader());

							if (!resp.equals("")) {
								if (resp.contains("faultcode")) {
									cdr.setMessageId(SMSController.DefaultMessageID);
									cdr.setStatus("Failure");
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											sublog + " SMSC Response Received for Short Message: " + cdr.getMessageId()
													+ " :Failure");
								} else if (resp.indexOf("<ns1:result>") != -1) {
									int index = resp.indexOf("<ns1:result>");
									messageId = resp.substring(index + "<ns1:length>".length(),
											resp.indexOf("</ns1:result>"));
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											sublog + " SMSC Response Received for Short Message: " + messageId);
									cdr.setStatus("Success");
									cdr.setMessageId(messageId);
								} else if (resp.indexOf("InsertContentResult") != -1) {
									int index = resp.indexOf("<InsertContentResult>");
									messageId = resp.substring(index + "<InsertContentResult>".length(),
											resp.indexOf("</InsertContentResult>"));
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											sublog + " SMSC Response Received for Short Message: " + messageId);
									cdr.setStatus("Success");
									cdr.setMessageId(messageId);
								} else if (resp.contains("responseCode:")) {
									String[] respCode = resp.split("-responseCode:");
									resp = respCode[0];
									String rCode = respCode[1];

									if (rCode.equals("200")) {
										cdr.setStatus("Success");
										Logger.sysLog(LogValues.info, this.getClass().getName(),
												sublog + " Setting response code 200 to success " + rCode);
									} else {
										cdr.setStatus("Failure " + rCode);
										Logger.sysLog(LogValues.info, this.getClass().getName(),
												sublog + " Setting response code for Failure " + rCode);
									}
								}
							}

						} 
						else
						{
							
							
							
							if(smsc.getFormat().getRequestformat()!=null)
							{
								
							String requestTemplate = smsc.getFormat().getRequestformat(); // {"msisdn":"%msisdn%","message":"%message%"}
							String requestData = requestTemplate
							                        .replace("%msisdn%", msg.getMsisdn())
							                        .replace("%message%", msg.getMessage());
							System.out.println("calling sendPOSTRequest with -> "+requestData);
							smsc.getHttpGateway().sendPOSTRequest(smsc.getConfig().getServiceUri(),
									requestData,
									//, msg.getMessage(),
									msg, null);
							}
							else
							{
								System.out.println("calling sendPOSTRequest with -> "+msg.getMessage());
								smsc.getHttpGateway().sendPOSTRequest(smsc.getConfig().getServiceUri(),
										
										msg.getMessage(),
										msg, null);
							}
						}

					} else if (smsc.getFormat().getMode().equalsIgnoreCase("GET")) {
						if (smsc.getFormat().getOptions() != null && !smsc.getFormat().getOptions().equals("")) {
							String option = smsc.getFormat().getOptions();
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"options in smsformat id " + smsc.getFormat().getCid() + " " + option);
							Expression exp = new Expression();
							Properties p = exp.msgAdditionParameter(option);
							String Smsauthkey = exp.generateAuthKey(p.getProperty("PServiceId"),
									p.getProperty("PpresharedKey")); // uncomment for batelco or mtn yemen or
																		// omantel_oman

							p.put("authKey", Smsauthkey); // both lines

							if (CoreUtils.getProperty("country").equalsIgnoreCase("oman")
									&& CoreUtils.getProperty("operator").equalsIgnoreCase("omantel"))

							{
								String sendingTime = exp.getTimeZone("Asia/Muscat");
								p.put("sendDate", sendingTime);
							}

							smsc.getHttpGateway().sendGETRequest(smsc.getConfig().getServiceUri(), msg, p);

						} else
							smsc.getHttpGateway().sendGETRequest(smsc.getConfig().getServiceUri(), msg, null);
					}

					//////////CdrCreator.saveAsXML(cdr);

				} else if (msg.getMode() == CoreEnums.Protocol.SOAP) {

					SmsCdrBean cdr = CoreUtils.getSmsCDR(msg);

					if (smsc.getFormat().getRequestFormat() != null
							&& !smsc.getFormat().getRequestFormat().equals("")) {
						RequestFormat format = (RequestFormat) XmlParser.parseXml(smsc.getFormat().getRequestFormat(),
								new RequestFormat());
						String option = smsc.getFormat().getOptions();
						Expression exp = new Expression();
						Properties p = null;
						if (option != null)
							p = exp.msgAdditionParameter(option);
						String Smsauthkey = exp.generateAuthKey(p.getProperty("PServiceId"),
								p.getProperty("PpresharedKey")); // uncomment for batelco or mtn yemen or omantel_oman
						p.put("authKey", Smsauthkey); // both lines

						if (CoreUtils.getProperty("country").equalsIgnoreCase("oman")
								&& CoreUtils.getProperty("operator").equalsIgnoreCase("omantel")) {
							String sendingTime = exp.getTimeZone("Asia/Muscat");
							p.put("sendDate", sendingTime);
							
							
							
						}
						
						if(CoreUtils.getProperty("country").equalsIgnoreCase("SDN")
								&& CoreUtils.getProperty("operator").equalsIgnoreCase("ZAIN")) {
							
							
							Timestamp timestamp = new Timestamp(System.currentTimeMillis());
							String requestTimeStamp = Long.toString(timestamp.getTime());

							p.put("reqtime", requestTimeStamp);
							
							String password = CoreUtils.getProperty("password");
							
							
							
						}

						format.setRequest(CoreUtils.parseUrl(format.getRequest(), msg, p));
						String resp = smsc.getHttpGateway().sendSyncPOSTRequest(smsc.getConfig().getServiceUri(),
								format.getRequest(), format.getHeader());
						String messageId = "";
						String reason = "";

						if (!resp.equals("")) {
							if (resp.contains("faultcode")) {
								cdr.setMessageId(SMSController.DefaultMessageID);
								cdr.setStatus("Failure");
								Logger.sysLog(LogValues.info, this.getClass().getName(),
										sublog + " SMSC Response Received for Short Message: " + cdr.getMessageId()
												+ " :Failure");
							} else if (resp.indexOf("<ns1:result>") != -1) {
								int index = resp.indexOf("<ns1:result>");
								messageId = resp.substring(index + "<ns1:length>".length(),
										resp.indexOf("</ns1:result>"));
								Logger.sysLog(LogValues.info, this.getClass().getName(),
										sublog + " SMSC Response Received for Short Message: " + messageId);
								cdr.setStatus("Success");
								cdr.setMessageId(messageId);
							} else if (resp.indexOf("InsertContentResult") != -1) {
								int index = resp.indexOf("<InsertContentResult>");
								messageId = resp.substring(index + "<InsertContentResult>".length(),
										resp.indexOf("</InsertContentResult>"));
								Logger.sysLog(LogValues.info, this.getClass().getName(),
										sublog + " SMSC Response Received for Short Message: " + messageId);
								cdr.setStatus("Success");
								cdr.setMessageId(messageId);
							} else if (resp.contains("<status>1</status>")) {
								cdr.setResponsetime(CoreUtils.getCurrentTimeStamp());
								cdr.setStatus("Success");
								DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
								try {
									DocumentBuilder builder = factory.newDocumentBuilder();
									Document document = builder.parse(new InputSource(new StringReader(resp)));
									Element rootElement = document.getDocumentElement();
									reason = getString("errorDescription", rootElement);
								} catch (ParserConfigurationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SAXException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cdr.setReason(reason);
							} else if (resp.contains("<status>2</status")) {
								cdr.setResponsetime(CoreUtils.getCurrentTimeStamp());
								cdr.setStatus("Failure");
								DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
								try {
									DocumentBuilder builder = factory.newDocumentBuilder();
									Document document = builder.parse(new InputSource(new StringReader(resp)));
									Element rootElement = document.getDocumentElement();
									reason = getString("errorDescription", rootElement);
								} catch (ParserConfigurationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SAXException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								cdr.setReason(reason);
							}

							else if (resp.contains("responseCode:")) {
								String[] respCode = resp.split("-responseCode:");
								resp = respCode[0];
								String rCode = respCode[1];

								if (rCode.equals("200")) {
									cdr.setStatus("Success");
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											sublog + " Setting response code 200 to success " + rCode);
								}

								else {
									cdr.setStatus("Failure " + rCode);
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											sublog + " Setting response code for Failure " + rCode);
								}
							} else if (resp.contains("errorCode:")) {
								String[] str = resp.split("errorCode:");
								String errCode = str[1];
								Logger.sysLog(LogValues.info, this.getClass().getName(),
										sublog + "error code" + errCode);
							}

						}

					} else
						smsc.getHttpGateway().sendPOSTRequest(smsc.getConfig().getServiceUri(), msg.getMessage(), msg,
								null);

					//////////CdrCreator.saveAsXML(cdr);

				} else {
					SmsCdrBean cdr = CoreUtils.getSmsCDR(msg);
					smsc.getHttpGateway().sendPOSTRequest(smsc.getConfig().getServiceUri(), msg.getMessage(), msg,
							null);
					//////////CdrCreator.saveAsXML(cdr);
				} // End Of IF(Protocols)

				this.smsCount--;
				return response;

			} // End Of Request IF Else

		} else {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " NULL SMS Content OR Unknown Configuration ");
			return FAIL + ": NullContent";
		} // End Of Message IF ELSE

		return FAIL;

	}// End Of Method

	public static String getString(String tagName, Element element) {
		NodeList list = element.getElementsByTagName(tagName);
		if (list != null && list.getLength() > 0) {
			NodeList subList = list.item(0).getChildNodes();

			if (subList != null && subList.getLength() > 0) {
				return subList.item(0).getNodeValue();
			}
		}

		return null;
	}

	private boolean checkTPS(Message sms) {

		String circle = sms.getCircle(); // pro
		int tps = QueueManager.TPS; // 15

		// System.out.println("TPS: "+tps);

		boolean result = false;

		if (circle == null || circle.equals("")) {
			circle = Pusher.defaultCircle;
		}

		SMSC smsc = QueueManager.getSMSC(circle);

		try {

			if (smsc != null) {
				Logger.sysLog(LogValues.warn, this.getClass().getName(), " SMS MODE :: " + sms.getMode()
						+ ", sms circle: " + sms.getCircle() + ", sms text: " + sms.getMessage());

				if (sms.getMode() == CoreEnums.Protocol.SMPP) {
					int currentCount = smsc.getSmppGateway().getRunningThreadsCount();

					// System.out.println("Current count of threads: "+currentCount);

					this.tpsLock = smsc.getSmppGateway().getTPSLock();

					if (currentCount >= tps) {
						Logger.sysLog(LogValues.debug, this.getClass().getName(), " TPS Count :: " + currentCount);
						result = true;
					}
				} else if (sms.getMode() == CoreEnums.Protocol.HTTP || sms.getMode() == CoreEnums.Protocol.SOAP) {
					int currentCount = smsc.getHttpGateway().getRunningThreadsCount();
					this.tpsLock = smsc.getHttpGateway().getTPSLock();

					if (currentCount >= tps) {
						result = true;
					}
				} // End Of Protocol Check
			} else {
				this.tpsLock = null;
				result = false;
			} // End Of IF

		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, this.getClass().getName(), " Unable to check TPS :: " + Logger.getStack(e));

			Gateway gateway = smsc.getGateway();
			if (gateway != null && gateway.isConnected()) {
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" Flushing SMSC [" + circle + "] Connection... ");
				smsc.shutdown();
			}
			QueueManager.removeSMSC(circle, smsc);
			// QueueManager.removeSMSC(circle);
			result = false;
		} // End Of Try Catch

		return result;
	}// End Of Method

	/**
	 * (Unused Method to check TPS) -> Gets all the active SMSC connections and
	 * checks number of active Threads on each session. -> If any ones TPS is
	 * achieved, Returns TRUE, else FALSE -> Without knowing the fact, for which
	 * connection next SMS is for. (Currently Using) -> Pops the next SMS to be
	 * pushed, check the circle -> Checks the number of active Threads for that
	 * specific connection. -> Accordingly returns TRUE or FALSE
	 */
	@SuppressWarnings("unused")
	private boolean checkTPS() {

		int tps = QueueManager.TPS;
		boolean result = false;
		String circle = null;

		Iterator<String> iter = QueueManager.smscList.keySet().iterator();

		while (iter.hasNext()) {

			SMSC smsc = QueueManager.getSMSC(iter.next());
			int currentCount = smsc.getSmppGateway().getRunningThreadsCount();

			if (currentCount >= tps) {
				circle = smsc.getCircle();
				result = true;
				break;
			}

		} // End Of Loop

		if (result) {
			Message nextSms = SmsQueue.peek(type);
			if (nextSms != null && circle != null) {
				String nextC = nextSms.getCircle();
				if (nextC.equals("")) {
					nextC = Pusher.defaultCircle;
				}

				if (circle.equalsIgnoreCase(nextC) == false) {
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" TPS Check:: Next SMS of Different Circle |  this=" + circle + "  |  next=" + nextC);
					result = false;
				} // End Of Circle Match
			}
		} // End Of Queue Peeking on TPS Achieved

		return result;

	}// End Of Method

	public void pause() {
		this.push = false;
		this.smsCount = -1L;
		Logger.sysLog(LogValues.debug, Pusher.class.getName(),
				" [" + this.id + "][" + this.type + "] Push Status: " + this.push);
	}// End Of Method

	public void begin(long smsCount) {

		try {

			this.smsCount = smsCount;
			Logger.sysLog(LogValues.debug, Pusher.class.getName(),
					" [" + this.id + "][" + this.type + "] Going to Notify... ");

			synchronized (this.lock) {
				this.push = true;
				this.lock.notify();
				Logger.sysLog(LogValues.debug, Pusher.class.getName(),
						" [" + this.id + "][" + this.type + "] Notified...");
			}

		} catch (IllegalMonitorStateException imse) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" [" + this.id + "][" + this.type + "]  Illegal State to Notify Queue Lock ");
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" [" + this.id + "][" + this.type + "]  Error Notifying Queue Lock :: " + e.getMessage());
		}

	}// End Of Method

	public void abort() {

		try {

			this.push = false;

			if (this.consumer != null)
				this.consumer.close();

			Logger.sysLog(LogValues.debug, Pusher.class.getName(),
					" [" + this.id + "][" + this.type + "] Stopping ActiveMQ Consumer ");
			Pusher.start = false;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" [" + this.id + "][" + this.type + "] \n" + Logger.getStack(e));
		}

	}// End Of Method

	public static void closeAllActiveSMSCConnections() {

		Set<String> circles = QueueManager.smscList.keySet();

		if (circles != null) {

			Iterator<String> iter = circles.iterator();
			String circle;
			SMSC smsc;

			while (iter.hasNext()) {

				circle = iter.next();
				smsc = QueueManager.getSMSC(circle);
				smsc.shutdown();

				circle = null;
				smsc = null;

			} // End Of While Loop

		} // End Of Circle Check

		QueueManager.smscList.clear();

	}// End Of Method

	synchronized public static void closeConnection() {

		try {

			Pusher.consumerSession.close();
			Logger.sysLog(LogValues.info, Pusher.class.getName(), " ActiveMQ Consumer Session Closed. ");
			Logger.sysLog(LogValues.info, Pusher.class.getName(), " Closing Connections & Gateways... ");
			Pusher.consumerConnection.close();

			ReConnector.killAll();
			Pusher.closeAllActiveSMSCConnections();

			if (ESME.getBindParams() != null) {
				ESME.getBindParams().clear();
			}

			if (CoreUtils.getDefaultHttpGateway() != null)
				CoreUtils.getDefaultHttpGateway().close();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, Pusher.class.getName(), Logger.getStack(e));
		}

	}// End of Method

}// End Of Class
