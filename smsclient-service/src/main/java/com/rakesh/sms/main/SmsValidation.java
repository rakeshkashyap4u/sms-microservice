package com.rakesh.sms.main;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.json.JSONException;
import org.json.JSONObject;

import com.rakesh.sms.beans.ConsentDetails;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.beans.Response;
import com.rakesh.sms.beans.SessionParameters;
import com.rakesh.sms.beans.USSDuser;
import com.rakesh.sms.bo.ReportsBo;
import com.rakesh.sms.cdr.ReceivedSmsBean;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.entity.DoubleConsent;
import com.rakesh.sms.entity.MessageActions;
import com.rakesh.sms.features.Feature;
import com.rakesh.sms.util.AppContext;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.webService.ServiceInfo;
import com.rakesh.sms.webService.SmsSubscription;

/**
 * 
 * ~~~Delivery Report~~~ Parameter: stat Value Details: DELIVRD: Message fully
 * delivered DELETED: If the message has been deleted before being sent UNDELIV:
 * The message cannot be delivered and the error is Permanent (No retry)
 * EXPIRED: The message has expired after a certain number of attempt (according
 * to the SMSC configuration) ENROUTE: Message ready to be sent. (This is
 * returned in response of an sm_query)
 * 
 */

public class SmsValidation implements Runnable {

	private static final String DeliverSmPattern = "^id:([a-zA-Z0-9]+) (sub:\\d{1,3} )?(dlvrd:\\d{1,3} )?submitDate:(\\d{10}) doneDate:(\\d{10}) stat:([A-Z0-9]+)( err:\\d{1,3})?( text:.*)?";
	public static final String NULL = new String("#");
	public static final String ANY = new String("*");

	/**
	 * Shortcode --to--> Msisdn --to--> UssdUser Mapping
	 */
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, USSDuser>> UssdSession;
	private static ConcurrentHashMap<String, Feature> features;
	private static ExecutorService execService;
	private static Pattern messagePattern;
	private static String callbackUrl;

	private ReportsBo reportsbo;
	private ReceivedSmsBean cdr;
	private String textMessage;

	public SmsValidation() {
		this.reportsbo = (ReportsBo) AppContext.getBean("reportsBo");
		this.textMessage = null;
		this.cdr = null;
	}// End of Constructor

	public static void init() {
		SmsValidation.init(100);
	}// End Of init-method

	public static void init(final int size) {
		SmsValidation.execService = Executors.newCachedThreadPool();
		SmsValidation.callbackUrl = CoreUtils.getProperty("callbackUrl");
		SmsValidation.messagePattern = Pattern.compile(DeliverSmPattern);
		SmsValidation.features = new ConcurrentHashMap<String, Feature>(5);
		SmsValidation.UssdSession = new ConcurrentHashMap<String, ConcurrentHashMap<String, USSDuser>>(size);
		Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
				" SMS Validator Initialized Version : Compatible with USSD ");
	}// End Of init-method

	public static void addFeature(String shortcode, Feature feature) {
		if (shortcode != null && feature != null)
			SmsValidation.features.put(shortcode.trim(), feature);
	}// End Of Method

	/*-----------------------------------------------------------------------------------------------------------------------------*/

	/** Starts SMPP based USSD session for a user, based on the short-code **/
	public static synchronized boolean startUssdSession(USSDuser session) {

		String log = "[" + session.getShortcode() + "][" + session.getMsisdn() + "]";

		try {
			if (UssdSession.containsKey(session.getShortcode()) == true) {
				UssdSession.get(session.getShortcode()).put(session.getMsisdn(), session);
				return true;
			} else {
				ConcurrentHashMap<String, USSDuser> sessionList = new ConcurrentHashMap<String, USSDuser>(500);
				sessionList.put(session.getMsisdn(), session);
				UssdSession.put(session.getShortcode(), sessionList);
				return true;
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsValidation.class.getName(),
					log + " Unable to START USSD Session :: " + e.getMessage());
			return false;
		} // End Of Try Catch

	}// End Of Method

	/** Stops SMPP based USSD session for a user, based on the short-code **/
	public static synchronized void stopUssdSession(USSDuser session) {
		String log = "[" + session.getShortcode() + "][" + session.getMsisdn() + "]";
		try {
			if (UssdSession.containsKey(session.getShortcode()) == true) {
				UssdSession.get(session.getShortcode()).remove(session.getMsisdn());
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsValidation.class.getName(),
					log + " Unable to STOP USSD Session :: " + e.getMessage());
		} // End Of Try Catch
	}// End Of Method

	/**
	 * Checks whether SMPP based USSD session exists for a user, based on a
	 * short-code
	 **/
	public static boolean isUssdSessionActive(String shortcode, String msisdn) {
		if (shortcode == null || msisdn == null)
			return false;

		if (UssdSession.containsKey(shortcode) && UssdSession.get(shortcode).containsKey(msisdn)) {
			return true;
		}
		return false;
	}// End Of Method

	/*-----------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Starts a Thread to Parse DeliverSm, if required, then validates it and
	 * creates its CDR
	 **/
	public void parseAndValidate(String textMessage, ReceivedSmsBean cdr) {
		// ayu comment

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				" Inside parse and Validate textMessage is  " + textMessage + cdr);

		try {

			this.textMessage = textMessage;
			this.cdr = cdr;

			if (this.textMessage == null)
				throw new NullPointerException(" Unable to Validate NULL Text SMS ");

			SmsValidation.execService.execute(this);

		} catch (RejectedExecutionException ree) {

			try {
				Logger.sysLog(LogValues.info, this.getClass().getName(), " Restarting Message Validator..!!! ");
				SmsValidation.stop();

				SmsValidation.execService = Executors.newCachedThreadPool();
				SmsValidation.execService.execute(this);

			} catch (Exception ie) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						ie.getMessage() + " Unable to read M0 or DR | Please Restart the tomcat ");
				cdr.setFailedStatus();
				cdr.setContent(textMessage);
				//CdrCreator.saveAsXML(cdr);
			} // End Of Inner Try Catch

		} catch (Exception e) {

			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));

//			cdr.setFailedStatus();
//			cdr.setContent(textMessage);
//			CdrCreator.saveAsXML(cdr);

		} // End Of Main Try Catch

	}// End Of Method

	/** Thread to validate DeliverSm **/
	public void run() {

		// ayu comment
		Logger.sysLog(LogValues.info, this.getClass().getName(), " inside run ");

		if (SMSController.validation != null) {

			/** Parse DeliverSm(this.textMessage) **/
			try {

				Response respSm = SmsValidation.parseRegEx(this.textMessage);

				// ayu comment
				Logger.sysLog(LogValues.info, this.getClass().getName(), " ResponseSM: " + respSm);

				if (respSm != null) {

					this.cdr.setTime(CoreUtils.getTimeStamp(respSm.getDoneDate()));
					this.cdr.setStatus(respSm.getStat());

					String messageId = SMPPMessageListener.getMessageId(respSm.getId().trim());
					this.cdr.setMessageId(messageId);

					if (respSm.getText() != null) {
						this.cdr.setContent(respSm.getText().trim());
					}

					if (this.cdr.isDeliveryReport()) {
						Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
								" DeliveryReport Received :: [" + this.cdr.getSender() + "] ::  MessageId=" + messageId
										+ " Status=" + this.cdr.getStatus());
					} else {
						Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
								" MO Received :: [" + this.cdr.getSender() + "][" + this.cdr.getReceiverMsisdn()
										+ "] ::  Message=" + messageId);
					}
					Logger.sysLog(LogValues.debug, SmsValidation.class.getName(),
							" Message Received Details :: " + respSm.toString());

				} else {
					Logger.sysLog(LogValues.debug, SmsValidation.class.getName(),
							" Unable to Parse DeliverSm | Try alternate method to parse... :: " + this.textMessage);
					throw new Exception(" Try alternate method to Parse DeliverSm...!! ");
				} // End Of IF ELSE

			} catch (Exception e) {
				/**
				 * If JSON of DeliverSm is not parsable, there are two possibilites -- Either it
				 * was already parsed by SMPPMessageListener -- OR its really Invalid
				 **/
				this.cdr.setContent(this.textMessage);
				if (this.cdr.getTime() != null)
					this.cdr.setTime(CoreUtils.getCurrentTimeStamp());
				Logger.sysLog(LogValues.debug, SmsValidation.class.getName(),
						" Response Received:: " + this.textMessage);
			} // End Of Try Catch

			/**
			 * Validate the Received MO from SMSC (NOT Delivery Report) and create CDR
			 */
			String smsText = this.cdr.getContent();

			boolean invalidUssdRequest = false;

			// if( smsText!=null && smsText.length()>0 &&
			// this.cdr.isDeliveryReport()==false ) {

			if (this.cdr.isDeliveryReport() == false) {
				// HttpGateway gateway = Pusher.getHttpGateway(Pusher
				// .getDefaultCircle());
				// ayu comment
				// Logger.sysLog(LogValues.info, this.getClass().getName()," Recieving gateway
				// now " + gateway);

				Message sms = CoreUtils.createMTfromMOcdr(cdr);
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" HERE SMS NOE " + sms.toString());

			//	HttpGateway gateway = Pusher.getHttpGateway(sms.getCircle());
				HttpGateway gateway = Pusher.getHttpGateway("PRO");
				

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" Recieving gateway now " + gateway + ", circle: " + sms.getCircle());

				if (gateway == null) {
					gateway = Pusher.getHttpGateway(Pusher.getDefaultCircle());
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" Recieving gateway now1 " + gateway + ", circle: " + Pusher.getDefaultCircle());
				}

				if (gateway == null) {
					gateway = Pusher.getRemovedHttpGateway(sms.getCircle());
				}
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" Testing Removed gateway" + gateway + sms.getCircle());

				// ayu comment
				Logger.sysLog(LogValues.info, this.getClass().getName(), " Creating Mt now" + sms);

				boolean isUSSDresponse = false;

				/**
				 * Considering MO as USSD response first. IF true, Does not takes any MO Action
				 */
				if (SmsValidation.UssdSession.isEmpty() == false
						&& SmsValidation.isUssdSessionActive(sms.getCli(), sms.getMsisdn())) {
					/*
					 * isEmpty check will make it faster, when USSD will not be used
					 */
					// ayu comment
					Logger.sysLog(LogValues.debug, this.getClass().getName(), " Inside ussd session if block ");

					USSDuser user = UssdSession.get(sms.getCli()).get(sms.getMsisdn());
					Date expiry = new Date(user.getExpiry());

					String sublog = "[" + sms.getCli() + "][" + sms.getMsisdn() + "]";

					if (expiry.after(new Date()) == true) {
						/* After ==> NOT Expired */
						isUSSDresponse = true;

						if (user.getTotalOptions() > 0) {

							int oid = user.isValidOption(sms.getMessage());
							boolean specialOpt = false;

							if (user.getSpecialOn() != null && user.getSpecialUrl() != null) {
								if (user.getTotalOptions() == oid
										&& user.getSpecialOn().trim().equalsIgnoreCase("last")) {
									specialOpt = true;
									for (int i = 1; i < user.getTotalOptions(); i++) {
										String specialUrl = user.getSpecialUrl().trim();
										specialUrl = user.replaceAction(specialUrl);
										specialUrl = specialUrl.replaceAll("\\$service\\$", user.getServiceId(i));
										gateway.sendGETRequest(specialUrl, sms);
									} // End Of Loop
								} // End On Special Action Check
							} // End Of Null Check

							if (specialOpt == false && oid >= 0 && user.getSuccessUrl() != null) {
								String successUrl = user.getSuccessUrl().trim();
								successUrl = successUrl.replaceAll("\\$service\\$", user.getServiceId(oid));
								gateway.sendGETRequest(successUrl, sms);
							} else if (oid == -1 && user.getFailureUrl() != null) {
								Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
										" Invalid USSD Response received :: " + sms.getMessage());
								gateway.sendGETRequest(user.getFailureUrl().trim(), sms);
							} else if (specialOpt == false) {
								Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
										" USSD Response received | USSD Action URL not defined ");
							}
							/* Session Complete. Remove Session */
							SmsValidation.stopUssdSession(user);
						} else if (user.getType().equalsIgnoreCase("otp") && user.getExtraInfo() != null) {

							String txnId = user.getExtraInfo();

							if (sms.getMessage().length() == 6 && Pattern.matches("^[0-9]{6}", sms.getMessage())) {
								Logger.sysLog(LogValues.info, this.getClass().getName(),
										sublog + " OTP received from user : " + sms.getMessage());
								String successUrl = user.getSuccessUrl().trim();

								if (successUrl != null && successUrl.length() > 0) {
									successUrl = successUrl.replaceAll("\\$txnId\\$", txnId);
									gateway.sendGETRequest(successUrl, sms);
								} else
									Logger.sysLog(LogValues.info, this.getClass().getName(), "No success url defined!");
							} else {
								String failureUrl = user.getFailureUrl().trim();

								if (failureUrl != null && failureUrl.length() > 0) {
									gateway.sendGETRequest(failureUrl, sms);
								} else
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											sublog + " Invalid OTP received. No failure url defined. No action taken.");
							}

							SmsValidation.stopUssdSession(user);
						} else {
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"No options defined for the USSD session.");
						}
					} else {
						Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
								" USSD Response received | Session Already Expired ");
						/* Session Expired. Remove Session */
						SmsValidation.stopUssdSession(user);
					} // End Of Session Check

				} // End Of USSD Check

				/** Not a USSD response... Take the required MO Action */
				if (isUSSDresponse == false) {
					Logger.sysLog(LogValues.info, this.getClass().getName(), " USSD Response is : " + isUSSDresponse);

					String shortcode = this.cdr.getReceiverMsisdn();

					// String shortcode = "1089";

					// System.out.println("shortcode"+shortcode);
					/**
					 * In case of Copy2App need to forward all Chat SMS to @Home App, independent of
					 * CLI/aParty. Hence overridden the CLI and sent to SMS validation for further
					 * action
					 */
//					if (CdrCreator.isJsonCDR()) 
//					{
//						shortcode = "athome";
//					}

					int result = SMSController.validation.validate(shortcode, smsText, sms.getMsisdn());

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" result after validating shortcode and text " + result);

					boolean whitelisted = true;

					// boolean whitelisted = CoreUtils.isWhitelisted(shortcode,
					// sms.getMsisdn(), "MO");

					/** Take MO based Actions */
					if (result < 0) {
						Logger.sysLog(LogValues.info, SmsValidation.class.getName(), " MO Validation Result = FALSE ");

						result = CoreUtils.getMoFailureNotify(shortcode);

						if (result < 0)
							result = CoreUtils.getScFailureNotify();

						// System.out.println(result);

						if (result > -1 && whitelisted) {
							List<MessageActions> actions = SMSController.validation.getActionsforMO(result);

							Logger.sysLog(LogValues.info, this.getClass().getName(),
									" actions size: " + actions.size());

							if (actions.size() > 0) {
								/**
								 * Assuming only one Action and GET HTTP Request
								 **/
								MessageActions action = actions.get(0);
								gateway.sendGETRequest(action.getDetails().trim(), sms);
								Logger.sysLog(LogValues.info, this.getClass().getName(), " Action Received: " + action);

							} else {
								Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
										" NO Action defined for Invalid MO [WRONG_KEY] ");
							} // End Of Size Check

						} else {
							Logger.sysLog(LogValues.info, SmsValidation.class.getName(), " NO Action taken ");
						} // End Of Check

					} else if (whitelisted) {
						/** Take the Action required, defined in table **/

						Logger.sysLog(LogValues.info, SmsValidation.class.getName(), " In else if");

						List<MessageActions> actions = SMSController.validation.getActionsforMO(result);

						Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
								"Action " + SMSController.validation.getActionsforMO(result));

						for (int i = 0; i < actions.size(); i++) {

							final String comma = String.valueOf(',');
							MessageActions action = actions.get(i);
							String detail = action.getDetails();
							String type = action.getType();

							System.out.println("inside for loop type " + type);

							System.out.println(type);

							System.out.println(detail);
							
							

							if (type != null && detail != null) {
								
								

								if (type.equalsIgnoreCase("GET")) {
									System.out.println("inside for loop get case");
									gateway.sendGETRequest(detail.trim(), sms);
								} else if (type.equalsIgnoreCase("POST")) {
									gateway.sendPOSTRequest(detail.trim(), sms, null);
								} else if (type.equalsIgnoreCase("SMS")) {
									gateway.sendGETRequest(detail.trim(), sms);
								} else if (type.equalsIgnoreCase("Copy2App")) {

									String msisdn = sms.getMsisdn();
									String cli = sms.getCli();

									sms.setMsisdn(CoreUtils.stripCodes(msisdn));
									sms.setCli(CoreUtils.stripCodes(cli));

									if (sms.getMsisdn() != null && sms.getCli() != null) {
										gateway.sendPOSTRequest(detail.trim(), sms, null);
									} else {
										Logger.sysLog(LogValues.error, SmsValidation.class.getName(),
												" Invalid Sender/Receiver :: " + cli + "/" + msisdn
														+ " --- NO Action Taken ");
									}

									
									
								} else if (type.equalsIgnoreCase("SubEndDate")) {

									String resp = gateway.sendSyncGETRequest(detail.trim(), sms);

									if (resp != null && resp.length() > 0) {

										String datetime = null;
										int ind = resp.indexOf(comma);

										if (ind > 0) {
											datetime = resp.substring(0, ind).trim();
										} else {
											datetime = resp.trim();
										} // End Of If Check

										datetime = CoreUtils.convertToStandardDateFormat(datetime).replaceAll(" ",
												"%20");
										if (datetime.trim().equals(comma) == false) {
											sms.setSubEndDate(datetime);
										} else {
											sms.setSubEndDate("-");
										}

									} else {
										Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
												" Response Received for Subcription End Date Request =" + resp);
										sms.setSubEndDate("-");
									} // End Of Response Check

									Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
											" Subcription End Date =" + sms.getSubEndDate());

								} else if (type.equalsIgnoreCase("SubEndDateSuccess")) {

									if (sms.getSubEndDate() != null && sms.getSubEndDate().length() > 0
											&& sms.getSubEndDate().equals("-") == false) {
										gateway.sendGETRequest(detail.trim(), sms);
									}

								} else if (type.equalsIgnoreCase("SubEndDateFailure")) {

									if (sms.getSubEndDate() == null || sms.getSubEndDate().length() == 0
											|| sms.getSubEndDate().equals("-") == true) {
										gateway.sendGETRequest(detail.trim(), sms);
									}

								} else if (type.equalsIgnoreCase("Feature")) {

									if (SmsValidation.features.containsKey(shortcode)) {
										Feature feature = SmsValidation.features.get(shortcode);
										feature.sendReply(sms);
									} else {
										Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
												" No Feature with shortcode " + shortcode + " exists ");
									} // End Of Check

								} else if (type.equalsIgnoreCase("USSD")) {

									ConsentDetails consentInfo = SmsValidation.parseConsent(detail.trim());

									USSDuser session = new USSDuser();
									session.setMsisdn(sms.getMsisdn());
									session.setMessage(sms.getMessage());
									session.setShortcode(sms.getCli());

									if (cdr.getParameters() != null) {

										try {
											OptionalParameter[] parameters = cdr.getParameters();

											SessionParameters sessParameters = new SessionParameters();

											for (int x = 0; x < parameters.length; x++) {

												byte[] param = parameters[x].serialize();

												int tag = CoreUtils.getInteger(param, 0, 2);
												int length = CoreUtils.getInteger(param, 2, 2);
												int value = CoreUtils.getInteger(param, 4, 1);

												Logger.sysLog(LogValues.info, this.getClass().getName(),
														"tag:" + tag + " | length :" + length + " | value:" + value);

												if (tag == Tag.ITS_SESSION_INFO.code()) {
													session.setSessionId(CoreUtils.getInteger(param, 4, 2));
													int mask;
													mask = (1 << 1) - 1;
													int sessionInactive = CoreUtils.getInteger(param, 4, 2) & mask;

													if (sessionInactive == 1)
														session.setType("end");
													else
														session.setType("pull");

													sessParameters.setItsSessionInfo(session.getSessionId());

													Logger.sysLog(LogValues.debug, this.getClass().getName(),
															"Got optional Parameter: ITS_SESSION_INFO: session id:"
																	+ session.getSessionId());
												} else if (tag == Tag.USSD_SERVICE_OP.code()) {
													session.setServiceOp(value);
													Logger.sysLog(LogValues.debug, this.getClass().getName(),
															"Got optional Parameter: USSD_SERVICE_OP: value:"
																	+ session.getServiceOp());
												}
											}

											sessParameters.setUssdServiceOp(2);

											sms.setOptionalSessParams(sessParameters);
										} catch (Exception e) {
											Logger.sysLog(LogValues.error, this.getClass().getName(),
													"Error in getting session optional parameters :"
															+ Logger.getStack(e));
										}
									} else {
										Logger.sysLog(LogValues.info, this.getClass().getName(),
												"Optional parameters null");

										result = CoreUtils.getMoFailureNotify(shortcode);

										if (result > -1 && whitelisted) {
											List<MessageActions> messageaction = SMSController.validation
													.getActionsforMO(result);

											if (actions.size() > 0) {
												/**
												 * Assuming only one Action and GET HTTP Request
												 **/
												MessageActions act = messageaction.get(0);
												gateway.sendGETRequest(act.getDetails().trim(), sms);
											} else {
												Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
														" NO Action defined for Invalid MO [WRONG_KEY] ");
											} // End Of Size Check

										} else {
											Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
													" NO Action taken ");
										} // End Of Check

										consentInfo = null;
										invalidUssdRequest = true;
									}

									if (consentInfo != null) {

										session.addSecondsToExpiry(consentInfo.getTimeoutInSec().longValue());
										String responseType = consentInfo.getResponseType();

										if (responseType != null && responseType.equalsIgnoreCase("Response")) {

											String respUrl = "";

											try {

												String url = consentInfo.getUrl()
														.replace("$sessionid$",
																new Integer(session.getSessionId()).toString())
														.replace("$serviceop$",
																new Integer(session.getServiceOp()).toString())
														.replace("$type$",
																URLEncoder.encode(session.getType(), "UTF-8"));
												Logger.sysLog(LogValues.info, this.getClass().getName(),
														"ayu Url:" + url + " --- gateway: " + gateway
																+ "default circle: " + Pusher.getDefaultCircle());
												gateway = Pusher.getHttpGateway(sms.getCircle());
												if (gateway == null) {
													gateway = Pusher.getRemovedHttpGateway(sms.getCircle());
												}
												Logger.sysLog(LogValues.info, this.getClass().getName(),
														"ayu testing gateway" + gateway + sms.getCircle());

												Logger.sysLog(LogValues.info, this.getClass().getName(),
														"ayu1 Url:" + url + " --- gateway: " + gateway
																+ "default circle: " + sms.getCircle());

												String response = gateway.sendSyncGETRequest(url, sms);

												if (consentInfo.getSmsResponseUrl() != null) {
													respUrl = consentInfo.getSmsResponseUrl();

													if (response != null && response.length() > 0) {
														try {

															String[] respArray = response.split("&");

															if (respArray[0].substring(respArray[0].indexOf("="))
																	.equalsIgnoreCase("ok"))
																respArray[0] = "";
															else if (respArray[0].substring(respArray[0].indexOf("="))
																	.equalsIgnoreCase("exit"))
																respArray[0] = " ";

															respUrl = respUrl
																	.replace("$response$",
																			URLEncoder.encode(respArray[0].substring(
																					respArray[0].indexOf("=") + 1),
																					"UTF-8"))
																	.replace("$sessionid$",
																			URLEncoder.encode(respArray[1].substring(
																					respArray[1].indexOf("=") + 1),
																					"UTF-8"));
															respUrl = respUrl.replace("$sessionend$",
																	URLEncoder.encode(
																			respArray[3].substring(
																					respArray[3].indexOf("=") + 1),
																			"UTF-8"));

														} catch (Exception e) {
															Logger.sysLog(LogValues.error, this.getClass().getName(),
																	"Error in url encoding response!");
														}

													} else
														Logger.sysLog(LogValues.info, this.getClass().getName(),
																"Response received null!");
												}
											} catch (Exception e) {
												Logger.sysLog(LogValues.error, this.getClass().getName(),
														"Error in encoding type :" + Logger.getStack(e));
											}

											gateway.sendGETRequest(respUrl, sms);

										} else {
											Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
													" ResponseType :: " + responseType + " not yet defined for USSD ");
										} // End Of Response Type Check

									} else {

										if (!invalidUssdRequest)
											Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
													" Invalid JSON Action defined for USSD!");
									} // End Of If Else

								} else if (type.equalsIgnoreCase("USSD")) {

									ConsentDetails consentInfo = SmsValidation.parseConsent(detail.trim());

									USSDuser session = new USSDuser();
									session.setMsisdn(sms.getMsisdn());
									session.setMessage(sms.getMessage());
									session.setShortcode(sms.getCli());

									if (consentInfo != null) {

										session.addSecondsToExpiry(consentInfo.getTimeoutInSec().longValue());
										String responseType = consentInfo.getResponseType();

										if (responseType != null && responseType.equalsIgnoreCase("ServiceList")) {

											String serviceList = gateway.sendSyncGETRequest(consentInfo.getUrl(), sms);
											StringBuilder options = new StringBuilder("");

											if (serviceList != null) {
												String[] map = serviceList.split(",");
												for (int k = 0; k < map.length; k++) {
													String[] temp = (map[k] != null) ? map[k].split("=") : null;

													if (temp != null && temp.length > 1) {
														String dtmf = String.valueOf(k + 1);
														String sName = temp[0].trim();
														session.addOption(dtmf, sName, temp[1].trim());
														options.append(dtmf);
														options.append(". " + sName + " ");
													} // End Of IF
												} // End of Loop(k)
											} // End Of Service List Check

											if (session.getTotalOptions() == 1
													&& consentInfo.getSuccessActionUrl() != null) {
												String successUrl = consentInfo.getSuccessActionUrl().trim();
												successUrl = consentInfo.replaceAction(successUrl);
												successUrl = successUrl.replaceAll("\\$service\\$",
														session.getServiceId());
												gateway.sendGETRequest(successUrl, sms);
											} else if (session.getTotalOptions() > 1
													&& consentInfo.getSmsResponseUrl() != null) {
												String optionListMsg = null;
												String dtmf = String.valueOf(session.getTotalOptions() + 1);
												session.addOption(dtmf, "All", "All");
												options.append(dtmf);
												options.append(". All ");

												try {
													optionListMsg = URLEncoder.encode(options.toString(), "UTF-8");
												} catch (Exception e) {
													optionListMsg = options.toString();
												}
												String respUrl = consentInfo.getSmsResponseUrl()
														.replace("$servicelist$", optionListMsg);
												session.setSuccessUrl(consentInfo.getSuccessActionUrl());
												session.setFailureUrl(consentInfo.getFailureActionUrl());
												session.setSpecialUrl(consentInfo.getSpecialActionUrl());
												session.setSpecialOn(consentInfo.getSpecialActionOn());
												session.setAction(consentInfo.getAction());
												SmsValidation.startUssdSession(session);
												gateway.sendGETRequest(respUrl, sms);
											} else {
												Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
														" USSD SMS Response URL Not Defined | No Response sent to User ");
											}
										} else if (responseType != null
												&& responseType.equalsIgnoreCase("RevenueInfo")) {
											String revenueInfo = gateway.sendSyncGETRequest(consentInfo.getUrl(), sms);
											// StringBuilder options = new
											// StringBuilder("");

											HashMap<String, String> info = new HashMap<String, String>();

											if (revenueInfo != null) {
												String[] map = revenueInfo.split(",");
												for (int k = 0; k < map.length; k++) {
													String[] temp = (map[k] != null) ? map[k].split("=") : null;

													if (temp != null && temp.length > 1) {
														String key = temp[0].trim().toLowerCase().replace(" ", "");
														info.put(key, temp[1].trim());
													} // End Of IF
												} // End of Loop(k)
											} // End Of Revenue Info Check

											String respUrl = "";
											if (consentInfo.getSmsResponseUrl() != null) {
												respUrl = consentInfo.getSmsResponseUrl();
											}

											if (info != null && info.size() > 0) {
												try {
													for (String prop : info.keySet()) {
														respUrl = respUrl.replace("$" + prop + "$",
																URLEncoder.encode(info.get(prop), "UTF-8"));
													}
												} catch (Exception e) {
													Logger.sysLog(LogValues.error, this.getClass().getName(),
															"Error in encoding revenue info!");
												}
											}

											gateway.sendGETRequest(respUrl, sms);
										} else if (responseType != null
												&& responseType.equalsIgnoreCase("UserStatus")) {

											String status = "";
											String respUrl = "";

											String statusJson = gateway.sendSyncGETRequest(consentInfo.getUrl(), sms);

											try {
												JSONObject obj = new JSONObject(statusJson);
												status = (String) obj.get("currentStatus");

												if (status.equalsIgnoreCase("pending")
														|| status.equalsIgnoreCase("parking")
														|| status.equalsIgnoreCase("suspended")) {
													status = "subscribed but not active";
												} else if (status.equalsIgnoreCase("new")) {
													status = "not subscribed";
												} else if (status.equalsIgnoreCase("active")
														|| status.equalsIgnoreCase("demo"))
													status = "active";

											} catch (JSONException e) {

												Logger.sysLog(LogValues.error, this.getClass().getName(),
														"Error parsing JSON:" + Logger.getStack(e));
											}

											if (consentInfo.getSmsResponseUrl() != null) {
												respUrl = consentInfo.getSmsResponseUrl();

												if (status != null && status.length() > 0) {
													try {
														respUrl = respUrl.replace("$status$",
																URLEncoder.encode(status, "UTF-8"));
													} catch (Exception e) {
														Logger.sysLog(LogValues.error, this.getClass().getName(),
																"Error in encoding status!");
													}
												} else
													Logger.sysLog(LogValues.info, this.getClass().getName(),
															"User status received null!");
											}

											gateway.sendGETRequest(respUrl, sms);

										} else if (responseType != null
												&& responseType.equalsIgnoreCase("WSSubResponse")) {

											if (consentInfo.getUrl() != null) {

												String response = gateway.sendSyncGETRequest(consentInfo.getUrl(), sms);
												String status = "unsuccessful";

												try {
													JSONObject obj = new JSONObject(response);
													status = (String) obj.get("status");
												} catch (Exception e) {
													Logger.sysLog(LogValues.error, this.getClass().getName(),
															"Unable to parse JSON: " + Logger.getStack(e));
												}

												if (status.equalsIgnoreCase("successful")) {
													SmsSubscription.response = true;
													Logger.sysLog(LogValues.info, this.getClass().getName(),
															"Setting response to true!");
												} else if (status.equalsIgnoreCase("unsuccessful")) {
													SmsSubscription.response = false;
													Logger.sysLog(LogValues.info, this.getClass().getName(),
															"Setting response to false!");
												} else {
													SmsSubscription.response = false;
													Logger.sysLog(LogValues.info, this.getClass().getName(),
															"Setting response to false!");
												}

												synchronized (this) {
													this.notify();
												}

											}

										} else if (responseType.equalsIgnoreCase("WSUserInfo")) {

											if (consentInfo.getUrl() != null) {
												String response = gateway.sendSyncGETRequest(consentInfo.getUrl(), sms);

												if (response != null && !response.equals("")) {

													String service[] = response.split(",");

													if (consentInfo.getSmsResponseUrl() != null) {
														for (int j = 0; service != null && j < service.length; j++) {

															Logger.sysLog(LogValues.info, this.getClass().getName(),
																	"Checking info for service:" + service[j]
																			.substring(0, service[j].indexOf("=")));

															String checkSubUrl = consentInfo.getSmsResponseUrl()
																	.replaceAll("\\$serviceid\\$", service[j]
																			.substring(0, service[j].indexOf("=")));

															String userInfo = gateway.sendSyncGETRequest(checkSubUrl,
																	sms);

															List<ServiceInfo> services = new ArrayList<ServiceInfo>();

															try {
																JSONObject obj = new JSONObject(userInfo);

																ServiceInfo serviceInfo = new ServiceInfo();
																serviceInfo.setActivationDate(
																		(String) obj.get("subStartDate"));
																serviceInfo.setServiceName(
																		(String) obj.get("subServiceId"));
																serviceInfo.setShortcode(sms.getCli());
																serviceInfo.setUnsubKeyword(
																		"unsub" + (String) obj.get("serviceName"));

																services.add(serviceInfo);

															} catch (Exception e) {
																Logger.sysLog(LogValues.error,
																		this.getClass().getName(),
																		"Unable to parse JSON: " + Logger.getStack(e));
															}

															SmsSubscription.services = services;
														}
													}
												} else {
													Logger.sysLog(LogValues.info, this.getClass().getName(),
															"Empty response received!");
													SmsSubscription.services = new ArrayList<ServiceInfo>();
												}

												synchronized (this) {
													this.notify();
												}
											}

										} else if (responseType.equalsIgnoreCase("OTP")) {

											if (consentInfo.getUrl() != null) {

												session.addSecondsToExpiry(consentInfo.getTimeoutInSec().longValue());
												String response = gateway.sendSyncGETRequest(consentInfo.getUrl(), sms);

												if (response != null && !response.equals("")) {

													String currentStatus = null;

													try {
														JSONObject obj = new JSONObject(response);
														currentStatus = (String) obj.get("currentStatus");

														Logger.sysLog(LogValues.info, this.getClass().getName(),
																"Current status of the user : " + sms.getMsisdn()
																		+ " : " + currentStatus);

														if (currentStatus.equalsIgnoreCase("new")) {

															if (consentInfo.getSpecialActionUrl() != null
																	&& consentInfo.getSpecialActionUrl().length() > 0) {
																String otpJson = gateway.sendSyncGETRequest(
																		consentInfo.getSpecialActionUrl().trim(), sms);

																try {

																	JSONObject otp = new JSONObject(otpJson);
																	session.setExtraInfo((String) otp.get("otptoken"));

																} catch (Exception e) {
																	Logger.sysLog(LogValues.error,
																			this.getClass().getName(),
																			"Error while parsing subscription response JSON. Setting otptoken to empty string.");
																	session.setExtraInfo("");
																}

															}

															session.setType("otp");
															session.setSuccessUrl(
																	consentInfo.getSuccessActionUrl().trim());
															session.setFailureUrl(consentInfo.getFailureActionUrl());
															session.setAction(consentInfo.getAction());
															SmsValidation.startUssdSession(session);

														} else {

															if (consentInfo.getSmsResponseUrl() != null
																	&& consentInfo.getSmsResponseUrl().length() > 0)
																gateway.sendGETRequest(
																		consentInfo.getSmsResponseUrl().trim(), sms);

														}

													} catch (Exception e) {
														Logger.sysLog(LogValues.error, this.getClass().getName(),
																"Error while parsing check subscription response :"
																		+ response);
													}

													/*
													 * try {
													 * 
													 * JSONObject obj = new JSONObject(response); session
													 * .setExtraInfo((String )obj.get("otptoken"));
													 * 
													 * }catch(Exception e) { Logger .sysLog(LogValues.error, this
													 * .getClass().getName(),
													 * "Error while parsing subscription response JSON. Setting otptoken to empty string."
													 * ); session.setExtraInfo(""); }
													 * 
													 * session.setType("otp");
													 * 
													 * if(consentInfo. getSmsResponseUrl()!=null &&
													 * consentInfo.getSmsResponseUrl ().length() > 0)
													 * gateway.sendGETRequest (consentInfo .getSmsResponseUrl ().trim(),
													 * sms);
													 * 
													 * session.setSuccessUrl( consentInfo .getSuccessActionUrl());
													 * session .setFailureUrl(consentInfo .getFailureActionUrl());
													 * session .setSpecialUrl(consentInfo .getSpecialActionUrl());
													 * session .setSpecialOn(consentInfo .getSpecialActionOn()); session
													 * .setAction(consentInfo .getAction()); SmsValidation
													 * .startUssdSession (session);
													 */

												} else {
													Logger.sysLog(LogValues.info, this.getClass().getName(),
															"Empty response received!");
												}
											}

										} else {
											Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
													" ResponseType :: " + responseType + " not yet defined for USSD ");
										} // End Of Response Type Check

									} else {

										if (!invalidUssdRequest)
											Logger.sysLog(LogValues.warn, SmsValidation.class.getName(),
													" Invalid JSON Action defined for USSD!");
									} // End Of If Else

								} else if (type.equalsIgnoreCase("FC")) {

									gateway.sendGETRequest(detail.trim(), sms);

									DoubleConsent consent = new DoubleConsent();
									consent.setMsisdn(sms.getMsisdn());
									consent.setMessage(sms.getMessage());
									consent.setShortcode(sms.getCli());
									consent.setMoId(result);
									Logger.sysLog(LogValues.info, this.getClass().getName(),
											"  [" + sms.getMsisdn() + "] fetching consent message ["
													+ consent.getMessage() + "]fetching msisdn [" + consent.getMsisdn()
													+ "] ayu ");

									SMSController.validation.saveFirstConsent(consent);

								} else if (type.equalsIgnoreCase("VGET")) // only for configured shortcode
																			// bngEligibleAPI will be hitted (done for
																			// africell drc)
								{
									Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
											"we gete shortcode " + sms.getCli());

									String eligibleShortcode = CoreUtils.getProperty("eligibleShortcode");
									String[] eligibleShortcodes = eligibleShortcode.split(",");

									String thirdPartyAPI = CoreUtils.getProperty("bngEligibleAPI");
									if (thirdPartyAPI != null && thirdPartyAPI.length() > 0) {

										Logger.sysLog(LogValues.info, SmsValidation.class.getName(), " [ "
												+ sms.getMsisdn() + " ] Hitting bng eligible API " + thirdPartyAPI);

										String response;

										System.out.println("sms.getCli())" + sms.getCli());

										System.out.println("eligibleShortcode.contains(sms.getCli())"
												+ eligibleShortcode.contains(sms.getCli()));

										if (!eligibleShortcode.contains(sms.getCli())) // only for selected shortcode we
																						// will hit checkBngEligible
										{

											System.out.println("eligibleShortcode contains " + sms.getCli());
											response = "0";
										}

										else {
											System.out.println("hitting checkBngEligible");

											response = gateway.checkBngEligible(thirdPartyAPI, sms.getMsisdn(), "POST");

											System.out.println(" response of checkBngEligible " + response);

										}

										System.out.println("response " + response);

										if (response.equals("0")) {
											gateway.sendGETRequest(detail.trim(), sms);
											System.out.println("gateway" + gateway.toString());
										} else {
											String failureAPI = CoreUtils.getProperty("bngEligibleFailure");

											if (failureAPI != null && failureAPI.length() > 0) {
												gateway.sendGETRequest(failureAPI.trim(), sms);
											}
										}
									}

									else {
										Logger.sysLog(LogValues.info, SmsValidation.class.getName(),
												"short code not eligble");
									}

								} else if (type.toUpperCase().startsWith("SC")) {

									String consentId = type.substring(3);

									Logger.sysLog(LogValues.info, this.getClass().getName(),
											" Inside else if of SC ayu");

									if (consentId == null || consentId.length() == 0)
										Logger.sysLog(LogValues.warn, this.getClass().getName(),
												"  [" + sms.getMsisdn() + "]  Wrong definition for Second Consent ");

									DoubleConsent consent = SMSController.validation.getFirstConsent(sms.getMsisdn(),
											consentId);

									Date now = Calendar.getInstance().getTime();

									if (consent != null && consentId.trim().equals(consent.getMoId().toString())) {

										if (consent.getExpiresAt().before(now) == false) {
											Logger.sysLog(LogValues.info, this.getClass().getName(),
													" [" + sms.getMsisdn() + "]  Second Consent Received :: "
															+ consent.toString());
											gateway.sendGETRequest(detail.trim(), sms);
											SMSController.validation.removeConsent(sms.getMsisdn(), consentId);

										} else {
											Logger.sysLog(LogValues.warn, this.getClass().getName(), " ["
													+ sms.getMsisdn()
													+ "]  Second Consent Received after First Consent has Expired :: "
													+ consent.toString());
											SMSController.validation.removeConsent(sms.getMsisdn(), consentId);
										} // End Of Consent Check

									} else {
										Logger.sysLog(LogValues.warn, this.getClass().getName(), "  [" + sms.getMsisdn()
												+ "]  Second Consent Received without First Consent ");
									}

								} else
									Logger.sysLog(LogValues.error, SmsValidation.class.getName(),
											" Invalid type defined: " + type);
							} else
								Logger.sysLog(LogValues.error, SmsValidation.class.getName(),
										" NULL Values :: All Action Details not specified :: " + action.toString());

						} // End Of Loop
					} // End Of MO Actions

				} // End Of USSD-MO Check

				//CdrCreator.saveAsXML(cdr);

			} else if (this.cdr.isDeliveryReport() == false) {
				/** Blank MO */
				Logger.sysLog(LogValues.info, this.getClass().getName(), " MO without message | NO Action taken ");
			} // End Of MO Check

			/**
			 * If Not MO, then Send Callback for Delivery Report (if required) and create
			 * CDR
			 */
			if (this.cdr.isDeliveryReport() && this.cdr.getMessageId() != null
					&& !this.cdr.getMessageId().equals("null")) {

				Message sms = reportsbo.getMessageLog(cdr.getMessageId());

				if (sms != null && sms.isCallback()) {

					if (cdr.getStatus() != null && (cdr.getStatus().equalsIgnoreCase("DELIVRD")
							|| cdr.getStatus().equalsIgnoreCase("DELIVERED"))) {
						sms.getCallbackDetails().setCallbackStatus("Success");
						sms.getCallbackDetails().setFailureReason("NA");
					} else {
						sms.getCallbackDetails().setCallbackStatus("Failure");
						sms.getCallbackDetails().setFailureReason(cdr.getStatus());
					}

//					if (CdrCreator.isJsonCDR()) {
//						/** For ATHOME */
//
//						String callbackstatus = sms.getCallbackDetails().getCallbackStatus();
//						String deliverystatus = this.cdr.getStatus();
//
//						sms.getCallbackDetails().setCallbackStatus(deliverystatus);
//						this.cdr.setReason(deliverystatus);
//
//						if (callbackstatus != null && callbackstatus.equalsIgnoreCase("Success")) {
//							this.cdr.setStatus("Success");
//						} else {
//							this.cdr.setStatus("Failure");
//						}
//
//					} // End Of JSON Check

					if (SmsValidation.callbackUrl != null) {
						HttpGateway gateway = Pusher.getHttpGateway(sms.getCircle());
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"Sending callback..." + sms.toString());
						if (gateway != null) {
							gateway.sendGETRequest(SmsValidation.callbackUrl.trim(), sms);
						} else {
							CoreUtils.getDefaultHttpGateway().sendGETRequest(SmsValidation.callbackUrl.trim(), sms);
						}
					} else
						Logger.sysLog(LogValues.error, this.getClass().getName(),
								" Subscription CALLBACK URL Not Defined in global.sms_properties ");

				} // End Of Callback Check

			//	CdrCreator.saveAsXML(cdr);

			} // End Of Delivery Report Check

		} else {
			Logger.sysLog(LogValues.error, SmsValidation.class.getName(),
					" Validation Object NULL | Code Initialization Error ");
		}

		this.textMessage = null;
		this.cdr = null;

	}// End Of Thread

	public static Response parseRegEx(String text) throws Exception {

		// text =
		// "id:Smsc2006 sub:1 dlvrd:1 submit date:1505051216 done date:1505051216 stat:0
		// err:0 text:Hi Hleodfb";
		text = text.replace("submit date", "submitDate").replace("done date", "doneDate");
		Response smscResponse;

		try {

			Matcher matcher = messagePattern.matcher(text);
			String temp = null;
			int group = 1;

			if (matcher.matches()) {

				smscResponse = new Response();

				String messageId = SMPPMessageListener.getMessageId(matcher.group(group++).trim());
				smscResponse.setId(messageId);

				temp = matcher.group(group++);

				if (temp != null && temp.startsWith("sub")) {
					smscResponse.setSub(Integer.parseInt(temp.trim().substring(temp.indexOf(':') + 1)));
					temp = null;
				}

				if (temp == null)
					temp = matcher.group(group++);
				if (temp != null && temp.startsWith("dlvrd")) {
					smscResponse.setDlvrd(Integer.parseInt(temp.trim().substring(temp.indexOf(':') + 1)));
					temp = null;
				}

				if (temp == null)
					temp = matcher.group(group++);

				smscResponse.setSubmitDate(Long.parseLong(temp.trim()));
				smscResponse.setDoneDate(Long.parseLong(matcher.group(group++).trim()));
				smscResponse.setStat(matcher.group(group++).trim());

				temp = matcher.group(group++);
				if (temp != null && temp.trim().startsWith("err")) {
					smscResponse.setErr(Integer.parseInt(temp.trim().substring(temp.indexOf(':'))));
					temp = null;
				}

				if (temp == null)
					temp = matcher.group(group++);

				if (temp != null && temp.trim().startsWith("text")) {
					String smstext = SMPPMessageListener.getTrimmedMessage(temp.trim().substring(temp.indexOf(':')));
					smscResponse.setText(smstext);
				}

			} else
				smscResponse = null;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsValidation.class.getName(),
					" Parsing Error | Unexpected SMSC Response Received \n " + text + " \n" + Logger.getStack(e));
			smscResponse = null;
		}

		return smscResponse;

	}// End Of Method

	private static ConsentDetails parseConsent(String json) {

		ConsentDetails consentInfo = null;

		try {
			consentInfo = CoreUtils.GSON.fromJson(json, ConsentDetails.class);
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsValidation.class.getName(), " Unable to Parse ConsentDetails :: " + json);
		} // End Of Try Catch

		return consentInfo;

	}// End Of Method

	public static void stop() {
		try {

			if (SmsValidation.execService != null && !SmsValidation.execService.isShutdown())
				SmsValidation.execService.shutdownNow();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsValidation.class.getName(),
					" Error stopping SMS Validation service--- \n" + Logger.getStack(e));
		} // End Of Try Catch
	}// End Of Method

}
