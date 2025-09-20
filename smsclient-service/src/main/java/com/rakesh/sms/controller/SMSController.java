package com.rakesh.sms.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.json.simple.*;
import org.json.simple.parser.ParseException;*/
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rakesh.sms.SmsQueueInitializer;
import com.rakesh.sms.beans.DeliveryInfo;
import com.rakesh.sms.beans.DeliveryInfoNotification;
import com.rakesh.sms.beans.InboundMessage;
import com.rakesh.sms.beans.MODetails;
import com.rakesh.sms.beans.MOFormat;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.beans.Request_data;
import com.rakesh.sms.beans.SessionParameters;
import com.rakesh.sms.beans.SmsPromotion;
import com.rakesh.sms.bo.MatchContentBo;
import com.rakesh.sms.bo.SmsSubscriptionBo;
import com.rakesh.sms.bo.ValidationBo;
import com.rakesh.sms.cdr.ReceivedSmsBean;
import com.rakesh.sms.entity.MatchContent;
import com.rakesh.sms.entity.MessageActions;
import com.rakesh.sms.entity.MessageFormats;
import com.rakesh.sms.entity.MtResponse;
import com.rakesh.sms.entity.SmsSubscription;
import com.rakesh.sms.entity.SubscriptionResponse;
import com.rakesh.sms.main.HttpGateway;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.main.SMPPMessageListener;
import com.rakesh.sms.main.SmsValidation;
import com.rakesh.sms.queue.QueueManager;
import com.rakesh.sms.queue.SmsQueue;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreEnums.SMSType;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.Decline;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.util.OptionalParameters;
import com.rakesh.sms.util.RedisConnection;
import com.rakesh.sms.util.SimpleParser;
import com.rakesh.sms.util.XmlParser;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import redis.clients.jedis.Jedis;

/**
 *  '' 		Pushes the SMS request to Priority Queue. The Queue when scheduled, then the SMS is popped and pushed to ESME. (Since Asynchronous, callback feature available to notify BillingEngine on successful Push). 
 *  'sendSyncSms'	Pushes the SMS directly to ESME. Assumes the priority to be High i.e. '0'. No Callback
 *  'receiveSms'	Test URL to Validate a Incoming Message. [CDRs is NOT Created]
 */

/**
 * 'cli' (string) sms sender 'msisdn' (string) sms receiver 'priority'
 * (int)(enum) sms priority => 0/1/2 => High/Medium/Low 'content' (string) sms
 * text 'validate' (boolean) Validates receiver msisdn by length and appends
 * country code 'unicode' (boolean) Use this option, to change encoding format.
 * Current Default: UTF-8 | Define New in global.sms_properties table 'type'
 * (int)(enum) sms type => 0/1 => MT/MO 'callback' (boolean) In case of Async
 * request, enable it to send SMSC response to BillingEngine, with parameters
 * 'reschedule' (boolean) If failed. To reschedule or not. Default false
 * 'circle' (string) Specifically if need to send to a SMSC, use circle as ID
 * 'serviceType' (string) Specified while submitting SMS. Default is CMT 'dcs'
 * (byte) Override DataCodingScheme Value 'script' (int)(enum) Arabic, Latin,
 * Hex Script 'flag' (int)(enum) Used for Optional/Additional Setting 'expiry'
 * (int) Defines Expiry of SMS. Unit Of Expire Time Unit is Defined in
 * Properties. Valid for Async Request only 'sync' (boolean) Defines whether its
 * a sync request or Not. IF Sync => Directly Send the SMS, without using Queues
 * and return the response immediately 'protocol' (int)(enum) Used for defining
 * the protocol
 */




@RestController
public class SMSController {

	// Airtel Malawi
	@Value("${blackoutenable}")
	boolean blackoutenable;

	public static final String DefaultMessageID = "E000";
	public static ValidationBo validation;
	public static MatchContentBo matchContent;
	public static SmsSubscriptionBo smsSubscriber;

	@Autowired
	private ValidationBo validationInstance;

	@PostConstruct
	public void init() {
		SMSController.validation = validationInstance;
	}

	@Autowired
	private SmsQueueInitializer smsQueueInitializer;

	public SmsSubscriptionBo getSmsSubscriber() {
		return smsSubscriber;
	}

	public void setSmsSubscriber(SmsSubscriptionBo smsSubscriber) {
		SMSController.smsSubscriber = smsSubscriber;
	}

	public MatchContentBo getMatchContent() {
		return matchContent;
	}

	public void setMatchContent(MatchContentBo MatchContent) {
		SMSController.matchContent = MatchContent;
	}

	public void setValidation(ValidationBo validation) {
		SMSController.validation = validation;
	}

	public ValidationBo getValidation() {
		return validation;
	}

	/**
	 * 'User-Agent' is mandatory 'Content-Type' =
	 * 'application/x-www-form-urlencoded'
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/pushSms", method = RequestMethod.POST, headers = "Accept=*")
	public @ResponseBody String pushSmsToQueue(@RequestParam(value = "cli", required = false) String cli,
			@RequestParam("msisdn") String msisdn, @RequestParam(value = "priority", required = false) Integer msgType,
			@RequestParam("content") String content,
			@RequestParam(value = "validate", required = false) Boolean validate,
			@RequestParam(value = "unicode", required = false) Boolean unicode,
			@RequestParam(value = "type", required = false) Integer smsType,
			@RequestParam(value = "callback", required = false) Boolean callback,
			@RequestParam(value = "circle", required = false) String circle,
			@RequestParam(value = "reschedule", required = false) Boolean reschedule,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "dcs", required = false) Integer dataCoding,
			@RequestParam(value = "script", required = false) Integer script,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "expiry", required = false) Integer expiry,
			@RequestParam(value = "sync", required = false) Boolean sync,
			@RequestParam(value = "detail", required = false) String extraDetail,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "multiple", required = false) Boolean multiple,
			@RequestParam(value = "session", required = false) Boolean session,
			@RequestParam(value = "sessionEnd", required = false) Boolean sessionEnd, HttpServletRequest request)
			throws IOException {

		return this.addSmsToQueue(cli, msisdn, msgType, content, validate, unicode, smsType, callback, circle,
				reschedule, serviceType, dataCoding, script, flag, expiry, sync, extraDetail, serviceid, multiple,
				session, null, sessionEnd, 0, false, null, null, null, 0, request); // Added for Dream Travel Mexico
																					// (since MT message required a
																					// special msgId)

	}// End Of Method

	@RequestMapping(value = "/sendRandomCotent", method = RequestMethod.GET, headers = "Accept=*")
	public @ResponseBody String sendRandomCotent(@RequestParam(value = "cli", required = false) String cli,
			@RequestParam("msisdn") String msisdn, @RequestParam(value = "priority", required = false) Integer msgType,
			@RequestParam("event") String event, @RequestParam("service") String service,
			@RequestParam(value = "validate", required = false) Boolean validate,
			@RequestParam(value = "unicode", required = false) Boolean unicode,
			@RequestParam(value = "type", required = false) Integer smsType,
			@RequestParam(value = "callback", required = false) Boolean callback,
			@RequestParam(value = "circle", required = false) String circle,
			@RequestParam(value = "reschedule", required = false) Boolean reschedule,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "dcs", required = false) Integer dataCoding,
			@RequestParam(value = "script", required = false) Integer script,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "expiry", required = false) Integer expiry,
			@RequestParam(value = "sync", required = false) Boolean sync,
			@RequestParam(value = "detail", required = false) String extraDetail,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "multiple", required = false) Boolean multiple,
			@RequestParam(value = "session", required = false) Boolean session,
			@RequestParam(value = "sessionEnd", required = false) Boolean sessionEnd, HttpServletRequest request)
			throws IOException {

		String content = "";

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Inside sendRandomCotent controller...");

		CoreUtils.loadMsgContents();

		String contentKey = service + "_" + event;
		ArrayList<String> actions = CoreUtils.getContents(contentKey);
		int actionsSize = actions.size();

		Random random = new Random();
		int rand = random.nextInt(actionsSize);

		content = content + actions.get(rand);

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				"service: " + service + " | event: " + event + " | content: " + content);

		return this.addSmsToQueue(cli, msisdn, msgType, content, validate, unicode, smsType, callback, circle,
				reschedule, serviceType, dataCoding, script, flag, expiry, sync, extraDetail, serviceid, multiple,
				session, null, sessionEnd, 0, false, null, null, null, 0, request); // Added for Dream Travel Mexico
																					// (since MT message required a
																					// special msgId)

	}// End Of Method

	@RequestMapping(value = "/sendSms", method = RequestMethod.GET)
	public @ResponseBody String addSmsToQueue(@RequestParam(value = "cli", required = false) String cli,
			@RequestParam("msisdn") String msisdn, @RequestParam(value = "priority", required = false) Integer msgType,
			@RequestParam("content") String content,
			@RequestParam(value = "validate", required = false) Boolean validate,
			@RequestParam(value = "unicode", required = false) Boolean unicode,
			@RequestParam(value = "type", required = false) Integer smsType,
			@RequestParam(value = "callback", required = false) Boolean callback,
			@RequestParam(value = "circle", required = false) String circle,
			@RequestParam(value = "reschedule", required = false) Boolean reschedule,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "dcs", required = false) Integer dataCoding,
			@RequestParam(value = "script", required = false) Integer script,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "expiry", required = false) Integer expiry,
			@RequestParam(value = "sync", required = false) Boolean sync,
			@RequestParam(value = "detail", required = false) String extraDetail,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "multiple", required = false) Boolean multiple,
			@RequestParam(value = "session", required = false) Boolean session,
			@RequestParam(value = "msgid", required = false) String msgid, // Added for Dream Travel Mexico (since MT
																			// message required a special msgId)
			@RequestParam(value = "sessionEnd", required = false) Boolean sessionEnd,
			@RequestParam(value = "delay", required = false) Integer delay,
			@RequestParam(value = "discard", required = false) Boolean discard,
			@RequestParam(value = "ussdOp", required = false) Integer ussdOp,
			@RequestParam(value = "productId", required = false) String productid,
			@RequestParam(value = "pricepointId", required = false) String pricepointid,
			@RequestParam(value = "extra", required = false) Integer intExtra, HttpServletRequest request)
			throws IOException {

		smsQueueInitializer.initQueue();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String codes[] = CoreUtils.getProperty("countryCodes").split(",");

		boolean sendMultiple = multiple == null ? false : multiple.booleanValue();
		boolean syncRequest = sync == null ? false : sync.booleanValue();

		int smsTypeValue = CoreEnums.Type.MT.ordinal();
		long diffInMillis = 60000L, diffMins = 1L;
		String result = "NullAgrumentsFound";
		boolean validation, send = true;

		Logger.sysLog(LogValues.debug, this.getClass().getName(),
				"UserAgent:: " + request.getHeader("user-agent").trim());
		CoreEnums.Protocol mode = CoreUtils.getProtocol();

		if (CoreUtils.isBlacklisted(msisdn)) {
			result = "BlacklistedUser";
			send = false;
		}

		if (discard != null && discard.booleanValue()) {
			String sDc = CoreUtils.getProperty("discard_percent");
			Decline dc = Decline.getInstance(sDc == null ? 0 : Integer.parseInt(sDc));

			if (dc.isRejected()) {
				result = "Discarded";
				send = false;
				Logger.sysLog(LogValues.info, this.getClass().getName(), msisdn + " Message Discarded " + content);
			}
		}

		long ldelay = 0;
		if (delay != null && delay > 0) {
			ldelay = delay * 1000;
		}

		// burkinaFaso
		if (CoreUtils.getProperty("country").equalsIgnoreCase("bfa")) {
			if (CoreUtils.token1 == null || CoreUtils.token2 == null) {
				CoreUtils.generateAndSaveToken();
			}
		}

		if (smsType != null) {
			smsTypeValue = smsType.intValue();
			if (smsTypeValue >= CoreEnums.Type.values().length - 1) {
				smsTypeValue = CoreEnums.Type.UNKNOWN.ordinal();
				send = false;
			}
		}

		if ((RedisConnection.expiryTimer > 0) && smsTypeValue == CoreEnums.Type.MT.ordinal()) {

			Jedis jedis = null;
			try {

				jedis = RedisConnection.getJedis();

				synchronized (SMSController.class) {

					if (jedis != null && jedis.get(msisdn) != null) {
						result = "DuplicateUser";
						send = false;
						Logger.sysLog(LogValues.info, this.getClass().getName(), "message failure");

					} else {
						if (jedis != null) {
							jedis.setex(msisdn, RedisConnection.expiryTimer, "Now entering in redis.");
						}
					}
				}
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), e.getMessage());
			} finally {
				if (jedis != null)
					RedisConnection.disconnect(jedis);
				Logger.sysLog(LogValues.info, this.getClass().getName(), "Disconnecting jedis");
			}
		}

		if (cli == null || cli.trim().length() == 0) {
			if (smsTypeValue == CoreEnums.Type.MT.ordinal()) {
				cli = CoreUtils.getProperty("callerID");
			} else {

				result = "NullAgrumentsFound";
				send = false;
			}
		}

		if (CoreUtils.getProperty("country").equals("Syria") && CoreUtils.getProperty("operator").equals("MTN")
				&& CoreUtils.getProperty("protocol").equals("HTTP")) {
			if (SMSController.fetchOptionalParamater(request, "language").equalsIgnoreCase("_A"))
				msgid = "0";
			else
				msgid = "1";
		}

		if (validate != null) {

			validation = validate.booleanValue();
			String msisdnBackup = msisdn;

			if (validation && smsTypeValue == CoreEnums.Type.MT.ordinal()) {
				msisdn = CoreUtils.stripCodes(msisdn);
				if (msisdn != null && msisdn.length() != 0) {
					msisdn = codes[0].concat(msisdn);
				} else {
					msisdn = msisdnBackup;
					result = "ValidationFailure";
					send = false;
				}
			} else if (validation && smsTypeValue == CoreEnums.Type.MO.ordinal() && cli != null) {
				cli = CoreUtils.stripCodes(cli);
				if (cli != null && cli.length() != 0) {
					cli = codes[0].concat(cli);
				} else {
					msisdn = msisdnBackup;
					result = "ValidationFailure";
					send = false;
				}
			} // End Of IF Else

		} // End Of Validation IF

		/*
		 * if (!CoreUtils.isWhitelisted(cli, msisdn, "MT")) { result = "NotWhitelisted";
		 * send = false; }
		 */
		boolean callBack = false;

		if (callback != null) {
			callBack = callback.booleanValue();
		}

		boolean ussdSession = false;

		if (session != null) {
			ussdSession = session.booleanValue();
		}

		boolean isSessionInactive = false;

		if (sessionEnd != null) {
			isSessionInactive = sessionEnd.booleanValue();
		}

		int type = CoreEnums.SMSType.Alert.ordinal();

		if (msgType != null) {
			type = msgType.intValue();

			if (type >= CoreEnums.SMSType.values.length - 1) {
				type = CoreEnums.SMSType.UNKNOWN.ordinal();
				send = false;
			}

		}

		/*
		 * log added for finding which queue we are pushing the message when problem
		 * occurred in NCell
		 */
		// Logger.sysLog(LogValues.info, this.getClass().getName(), "The queue for
		// msisdn: " + msisdn + " is: smsqueue" + type);

		if (circle != null && circle.trim().length() != 0) {
			circle = circle.toUpperCase().trim();
		} else
			circle = "";

		CoreEnums.SMSFlag smsflag;
		try {
			smsflag = CoreEnums.SMSFlag.values[flag];
		} catch (Exception e) {
			smsflag = CoreEnums.SMSFlag.UNKNOWN;
		}

		/**
		 * ~~~Blackout Hour~~~ IF SYNC request Does not block SMS because its a
		 * Synchronous Request | Only Warning is given ELSE SMS is pushed at the end of
		 * current Blackout Hour, the SMS is sent Randomly within an Hour period
		 */

		boolean blackedOut = false;
		if (type != SMSType.Service.ordinal()) {

			Date now = new Date();
			blackedOut = CoreUtils.withinBlackoutHours(now);

			if (blackedOut && syncRequest == false) {

				Date bhEnd = CoreUtils.getEndOfCurrentBlackoutHour();
				Random rand = new Random();
				int minute = 60000; // In Millis

				if (bhEnd != null) {

					diffInMillis = bhEnd.getTime() - now.getTime();
					diffInMillis += rand.nextInt(10 * minute) + 1000;
					diffMins = (int) (diffInMillis / (long) minute);

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"Currently In Blackout Hour | SMS Scheduled after " + diffMins + " Mins ");

				} else {
					Logger.sysLog(LogValues.warn, this.getClass().getName(),
							"Currently In Blackout Hour | SMS Scheduling Failed ");
					result = "WithinBlackoutHours";
					send = false;
				}

			} else if (blackedOut && syncRequest) {
				Logger.sysLog(LogValues.warn, this.getClass().getName(),
						" WARNING:- [Pushing SMS] Within Blackout Hours... ");
			} // End Of Blackout Hour Check

		} else if (blackoutenable) { // Airtel Malawi

			Date now = new Date();
			blackedOut = CoreUtils.withinBlackoutHours(now);

			if (blackedOut && syncRequest == false) {

				Date bhEnd = CoreUtils.getEndOfCurrentBlackoutHour();
				Random rand = new Random();
				int minute = 60000; // In Millis

				if (bhEnd != null) {

					diffInMillis = bhEnd.getTime() - now.getTime();
					diffInMillis += rand.nextInt(10 * minute) + 1000;
					diffMins = (int) (diffInMillis / (long) minute);
					type = 3; // Airtel Malawi
					send = true; // Airtel Malawi
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"Currently In Blackout Hour | SMS Scheduled after " + diffMins + " Mins ");
					// Logger.sysLog(LogValues.info, this.getClass().getName(), "The queue for
					// msisdn: " + msisdn + " is changed to: smsqueue" + type); //Airtel Malawi

				} else {
					Logger.sysLog(LogValues.warn, this.getClass().getName(),
							"Currently In Blackout Hour | SMS Scheduling Failed ");
					result = "WithinBlackoutHours";
					send = false;
				}

			} else if (blackedOut && syncRequest) {
				Logger.sysLog(LogValues.warn, this.getClass().getName(),
						" WARNING:- [Pushing SMS] Within Blackout Hours... ");
			} // End Of Blackout Hour Check

		} // End of Priority Check

		if (send == true && msisdn != null && content != null && msisdn.length() != 0 && content.length() != 0) {

			SmsQueue queue = new SmsQueue();

			if (sendMultiple) {

				String contentArray[] = content.split("\\|\\|\\|");

				for (int i = 0; i < contentArray.length; i++) {

					try {
						Message msg = new Message(cli, msisdn, type, contentArray[i], mode.ordinal(), smsTypeValue);
						msg.setCircle(circle);
						msg.setFlag(smsflag);
						System.out.println("type =" + type);
						msg.setTxnid(Long.toString(msg.getTime()));

						msg.setProductid(productid);
						msg.setPricepointid(pricepointid);
						// Burkina Faso
						if (CoreUtils.getProperty("country").equalsIgnoreCase("bfa")) {
							if (cli.equals("399") || cli.equals("226399")) {
								if (CoreUtils.token1 == null) {
									CoreUtils.generateAndSaveToken();
									msg.setToken(CoreUtils.token1);
									msg.setSenderName("Islamic");
								} else {
									msg.setToken(CoreUtils.token1);
									msg.setSenderName("Islamic");
								}
							} else if (cli.equals("145") || cli.equals("226145")) {
								if (CoreUtils.token2 == null) {
									CoreUtils.generateAndSaveToken();
									msg.setToken(CoreUtils.token2);
									msg.setSenderName("MagicVoice");
								} else {
									msg.setToken(CoreUtils.token2);
									msg.setSenderName("MagicVoice");
								}
							}
						}

						// Mexico
						if (msgid != null)
							msg.setMsgid(msgid);

						if (serviceid != null && serviceid.length() > 0)
							msg.setServiceid(serviceid);

						if (extraDetail != null && extraDetail.length() > 0) {
							msg.setExtraDetail(extraDetail.trim());
						}

						try {
							msg.setDataCoding(dataCoding.byteValue());
						} catch (Exception e) {
							Logger.sysLog(LogValues.debug, this.getClass().getName(),
									" Unable to Parse DataCoding | Setting Default = 8 ");
						}

						if (unicode != null)
							msg.setEncoding(unicode.toString());
						else
							msg.setEncoding(false);

						if (reschedule != null) {
							msg.setReschedule(reschedule);
							msg.setRemainingRetries(CoreUtils.getFailureRetries());
						} // End Of Retry Check

						if (syncRequest == false && callBack == true) {
							msg.setCallback(true);
							CoreUtils.setCallbackDetails(msg, request);
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"After setting callback details..." + msg.toString());
						} else if (syncRequest == true && callBack == true) {
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									" Sorry... NO CallBack at Sync Request... ");
							msg.setCallback(false);
						}

						if (serviceType != null && serviceType.length() >= 0) {
							msg.setServiceType(serviceType.trim());
						}
						if (expiry != null) {
							msg.setExpiryTime(CoreUtils.getExpiryTime(expiry));
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									" ExpiryTime=" + msg.getExpiryTime().toString());
						}
						String param = SMSController.fetchOptionalParamater(request, "language");
						if (OptionalParameters.SET(msg, param) == false) {
							String message = SMSController.convertTexttoUnicode(contentArray[i], script);
							msg.setMessage(message);
						}

						if (ussdSession) {
							SessionParameters sessParams = new SessionParameters();

							sessParams.setSessionInactive(0);

							if (isSessionInactive) {
								sessParams.setSessionInactive(1);
							}

							String sessionId = request.getSession().getId();
							if (request.getParameter("sessionid") != null)
								sessParams.setItsSessionInfo(Integer.parseInt(request.getParameter("sessionid")));
							else
								sessParams.setItsSessionInfo(Integer.parseInt(sessionId));
							if (ussdOp != null)
								sessParams.setUssdServiceOp(ussdOp);
							else
								sessParams.setUssdServiceOp(2);

							msg.setOptionalSessParams(sessParams);

							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"Session:" + ussdSession + " | sessionId:"
											+ msg.getOptionalSessParams().getItsSessionInfo() + " | ussdServiceOp:"
											+ msg.getOptionalSessParams().getUssdServiceOp());
						}

						Logger.sysLog(LogValues.info, this.getClass().getName(),
								" CallBack= " + msg.isCallback() + " | ScriptType=" + script + " |  ServiceType="
										+ msg.getServiceType() + " | msisdn=" + msg.getMsisdn());

						if (syncRequest == false) {

							if (blackedOut == false && queue.push(msg, ldelay))
								result = "Success";
							else if (blackedOut && queue.push(msg, diffInMillis))
								result = "Success";
							else
								result = "Failure";

						} else {
							/** Sync Request */
							result = QueueManager.getPusherObject().push(msg, true);

							if (result != null && (result.toLowerCase().contains("true")
									|| result.toLowerCase().contains("Success"))) {
								result = "Success";
							} else {
								result = "Failure";
							}

						} // End Of Sync Check

						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"URL Hit for SMS:: " + result + " msisdn: " + msg.getMsisdn());

					} catch (Exception e) {
						Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
						result = "Failure";
					}
				}

			} else {

				try {

					Message msg = new Message(cli, msisdn, type, content, mode.ordinal(), smsTypeValue);

					if (content.contains("$uid$")) {

						String phraseToEncrypt = CoreUtils.getProperty("uid");

						String finalmsisdn = CoreUtils.stripCodes(msisdn);

						content = CoreUtils.getUid(finalmsisdn);

					}

					System.out.println("content " + content);

					msg.setCircle(circle);
					msg.setFlag(smsflag);

					msg.setTxnid(Long.toString(msg.getTime()));

					msg.setProductid(productid);
					msg.setPricepointid(pricepointid);
					if (msgid != null) {
						msg.setMsgid(msgid); // Added for Dream Travel Mexico (since MT message required a special
												// msgId)
					}

//					//Burkina Faso
//					if(CoreUtils.getProperty("country").equalsIgnoreCase("bfa")) 
//					{
//						if(cli.equals("399") || cli.equals("226399")) {
//							if(CoreUtils.token1 == null) {
//								CoreUtils.generateAndSaveToken();
//								msg.setToken(CoreUtils.token1);
//								msg.setSenderName("Islamic");
//							} else {
//								msg.setToken(CoreUtils.token1);
//								msg.setSenderName("Islamic");									
//							}
//						} else if(cli.equals("145") || cli.equals("226145")) 
//						{
//							if(CoreUtils.token2 == null) {
//								CoreUtils.generateAndSaveToken();
//								msg.setToken(CoreUtils.token2);
//								msg.setSenderName("MagicVoice");
//							} else {
//								msg.setToken(CoreUtils.token2);
//								msg.setSenderName("MagicVoice");
//							}
//						}
//					}

					Logger.sysLog(LogValues.info, this.getClass().getName(), " msgid set is: " + msg.getMsgid());

					if (serviceid != null && serviceid.length() > 0)
						msg.setServiceid(serviceid);

					if (extraDetail != null && extraDetail.length() > 0) {
						msg.setExtraDetail(extraDetail.trim());
					}

					try {
						msg.setDataCoding(dataCoding.byteValue());
					} catch (Exception e) {
						Logger.sysLog(LogValues.debug, this.getClass().getName(),
								" Unable to Parse DataCoding | Setting Default = 8 ");
					}

					if (unicode != null)
						msg.setEncoding(unicode.toString());
					else
						msg.setEncoding(false);

					if (reschedule != null) {
						msg.setReschedule(reschedule);
						msg.setRemainingRetries(CoreUtils.getFailureRetries());
					} // End Of Retry Check

					if (syncRequest == false && callBack == true) {
						msg.setCallback(true);
						CoreUtils.setCallbackDetails(msg, request);
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"After setting callback details..." + msg.toString());
					} else if (syncRequest == true && callBack == true) {
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								" Sorry... NO CallBack at Sync Request... ");
						msg.setCallback(false);
					}

					if (serviceType != null && serviceType.length() >= 0) {
						msg.setServiceType(serviceType.trim());
					}
					if (expiry != null) {
						msg.setExpiryTime(CoreUtils.getExpiryTime(expiry));
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								" ExpiryTime=" + msg.getExpiryTime().toString());
					}

					String param = SMSController.fetchOptionalParamater(request, "language");
					if (OptionalParameters.SET(msg, param) == false) {
						String message = SMSController.convertTexttoUnicode(content, script);
						msg.setMessage(message);
					}

					if (ussdSession) {
						SessionParameters sessParams = new SessionParameters();

						String sessionId = request.getSession().getId();

						if (request.getParameter("sessionid") != null)
							sessParams.setItsSessionInfo(Integer.parseInt(request.getParameter("sessionid")));
						else
							sessParams.setItsSessionInfo(Integer.parseInt(sessionId));
						if (ussdOp != null)
							sessParams.setUssdServiceOp(ussdOp);
						else
							sessParams.setUssdServiceOp(2);

						sessParams.setSessionInactive(0);

						if (isSessionInactive) {
							sessParams.setSessionInactive(1);
						}

						msg.setOptionalSessParams(sessParams);

						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"Session:" + ussdSession + " | sessionId:"
										+ msg.getOptionalSessParams().getItsSessionInfo() + " | ussdServiceOp:"
										+ msg.getOptionalSessParams().getUssdServiceOp());
					}

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" CallBack= " + msg.isCallback() + " | ScriptType=" + script + " |  ServiceType="
									+ msg.getServiceType() + " | msisdn=" + msg.getMsisdn());

					if (syncRequest == false) {
						/** ASync Request */
						if (blackedOut == false && queue.push(msg, ldelay))
							result = "Success";
						else if (blackedOut && queue.push(msg, diffInMillis))
							result = "Success";
						else
							result = "Failure";

					} else {
						/** Sync Request */
						result = QueueManager.getPusherObject().push(msg, true);

						if (result != null && (result.toLowerCase().contains("true")
								|| result.toLowerCase().contains("success"))) {
							result = "Success";
						} else {
							result = "Failure";
						}

					} // End Of Sync Check

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"URL Hit for SMS:: " + result + " msisdn: " + msg.getMsisdn());

				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
					result = "Failure";
				}
			}

		} else {

			if (sendMultiple) {
				String contentArray[] = content.split("\\|\\|\\|");

				for (int i = 0; i < contentArray.length; i++) {
					Message msg = new Message(cli, msisdn, type, contentArray[i], mode.ordinal(), smsTypeValue);
					msg.setCircle(circle);

					msg.setTxnid(Long.toString(msg.getTime()));

					msg.setProductid(productid);
					msg.setPricepointid(pricepointid);
					if (msgid != null)
						msg.setMsgid(msgid); // Mexico

					// Burkina Faso
					if (CoreUtils.getProperty("country").equalsIgnoreCase("bfa")) {
						if (cli.equals("399") || cli.equals("226399")) {
							if (CoreUtils.token1 == null) {
								CoreUtils.generateAndSaveToken();
								msg.setToken(CoreUtils.token1);
								msg.setSenderName("Islamic");
							} else {
								msg.setToken(CoreUtils.token1);
								msg.setSenderName("Islamic");
							}
						} else if (cli.equals("145") || cli.equals("226145")) {
							if (CoreUtils.token2 == null) {
								CoreUtils.generateAndSaveToken();
								msg.setToken(CoreUtils.token2);
								msg.setSenderName("MagicVoice");
							} else {
								msg.setToken(CoreUtils.token2);
								msg.setSenderName("MagicVoice");
							}
						}
					}

					if (serviceid != null && serviceid.length() > 0)
						msg.setServiceid(serviceid);

					if (extraDetail != null && extraDetail.length() > 0) {
						msg.setExtraDetail(extraDetail.trim());
					}

					// SmsCdrBean smsCdr = CoreUtils.getSmsCDR(msg);
					Date now = new Date(msg.getTime());
					// smsCdr.setSubmittime(sdf.format(now));
					// smsCdr.setMessageId(SMSController.DefaultMessageID);

//					if (CdrCreator.isJsonCDR()) {
//						smsCdr.setStatus("Failure " + result);
//					} else {
//						smsCdr.setStatus(result);
//					}

					Logger.sysLog(LogValues.warn, this.getClass().getName(),
							" Unable to Push SMS for msisdn= " + msisdn + " | content= " + content + " | time= "
									+ now.toString() + " | reason= " + result + ".  [CDR successfully created] ");
					// CdrCreator.saveAsXML(smsCdr);
					result = "Failure";

					/**
					 * Do Not send Failure callback in case of @Home SMS Push failure
					 */
					if (callBack == true && syncRequest == false) {
						msg.setCallback(true);
						CoreUtils.setCallbackDetails(msg, request);
						msg.getCallbackDetails().setCallbackStatus(result);
						Logger.sysLog(LogValues.info, this.getClass().getName(), "Sending callback: " + msg.toString());
						CoreUtils.sendCallback(msg);
					} // End Of CallBack IF

				}

			} else {

				Message msg = new Message(cli, msisdn, type, content, mode.ordinal(), smsTypeValue);
				msg.setCircle(circle);

				msg.setTxnid(Long.toString(msg.getTime()));

				msg.setProductid(productid);
				msg.setPricepointid(pricepointid);
				if (serviceid != null && serviceid.length() > 0)
					msg.setServiceid(serviceid);

				if (msgid != null)
					msg.setMsgid(msgid); // Mexico

				if (extraDetail != null && extraDetail.length() > 0) {
					msg.setExtraDetail(extraDetail.trim());
				}
//
//				//Burkina Faso
//				if(CoreUtils.getProperty("country").equalsIgnoreCase("bfa")) {
//					if(cli.equals("399") || cli.equals("226399")) {
//						if(CoreUtils.token1 == null) {
//							CoreUtils.generateAndSaveToken();
//							msg.setToken(CoreUtils.token1);
//							msg.setSenderName("Islamic");
//						} else {
//							msg.setToken(CoreUtils.token1);
//							msg.setSenderName("Islamic");									
//						}
//					} else if(cli.equals("145") || cli.equals("226145")) {
//						if(CoreUtils.token2 == null) {
//							CoreUtils.generateAndSaveToken();
//							msg.setToken(CoreUtils.token2);
//							msg.setSenderName("MagicVoice");
//						} else {
//							msg.setToken(CoreUtils.token2);
//							msg.setSenderName("MagicVoice");
//						}
//					}
//				}

//				SmsCdrBean smsCdr = CoreUtils.getSmsCDR(msg);
				Date now = new Date(msg.getTime());
//				smsCdr.setSubmittime(sdf.format(now));
//				smsCdr.setMessageId(SMSController.DefaultMessageID);
//
//				if (CdrCreator.isJsonCDR()) {
//					smsCdr.setStatus("Failure " + result);
//				} else {
//					smsCdr.setStatus(result);
//				}

				Logger.sysLog(LogValues.warn, this.getClass().getName(),
						" Unable to Push SMS for msisdn= " + msisdn + " | content= " + content + " | time= "
								+ now.toString() + " | reason= " + result + ".  [CDR successfully created] ");
				// CdrCreator.saveAsXML(smsCdr);
				result = "Failure";

				/**
				 * Do Not send Failure callback in case of @Home SMS Push failure
				 */
				if (callBack == true && syncRequest == false) {
					msg.setCallback(true);
					CoreUtils.setCallbackDetails(msg, request);
					msg.getCallbackDetails().setCallbackStatus(result);
					Logger.sysLog(LogValues.info, this.getClass().getName(), "Sending callback: " + msg.toString());
					CoreUtils.sendCallback(msg);
				} // End Of CallBack IF
			} // End Of Multiple Message Check

		} // End Of If Else

		return result;

	}// End Of Request Mapping

	/**
	 * GET Request Mapping, used for testing sendSms API locally
	 */
	@RequestMapping(value = "/testsendSms", method = RequestMethod.GET)
	public @ResponseBody String testsendSmsLocally(@RequestParam(value = "cli") String shortcode,
			@RequestParam(value = "msisdn") String msisdn, @RequestParam(value = "message") String message,
			@RequestParam(value = "type") String type, @RequestParam(value = "msdid") String msgid) {

		String response = "Hello";

		return "response: " + response + " message: " + message + " cli: " + shortcode + " msisdn: " + msisdn;
	}

	/**
	 * POST Request Mapping, used for handling SMS Promotions
	 */
	@RequestMapping(value = "/promote", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	public @ResponseBody String addSMSPromotion(@RequestParam(value = "message") String smsText,
			@RequestParam("pname") String promotionName, @RequestParam("cli") String callerId,
			@RequestParam("validation") Boolean validation, @RequestParam("circle") String circle,
			@RequestParam("expiryDate") String expiryDate, @RequestParam("expiryTime") String expiryTime,
			@RequestParam("startDate") String startDate, @RequestParam("startTime") String startTime,
			@RequestParam("baseType") String baseType,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "smsFlag", required = false) Integer smsFlag,
			@RequestParam(value = "protocol", required = false) Integer protocol,
			@RequestParam(value = "inputMsisdn", required = false) String msisdn,
			@RequestParam(value = "baseFile[]", required = false) MultipartFile file,
			@RequestParam(value = "serviceid", required = false) String serviceid, HttpServletRequest request,
			HttpServletResponse resp) {

		String codes[] = CoreUtils.getProperty("countryCodes").split(",");
		List<String> baseMsisdn = new ArrayList<String>(1000);
		SmsPromotion promotion = null;
		String reason = "", temp;
		boolean response = false;
		
		System.out.println("expiryDate------------------"+expiryDate);

		if (circle != null && circle.trim().equalsIgnoreCase("default")) {
			circle = Pusher.getDefaultCircle();
		}

		promotion = new SmsPromotion(promotionName, smsText, callerId, circle);
		Calendar cal = Calendar.getInstance();

		if (serviceid != null && serviceid.length() > 0)
			promotion.setServiceid(serviceid);

		if (smsFlag != null) {
			promotion.setFlag(smsFlag.intValue());
		}

		// added for sms through http

		if (protocol != null) {
			promotion.setProtocol(protocol.intValue());
		}

		if (startDate != null && startTime != null) {
			String startDateTime = startDate + " " + startTime + ":00.000";
			Date sDateTime = CoreUtils.getDate(startDateTime);

			if (sDateTime != null) {
				promotion.setStarttime(sDateTime);
			} else {
				promotion.setStarttime(cal.getTime());
			}
		} else {
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Promotion Start Time Not Specified Correctly ");
			promotion.setStarttime(cal.getTime());
		} // End Of Start Time Check

		if (expiryDate != null && expiryTime != null) {
			String expiryDatetime = expiryDate + " " + expiryTime + ":00.000";
			Date eDatetime = CoreUtils.getDate(expiryDatetime);

			if (eDatetime != null && cal.getTime().before(eDatetime)) {
				promotion.setExpiry(eDatetime);
			} else {
				Logger.sysLog(LogValues.error, this.getClass().getName(), " Promotion Expiry Before Current Time ");
				cal.add(Calendar.HOUR_OF_DAY, 72);
				promotion.setExpiry(cal.getTime());
			}
		} else {
			Logger.sysLog(LogValues.warn, this.getClass().getName(), " Promotion Expire Time Not Specified Correctly ");
			cal.add(Calendar.HOUR_OF_DAY, 72);
			promotion.setExpiry(cal.getTime());
		} // End Of Expire Time Check

		if (language != null && language.length() > 0) {
			promotion.setLanguage(language);
		}

		if (smsText != null && callerId != null) {

			try {

				if (file != null && file.isEmpty() == false && baseType.equalsIgnoreCase("upload")) {
					/* Msisdn Base uploaded using a file */
					Logger.sysLog(LogValues.debug, this.getClass().getName(), " Upload File Details::  Name="
							+ file.getOriginalFilename() + " | Format=" + file.getContentType());

					try {

						InputStreamReader reader = new InputStreamReader(file.getInputStream());
						BufferedReader buffer = new BufferedReader(reader);

						do {

							msisdn = buffer.readLine();

							if (msisdn != null && msisdn.length() > 0 && validation.booleanValue()) {
								temp = CoreUtils.stripCodes(msisdn.replaceAll(",", ""));
								if (temp != null && temp.length() > 0) {
									msisdn = codes[0].concat(temp);
								} else {
									Logger.sysLog(LogValues.warn, this.getClass().getName(), " Msisdn " + msisdn
											+ " Ignored from Promotion base |  Reason= ValidationFailure ");
								}
							} // End Of Validation

							if (msisdn != null && msisdn.trim().length() > 0) {
								if (CoreUtils.isBlacklisted(msisdn) == true) {
									Logger.sysLog(LogValues.warn, this.getClass().getName(), " Msisdn " + msisdn
											+ " Ignored from Promotion base |  Reason= Blacklisted ");
								} else
									baseMsisdn.add(msisdn);
							} // End of Msisdn Check

						} while (msisdn != null);

						buffer.close();
						reader.close();

					} catch (Exception ie) {
						Logger.sysLog(LogValues.error, this.getClass().getName(),
								" ERROR Unable to read the uploaded Base File \n" + Logger.getStack(ie));
						reason = "FileUploadError";
					} // End Of Try Catch

				} else if (msisdn != null && msisdn.length() > 0 && baseType.equalsIgnoreCase("category")) {
					/* Comma Separated Msisdn Input in Textbox */
					String[] numbers = msisdn.split(",");

					for (int i = 0; i < numbers.length; i++) {

						if (numbers[i] != null && validation.booleanValue()) {
							temp = CoreUtils.stripCodes(numbers[i]);
							if (temp != null && temp.length() > 0) {
								msisdn = codes[0].concat(temp);
							} else {
								Logger.sysLog(LogValues.warn, this.getClass().getName(), " Msisdn " + numbers[i]
										+ " Ignored from Promotion base |  Reason= ValidationFailure ");
							}
						} // End Of Validation

						if (msisdn != null && msisdn.trim().length() > 0) {
							if (CoreUtils.isBlacklisted(msisdn) == true) {
								Logger.sysLog(LogValues.warn, this.getClass().getName(),
										" Msisdn " + msisdn + " Ignored from Promotion base |  Reason= Blacklisted ");
							} else
								baseMsisdn.add(msisdn);
						}

					} // End Of Loop

				} else {
					reason = "BaseNotFound";
				} // End Of Base Type Check

				if (baseMsisdn.size() > 0 && reason.length() == 0) {

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"Starting PROMOTION " + promotion.getJobName() + " with baseSize= " + baseMsisdn.size());
					promotion.setStatus("Success");
					promotion.setBase(baseMsisdn);
					response = true;

				} else {
					reason = "ZeroBaseSize";
				} // End Of Base

				/**
				 * [IF]Case 1: Msisdn starts with country code and also has special characters.
				 * Hence Ignoring + of country code, it checks for special characters. [ELSE
				 * IF]Case 2: Msisdn does not contains country code and contains special
				 * characters. (Normal Scenario)
				 */
				if (callerId.startsWith("+") && CoreUtils.containsSpecialCharacters(callerId.substring(1))) {
					reason = "CLIcontainsSpecialChars";
					response = false;
				} else if (callerId.startsWith("+") == false && CoreUtils.containsSpecialCharacters(callerId)) {
					reason = "CLIcontainsSpecialChars";
					response = false;
				} // End Of CallerId Check

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" ERROR Unable to Start Promotions \n" + Logger.getStack(e));
				reason = "InternalError";
			} // End Of Main Try Catch

		} else {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " ERROR Invalid Parameters ");
			reason = "InvalidParameters";
		} // End Of NULL Check

		resp.setHeader("Refresh", "5;url=../");

		if (response == true) {
			promotion.startPromotions();
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" PROMOTION " + promotion.getJobName() + " ended with status= 'Success'");
			return "Success";
		} else {
			promotion.setCurrentTimestamp();
			promotion.setStatus("Failed: " + reason);
			promotion.startPromotions();
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" PROMOTION " + promotion.getJobName() + " ended with status= 'Failure " + reason + "'");
			return "Failure " + reason;
		}

	}// End Of Request Mapping

	/**
	 * POST Request Mapping, used for adding New MO Keyword
	 */
	@RequestMapping(value = "/addMO", method = RequestMethod.POST)
	public @ResponseBody String addMOKeyword(@RequestParam(value = "action") String action,
			@RequestParam("shcode") String shortcode, @RequestParam("keyword") String keyword,
			@RequestParam("type") String type, HttpServletRequest request, HttpServletResponse resp) {

		String result = "";

		if (action.trim().length() > 0 && shortcode.trim().length() > 0 && keyword.trim().length() > 0
				&& type.trim().length() > 0) {

			String[] args = keyword.split(String.valueOf(' '));
			MessageFormats format = new MessageFormats();
			boolean tooManyArguments = false;
			int count = 1;

			format.setServiceCode(shortcode.trim());

			for (int i = 0; i < args.length; i++) {
				if (args[i] != null && args[i].length() > 0) {

					switch (count) {

					case 1:
						format.setKeyword(args[i].trim());
						format.setSubkey("#");
						format.setArgument1("#");
						count++;
						break;
					case 2:
						format.setSubkey(args[i].trim());
						format.setArgument1("#");
						count++;
						break;
					case 3:
						format.setArgument1(args[i].trim());
						format.setArgument2("#");
						count++;
						break;
					case 4:
						format.setArgument2(args[i].trim());
						format.setArgument3("#");
						count++;
						break;
					case 5:
						format.setArgument3(args[i].trim());
						count++;
						break;
					case 6:
					default:
						tooManyArguments = true;
						break;
					}// End Of Switch

				} // End of IF
			} // End Of Loop

			if (tooManyArguments == false) {

				MessageActions moaction = new MessageActions();
				moaction.setDetails(action.trim());
				moaction.setType(type.trim());

				int moId = SMSController.validation.addMO(format, moaction);

				if (moId != -1)
					result = " MO Successfully added to Database...";
				else
					result = " Adding MO failed :( ";

			} else {
				result = " Too Many Keywords... MO NOT added... ";
			}

		} else {
			result = " Adding MO failed :(   Please check the fields...";
		}

		resp.setHeader("Refresh", "2;url=more/moconfigurations.html");
		return result;
	}// End Of Request Mapping

	public String addMTResponse(MtResponse mtresponse) {

		String result = "failed";

		if (SMSController.validation.addMtResponse(mtresponse)) {
			result = "success";
		}
		return result;

	}

	/**
	 * POST Request Mapping, used for adding MO Action (In case of multiple Actions
	 * for one MO)
	 */
	@RequestMapping(value = "/addMOAction", method = RequestMethod.POST)
	public @ResponseBody String addMOAction(@RequestParam("moid") String moid,
			@RequestParam(value = "action") String action, @RequestParam("type") String type,
			HttpServletRequest request, HttpServletResponse resp) {

		String result;
		
		 System.out.println("here u are..");

		if (moid != null && action.trim().length() > 0 && type.trim().length() > 0) {

			try {
				int moId = Integer.parseInt(moid);

				MessageActions moaction = new MessageActions();
				moaction.setDetails(action.trim());
				moaction.setType(type.trim());
				moaction.setMoId(moId);
				 System.out.println("claling addMOAction");
				SMSController.validation.addMOAction(moaction);
				result = " MO Action Successfully added to DB...";
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" AddMOAction Request | Invalid MoId :: " + moid);
				result = " Invalid MO-ID... Adding MO Action failed :( ";
			} // End of Try Catch

		} else {
			result = " Adding MO Action failed :(  Please check the fields...";
		}

		resp.setHeader("Refresh", "2;url=more/moconfigurations.html");
		return result;
	}// End Of Request Mapping

	/**
	 * GET/POST Request Mapping, used by Operator to push MO via HTTP request
	 */

	@RequestMapping(value = "/postMO", method = RequestMethod.POST)
	public @ResponseBody String receiveMoPOST(@RequestParam(value = "msisdn", required = false) String msisdn,
			@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "cli", required = false) String cli,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "source", required = false) String source,
			@RequestParam(value = "responseType", required = false) String responseType, HttpServletRequest request) {

		try {
			ServletInputStream is = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String data = br.readLine();

			Logger.sysLog(LogValues.info, this.getClass().getName(), "Data received:" + data);

			if (data != null) {
				if (data.indexOf("<soap") != -1) {
					responseType = "soap";
					message = data;
				} else if (data.indexOf("<?xml") != -1) {
					responseType = "xml";
					message = data;
				}
			}

		} catch (IOException e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					"[Post MO] Error in reading from input stream : " + Logger.getStack(e));
		}

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				"Message received: " + message + " responseType: " + responseType);

		return this.receiveMoGET(msisdn, message, cli, serviceType, responseType, null, source, null, null, null, null,
				null); // Added for Dream Travel Mexico (since MT message required a special msgId)
	}// End of Request Mapping

	@RequestMapping(value = "/receiveMO", method = RequestMethod.GET)
	public @ResponseBody String receiveMoGET(@RequestParam(value = "msisdn", required = false) String msisdn,
			@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "cli", required = false) String cli,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "responseType", required = false) String responseType,
			@RequestParam(value = "msgid", required = false) String msgid,
			@RequestParam(value = "source", required = false) String source,
			@RequestParam(value = "X-Source-Addr", required = false) String X_Source_Addr,
			@RequestParam(value = "X-Dest-Addr", required = false) String X_Dest_Addr,
			@RequestParam(value = "X-Pull-Trx-Id", required = false) String X_Pull_Trx_Id,
			@RequestParam(value = "_SC", required = false) String SC,
			@RequestParam(value = "_TID", required = false) String TID) {
		// Added for Dream Travel Mexico (since MT message required a special msgId)
		ReceivedSmsBean cdr = new ReceivedSmsBean();
		cdr.setStatus("Success");
		cdr.setDeliveryReport(false);
		cdr.setTime(CoreUtils.getCurrentTimeStamp());

		if (CoreUtils.getProperty("country").equals("Indonesia") && CoreUtils.getProperty("operator").equals("XL")
				&& CoreUtils.getProperty("protocol").equals("HTTP")) {

			msisdn = X_Source_Addr;
			cli = X_Dest_Addr;
			message = SC;

			Logger.sysLog(LogValues.info, this.getClass().getName(),
					"Received MO:  X-Source-Addr , X-Dest-Addr , X-Pull-Trx-Id , " + " _SC , _TID => " + X_Source_Addr
							+ " , " + X_Dest_Addr + " , " + X_Pull_Trx_Id + " , " + SC + " , " + TID);
		}

		if (source != null)
			cdr.setMode(source);

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Message received: " + message);

		// Added for Dream Travel Mexico (since MT message required a special msgId)
		if (msgid != null) {
			cdr.setMsgid(msgid);
			Logger.sysLog(LogValues.info, this.getClass().getName(), "message_id is: " + msgid);
		}

		if (responseType != null) {
			if (responseType.equalsIgnoreCase("xml") && !message.equals("")) {

				Logger.sysLog(LogValues.info, this.getClass().getName(), "Response type : xml");

				// if(CoreUtils.getProperty("country").equalsIgnoreCase("PHI")
				// &&
				// CoreUtils.getProperty("operator").equalsIgnoreCase("SMART"))
				// {
				MOFormat moFormat = (MOFormat) XmlParser.parseXml(message, new MOFormat());

				cdr.setSender(moFormat.getInboundMessage().get(0).getSenderAddress().substring(4));
				cdr.setReceiverMsisdn(moFormat.getInboundMessage().get(0).getDestinationAddress());
				cdr.setMessageId(moFormat.getInboundMessage().get(0).getMessageId());

				message = moFormat.getInboundMessage().get(0).getMessage();
				msisdn = moFormat.getInboundMessage().get(0).getSenderAddress().substring(4).trim();
				cli = moFormat.getInboundMessage().get(0).getDestinationAddress().trim();
				// }

			} else if (responseType.equalsIgnoreCase("soap")) {
				int startIndex = message.indexOf("<ns2:message>");

				if (startIndex != -1) {
					message = message.substring(startIndex, message.indexOf("</ns2:message>") + 15);
					message = message.replaceAll("ns2:", "mo").replaceAll("tel:", "");

					if (!message.equals("")) {
						InboundMessage mo = (InboundMessage) XmlParser.parseXml(message, new InboundMessage());
						cdr.setSender(mo.getSenderAddress());
						cdr.setReceiverMsisdn(mo.getDestinationAddress());
						message = mo.getMessage();
						msisdn = mo.getSenderAddress();
						cli = mo.getDestinationAddress();
					} else
						Logger.sysLog(LogValues.info, SMSController.class.getName(), "[Post MO] MO Received is empty!");
				} else
					Logger.sysLog(LogValues.info, SMSController.class.getName(), "[Post MO] MO Received is empty!");
			} else {
				Logger.sysLog(LogValues.info, SMSController.class.getName(), "[Post MO] ResponseType not yet defined!");
			}

		} else {
			cdr.setSender(msisdn.trim());
			cdr.setReceiverMsisdn(cli.trim());
		} // End Of ResponseType Check

		if (serviceType != null)
			cdr.setServiceType(serviceType.trim());

		String receivedMessage = SMPPMessageListener.getTrimmedMessage(message);
		Logger.sysLog(LogValues.info, this.getClass().getName(),
				" MO Received :: [" + msisdn + "] [" + cli + "]::  Message=" + receivedMessage);

		SmsValidation validator = new SmsValidation();
		validator.init(); // rk for self
		validator.parseAndValidate(receivedMessage, cdr);

		return "Success";

	}// End of Request Mapping

	// changes in receive mo according to orange egypt added mo as mapping

	@RequestMapping(value = "/mo", method = RequestMethod.GET, produces = "application/xml")
	public @ResponseBody String moGET(@RequestParam(value = "msisdn", required = true) String msisdn,
			@RequestParam("action") String message, @RequestParam(value = "ServiceID", required = false) String cli,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "responseType", required = false) String responseType,
			@RequestParam(value = "Extra", required = false) String extra,
			@RequestParam(value = "ServiceName", required = false) String serviceName) {

		ReceivedSmsBean cdr = new ReceivedSmsBean();
		cdr.setStatus("Success");
		cdr.setDeliveryReport(false);
		cdr.setTime(CoreUtils.getCurrentTimeStamp());

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Message received: " + message);

		if (responseType != null) {
			if (responseType.equalsIgnoreCase("xml") && !message.equals("")) {

				Logger.sysLog(LogValues.info, this.getClass().getName(), "Response type : xml");

				// if(CoreUtils.getProperty("country").equalsIgnoreCase("PHI")
				// &&
				// CoreUtils.getProperty("operator").equalsIgnoreCase("SMART"))
				// {
				MOFormat moFormat = (MOFormat) XmlParser.parseXml(message, new MOFormat());

				cdr.setSender(moFormat.getInboundMessage().get(0).getSenderAddress().substring(4));
				cdr.setReceiverMsisdn(moFormat.getInboundMessage().get(0).getDestinationAddress());
				cdr.setMessageId(moFormat.getInboundMessage().get(0).getMessageId());

				message = moFormat.getInboundMessage().get(0).getMessage();
				msisdn = moFormat.getInboundMessage().get(0).getSenderAddress().substring(4).trim();
				cli = moFormat.getInboundMessage().get(0).getDestinationAddress().trim();
				// }

			} else if (responseType.equalsIgnoreCase("soap")) {
				int startIndex = message.indexOf("<ns2:message>");

				if (startIndex != -1) {
					message = message.substring(startIndex, message.indexOf("</ns2:message>") + 15);
					message = message.replaceAll("ns2:", "mo").replaceAll("tel:", "");

					if (!message.equals("")) {
						InboundMessage mo = (InboundMessage) XmlParser.parseXml(message, new InboundMessage());
						cdr.setSender(mo.getSenderAddress());
						cdr.setReceiverMsisdn(mo.getDestinationAddress());
						message = mo.getMessage();
						msisdn = mo.getSenderAddress();
						cli = mo.getDestinationAddress();
					} else
						Logger.sysLog(LogValues.info, SMSController.class.getName(), "[Post MO] MO Received is empty!");
				} else
					Logger.sysLog(LogValues.info, SMSController.class.getName(), "[Post MO] MO Received is empty!");
			} else {
				Logger.sysLog(LogValues.info, SMSController.class.getName(), "[Post MO] ResponseType not yet defined!");
			}

		} else {
			cdr.setSender(msisdn.trim());
			cdr.setReceiverMsisdn(cli.trim());
		} // End Of ResponseType Check

		if (serviceType != null)
			cdr.setServiceType(serviceType.trim());

		String receivedMessage = SMPPMessageListener.getTrimmedMessage(message);
		Logger.sysLog(LogValues.info, this.getClass().getName(),
				" MO Received :: [" + msisdn + "] [" + cli + "]::  Message=" + receivedMessage);

		SmsValidation validator = new SmsValidation();

		validator.parseAndValidate(receivedMessage, cdr);

		return "Success";

	}// End of Request Mapping

	/* receive mo for dream travel service Mexico */

	@RequestMapping(value = "/receivemo", method = RequestMethod.POST)
	public @ResponseBody String receiveMoPost(@RequestBody String request) {

		Logger.sysLog(LogValues.info, this.getClass().getName(), "xml received: " + request);
		Request_data reqData = (Request_data) XmlParser.parseXml(request, new Request_data());

		Logger.sysLog(LogValues.info, this.getClass().getName(), "msisdn is: " + reqData.getIdentifier()
				+ " ,Message received: " + reqData.getMessage() + " ,message_id received: " + reqData.getMessage_id());

		receiveMoGET(reqData.getIdentifier(), reqData.getMessage(), reqData.getShortcode(), null, null,
				String.valueOf(reqData.getMessage_id()), null, null, null, null, null, null);

		return "<request_data>\n" + "<result_code>200</result_code>\n" + "</request_data>";

	}
	/* end of receive mo for dream travel service Mexico */

	@RequestMapping(value = "mtnsyria/mo", method = RequestMethod.GET)
	public @ResponseBody String receiveMoForMtnsyria(@RequestParam(value = "GSM", required = false) String msisdn,
			@RequestParam(value = "msg", required = false) String message,
			@RequestParam(value = "lang", required = false) String language,
			@RequestParam(value = "SC", required = false) String cli) {

		if (msisdn == null || cli == null || message == null) {
			Logger.sysLog(LogValues.warn, this.getClass().getName(),
					"Request Failed because required parameters are missing, msisdn:" + msisdn + " ,cli: " + cli
							+ " ,message: " + message);

			return "Failure";
		}
		return receiveMoGET(msisdn, message, cli, null, null, null, null, null, null, null, null, null);
	}

	/* receive mo for Burkina Faso */
	@RequestMapping(value = "burkinafaso/receivemo", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String receivemoBrukinaFaso(@RequestBody String request) {
		Logger.sysLog(LogValues.info, this.getClass().getName(), "json received in case of mo is: " + request);
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonTree = jsonParser.parse(request);
		JsonElement inboundSMSMessageNotification = null;
		if (jsonTree.isJsonObject()) {
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			inboundSMSMessageNotification = jsonObject.get("inboundSMSMessageNotification");
			// System.out.println(inboundSMSMessageNotification.toString());
		}

		JsonElement jsonElement = jsonParser.parse(inboundSMSMessageNotification.toString());
		JsonElement inboundSMSMessage = null;
		if (jsonElement.isJsonObject()) {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			inboundSMSMessage = jsonObject.get("inboundSMSMessage");
			// System.out.println(inboundSMSMessage.toString());
		}

		JsonObject jsonObject = inboundSMSMessage.getAsJsonObject();

		String destinationAddress = null;
		String senderAddress = null;
		String message = null;

		String temp = null;
		int len = 0;

		temp = jsonObject.get("destinationAddress").toString();
		len = temp.length();
		destinationAddress = temp.substring(5, len - 1);

		temp = jsonObject.get("senderAddress").toString();
		len = temp.length();
		senderAddress = temp.substring(5, len - 1);

		temp = jsonObject.get("message").toString();
		len = temp.length();
		message = temp.substring(1, len - 1);

		receiveMoGET(senderAddress, message, destinationAddress, null, null, null, null, null, null, null, null, null);
		return "{204 No Content}";
	}
	/* end of receive mo for Burkina Faso */

	/**
	 * GET/POST Request Mapping, used by Operator to push DR via HTTP request
	 * 
	 * @throws JSONException
	 */
	@RequestMapping(value = "/postDRJson", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String receiveDrPOSTJson(HttpServletRequest request) throws JSONException {

		String sender = "";
		String status = "";
		String receiver = "";
		String serviceType = "";
		String messageId = "";
		String responseType = "";
		String message = "";
		try {
			ServletInputStream is = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String data = br.readLine();

			// if (data != null && data.indexOf("<soap") != -1) {
			// responseType = "soap";
			// message = data;
			// } else if (data != null && data.indexOf("<?xml") != -1) {

			JSONObject jobj = new JSONObject(data);

			sender = jobj.getString("sender");
			status = "";
			receiver = "";
			serviceType = "";
			messageId = "";
			responseType = "json";
			message = data;
			// }

		} catch (IOException e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					"Error in reading from input stream : " + Logger.getStack(e));
		}

		return this.receiveDrGET(sender, status, receiver, serviceType, responseType, message, messageId, null, null,
				null, null, null, null, request);
	}// End of Request Mapping

	/**
	 * GET/POST Request Mapping, used by Operator to push DR via HTTP request
	 */
	@RequestMapping(value = "/postDR", method = RequestMethod.POST, produces = "application/xml")
	public @ResponseBody String receiveDrPOST(@RequestParam(value = "sender", required = false) String sender,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "receiver", required = false) String receiver,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "responseType", required = false) String responseType,
			@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "messageId", required = false) String messageId, HttpServletRequest request) {

		try {
			ServletInputStream is = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String data = br.readLine();

			if (data != null && data.indexOf("<soap") != -1) {
				responseType = "soap";
				message = data;
			} else if (data != null && data.indexOf("<?xml") != -1) {
				responseType = "xml";
				message = data;
			}

		} catch (IOException e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					"Error in reading from input stream : " + Logger.getStack(e));
		}

		return this.receiveDrGET(sender, status, receiver, serviceType, responseType, message, messageId, null, null,
				null, null, null, null, request);
	}// End of Request Mapping

	@RequestMapping(value = "/receiveDR", method = RequestMethod.GET)
	public @ResponseBody String receiveDrGET(@RequestParam(value = "sender", required = false) String sender,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "receiver", required = false) String receiver,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "responseType", required = false) String responseType,
			@RequestParam(value = "message", required = false) String message,
			@RequestParam(value = "messageId", required = false) String messageId,
			@RequestParam(value = "_tid", required = false) String tid,
			@RequestParam(value = "status_id", required = false) String status_id,
			@RequestParam(value = "dtdone", required = false) String dtdone,
			@RequestParam(value = "errorcode", required = false) String errorcode,
			@RequestParam(value = "errordescription", required = false) String errordescription,
			@RequestParam(value = "extraDetail", required = false) String extraDetail, HttpServletRequest request) {

		ReceivedSmsBean cdr = new ReceivedSmsBean();

		try {
			cdr.setDeliveryReport(true);
			cdr.setTime(CoreUtils.getCurrentTimeStamp());
			String msisdn = "";

			if (CoreUtils.getProperty("country").equals("Indonesia") && CoreUtils.getProperty("operator").equals("XL")
					&& CoreUtils.getProperty("protocol").equals("HTTP")) {

				MtResponse mtresponse = null;
				String requestData = "";
				String cli = "";
				String serviceid = "";
				String subserviceid = "";
				String status1 = "";
				String action = "";

				if (tid != null) {

					mtresponse = SMSController.validation.getMTResponse(tid);

					msisdn = mtresponse.getMsisdn();
					cli = mtresponse.getCli();
					serviceid = mtresponse.getServiceid();
					subserviceid = mtresponse.getSubserviceid();

					if (status_id != null) { // Hitting DLR_provisoningUrl

						if (status_id.equals("102")) {
							status = "success";
							status1 = "active";
							action = "renew";
						}

						else {

							status = "failed";
							status1 = "grace";
							action = "grace";
						}

						sender = msisdn;
						receiver = cli;

						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"received tid, status_id , dtdone , errorcode , errordescription , sender , receiver: "
										+ tid + " , " + status_id + " , " + dtdone + " , " + errorcode + " , "
										+ errordescription + " , " + msisdn + " , " + cli);

						requestData = "&msisdn=" + msisdn + "&transactionID=" + tid + "&subServiceId=" + subserviceid
								+ "&status=" + status1 + "&action=" + action;
					}
				}

				else if (sender != null && extraDetail != null) { // Here extraDetail is subserviceId

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"Hitting in socket exception internally DR Called for msisdn : " + sender);

					status1 = "grace";
					action = "grace";
					requestData = "&msisdn=" + sender + "&transactionID=&subServiceId=" + extraDetail + "&status="
							+ status1 + "&action=" + action;
				}

				else {
					return "Failure";
				}

				String url = "";
				try {
					url = CoreUtils.getProperty("DLR_provisoningUrl");

					if (url != null && url.length() > 0) {

						url += requestData;

						Logger.sysLog(LogValues.info, this.getClass().getName(), "DLR_provisoningUrl: " + url);

						Message msg = new Message(receiver, sender, 0, "DR", 3, 1);
						HttpGateway g = new HttpGateway();
						g.sendGETRequest(url, msg);
					} else {
						Logger.sysLog(LogValues.error, this.getClass().getName(), "DLR_provisoningUrl hit null in db ");
					}
				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							"DLR_provisoningUrl hit false because of the satus: " + cdr.getStatus());
				}

			} else {
				Logger.sysLog(LogValues.info, this.getClass().getName(), "Delivery failed for user : " + msisdn);
				status = "failed";
			}

			if (responseType != null) {
				if (responseType.equalsIgnoreCase("xml") && !message.equals("")) {

					// itika: xml parse
					DeliveryInfoNotification notification = (DeliveryInfoNotification) XmlParser.parseXml(status,
							new DeliveryInfoNotification());
					cdr.setReceiverMsisdn("");
					cdr.setSender(notification.getDeliveryInfo().get(0).getAddress());
					cdr.setStatus(notification.getDeliveryInfo().get(0).getDeliveryStatus());
				} else if (responseType.equalsIgnoreCase("soap")) {
					int startIndex = status.indexOf("<ns2:deliveryStatus>");

					if (startIndex != -1)
						status = status.substring(startIndex, status.indexOf("</ns2:deliveryStatus>") + 23);

					if (!status.equals("")) {
						DeliveryInfo deliveryReport = (DeliveryInfo) XmlParser.parseXml(status, new DeliveryInfo());
						cdr.setReceiverMsisdn("");
						cdr.setSender(deliveryReport.getAddress());
						cdr.setStatus(deliveryReport.getDeliveryStatus());
					} else
						Logger.sysLog(LogValues.info, this.getClass().getName(), "DR received is empty!");
				}
			} else {

				cdr.setStatus(status);

				if (sender != null)
					cdr.setSender(sender.trim());
				if (receiver != null)
					cdr.setReceiverMsisdn(receiver.trim());
				if (serviceType != null)
					cdr.setServiceType(serviceType.trim());
				if (messageId != null)
					cdr.setMessageId(messageId.trim());
			}

			if (message == null)
				message = "";

			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" DeliveryReport Received :: [" + cdr.getSender() + "] :: status=" + cdr.getStatus());

			SmsValidation validator = new SmsValidation();
			validator.parseAndValidate(message, cdr);
		} catch (Exception e) {
		}
		return "Success";

	}// End of Request Mapping

	/* receive dr for Burkina Faso */
	@RequestMapping(value = "burkinafaso/receivedr", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String receivedrBrukinaFaso(@RequestBody String request) {
		Logger.sysLog(LogValues.info, this.getClass().getName(), "json received in the dr is: " + request);

		DeliveryInfoNotification deliveryInfoNotification = new DeliveryInfoNotification();
		Gson gson = new Gson();
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonTree = jsonParser.parse(request);
		JsonElement firstpart = null;
		if (jsonTree.isJsonObject()) {
			JsonObject jsonObject = jsonTree.getAsJsonObject();
			firstpart = jsonObject.get("deliveryInfoNotification");
		}
		deliveryInfoNotification = gson.fromJson(firstpart, DeliveryInfoNotification.class);
		String address = deliveryInfoNotification.getDeliveryInfo().get(0).getAddress().substring(4);
		String deliveryStatus = deliveryInfoNotification.getDeliveryInfo().get(0).getDeliveryStatus();

		ReceivedSmsBean cdr = new ReceivedSmsBean();

		cdr.setDeliveryReport(true);
		cdr.setTime(CoreUtils.getCurrentTimeStamp());
		cdr.setStatus(deliveryStatus);
		cdr.setSender(address);

		return "{204 No Content}";
	}
	/* end of receive dr for Burkina Faso */

	/**
	 * GET Request Mapping, used for pushing Test SMS
	 */
	@RequestMapping(value = "/testSMS")
	public @ResponseBody String pushTestSMS(@RequestParam("priority") Integer priority,
			@RequestParam("start") Integer start, @RequestParam(value = "count", required = false) Integer count,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "cli", required = false) String cli, HttpServletRequest request,
			HttpServletResponse resp) {

		Calendar cal = Calendar.getInstance();
		final String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));

		SmsQueue queue = new SmsQueue();

		Random rand = new Random();

		if (key != null && key.toLowerCase().contains("sms") == true && key.toLowerCase().contains(hour) == true) {

			if (cli == null || cli.length() == 0) {
				cli = "5606" + priority.toString();
			} else {
				cli = cli.trim() + priority.toString();
			}

			if (count == null) {
				count = 1;
			}

			int end = start + count;

			for (int i = start; i < end; i++) {

				long num = rand.nextLong();
				num = num < 0 ? num * -1 : num;

				String msisdn = "99" + String.valueOf(num).substring(0, 10);
				String number = priority.toString() + String.valueOf(i);

				Message msg = new Message(cli, msisdn, priority.intValue(), "Test Message " + number, 0, 0);
				msg.setEncoding(true);

				queue.push(msg);

			} // End Of Loop

			return " TEST SMS Pushed Successfully... ";
		} else {
			return " Sorry! Please provide password to push TEST SMS... ";
		}
	}// End Of Request Mapping

	/**
	 * To send Test MO to self
	 */
	@RequestMapping(value = "/sendTestMO", method = RequestMethod.GET)
	public @ResponseBody String receiveSms(@RequestParam("shortcode") String serviceCode,
			@RequestParam("content") String content, @RequestParam(value = "msisdn") String msisdn,
			@RequestParam(value = "circle", required = false) String circle,
			@RequestParam(value = "inactive", required = false) Boolean inactive) {

		String result = "NullAgrumentsFound";

		if (serviceCode != null && msisdn != null && content != null && serviceCode.length() > 0 && msisdn.length() > 0
				&& content.length() > 0) {

			try {

				Logger.sysLog(LogValues.info, this.getClass().getName(), " [TestMO] URL Hit for Receiving MO ");

				DeliverSm deliver = new DeliverSm();
				byte[] textBytes = content.trim().getBytes(Charset.forName("UTF-16BE"));

				deliver.setSourceAddr(msisdn);
				deliver.setDestAddress(serviceCode);
				deliver.setShortMessage(textBytes);
				deliver.setServiceType("bNgTestMO");
				deliver.setEsmClass((byte) 24);
				deliver.setEsmClass((byte) 8);

				boolean sessionInactive = false;

				if (inactive != null)
					sessionInactive = inactive.booleanValue();

				OptionalParameter op1 = new OptionalParameter.Byte(Tag.USSD_SERVICE_OP, (byte) 2);
				OptionalParameter op2 = null;

				if (sessionInactive) {
					op2 = new OptionalParameter.Int(Tag.ITS_SESSION_INFO, (int) 1);
				} else
					op2 = new OptionalParameter.Int(Tag.ITS_SESSION_INFO, (int) 2);

				deliver.setOptionalParameters(op1, op2);

				if (circle == null)
					circle = "TEST";

				SMPPMessageListener listner = new SMPPMessageListener(circle);

				listner.onAcceptDeliverSm(deliver);

				result = "Successful";

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
				result = "Un-Successful";
			}

		} // End Of IF

		return result;

	}// End Of Request Mapping

	@RequestMapping(value = "/testUssd", method = RequestMethod.GET)
	public @ResponseBody String testUssdRequest(@RequestParam(value = "cli") String shortcode,
			@RequestParam(value = "msisdn") String msisdn, @RequestParam(value = "input") String message,
			@RequestParam(value = "serviceOp") String serviceop, @RequestParam(value = "sessionId") String sessionId,
			@RequestParam(value = "type") String type) {

		String response = "Hello";

		return "response=" + response + "&sessionId=" + sessionId + "&serviceOp" + serviceop + "&sessionend=true";
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public @ResponseBody String test(@RequestBody String data, HttpServletRequest request, HttpServletResponse resp) {

		System.out.println("data: " + data);

		return "in controller";
	}

	public static String convertTexttoUnicode(String str, Integer script) {

		CoreEnums.LanguageScript language;
		String content;

		try {
			language = CoreEnums.LanguageScript.values[script];
		} catch (Exception e) {
			language = CoreEnums.LanguageScript.UNKNOWN;
		}

		try {

			String decodedContent;
			Logger.sysLog(LogValues.info, SMSController.class.getName(), " ReceivedContent= " + str);

			try {
				if (language.ordinal() != 0 && str.startsWith("%") == false
						&& (str.equalsIgnoreCase("%20") == false && str.equalsIgnoreCase(" ") == false)) {
					Logger.sysLog(LogValues.info, SMSController.class.getName(),
							" ayu :Before Encoding recieved Content = " + str);
					str = URLEncoder.encode(str, "UTF-8");
					Logger.sysLog(LogValues.info, SMSController.class.getName(), " ReceivedContent Encoded = " + str);
				}
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, SMSController.class.getName(), " Error " + Logger.getStack(e));
			}

			Logger.sysLog(LogValues.debug, SMSController.class.getName(), "Language= " + language.toString());

			if (language == CoreEnums.LanguageScript.ARABIC) {
				content = str.replaceAll("%C2", "").replaceAll("%C3%8", "%C").replaceAll("%C3%9", "%D")
						.replaceAll("%C3%A", "%E").replaceAll("%C3%B", "%F");
				decodedContent = URLDecoder.decode(content, "UTF-8");
			} else if (language == CoreEnums.LanguageScript.LATIN) {
				content = str.replaceAll("%83%C2", "");
				decodedContent = URLDecoder.decode(content, "UTF-8");
			} else if (language == CoreEnums.LanguageScript.HEX) {
				byte[] data = Hex.decodeHex(str.toCharArray());
				decodedContent = Hex.encodeHexString(data);
			} else
				decodedContent = str;

			Logger.sysLog(LogValues.info, SMSController.class.getName(), "DecodedText= " + decodedContent);
			return decodedContent;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SMSController.class.getName(),
					" ERROR Decoding SMS Content \n" + Logger.getStack(e));
			return str;
		} // End Of Try Catch

	}// End Of Method

	private static String fetchOptionalParamater(HttpServletRequest request, String paramName) {

		String paramValue = request.getParameter(paramName);

		if (paramValue != null && paramValue.length() > 0 && paramValue.equalsIgnoreCase("null") == false) {
			return paramValue.trim();
		}

		return "";
	}// End Of Method

	@SuppressWarnings("unused")
	private static Properties fetchAllOptionalParamaters(HttpServletRequest request) {
		// TODO (Get All Optional Parameters)
		return null;
	}// End Of Method

	@RequestMapping(value = "/getMOsAsJson", method = RequestMethod.GET)
	public @ResponseBody String getMOsAsJson() {

		List<MODetails> mos = SMSController.validation.getAllMOs();

		String json = "{\"mos\":";
		json += CoreUtils.GSON.toJson(mos);
		json += "}";

		return json;
	}// End Of Request Mapping

	@RequestMapping(value = "/bid", method = RequestMethod.GET)
	public @ResponseBody String bid(@RequestParam(value = "bid", required = true) String sBid,
			@RequestParam(value = "cli", required = false) String cli, @RequestParam("msisdn") String msisdn,
			@RequestParam(value = "priority", required = false) Integer msgType,
			@RequestParam("content") String content,
			@RequestParam(value = "validate", required = false) Boolean validate,
			@RequestParam(value = "unicode", required = false) Boolean unicode,
			@RequestParam(value = "type", required = false) Integer smsType,
			@RequestParam(value = "callback", required = false) Boolean callback,
			@RequestParam(value = "circle", required = false) String circle,
			@RequestParam(value = "reschedule", required = false) Boolean reschedule,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "dcs", required = false) Integer dataCoding,
			@RequestParam(value = "script", required = false) Integer script,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "expiry", required = false) Integer expiry,
			@RequestParam(value = "sync", required = false) Boolean sync,
			@RequestParam(value = "detail", required = false) String extraDetail,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "multiple", required = false) Boolean multiple,
			@RequestParam(value = "session", required = false) Boolean session,
			@RequestParam(value = "msgid", required = false) String msgid, // Added for Dream Travel Mexico (since MT
			// message required a special msgId)
			@RequestParam(value = "sessionEnd", required = false) Boolean sessionEnd, HttpServletRequest request)
			throws IOException {
		// bid = "bid 342"
		Logger.sysLog(LogValues.debug, SMSController.class.getName(), "bid: " + sBid);

		String regex = "[0-9]*\\.?[0-9]*";

		String msg = "";
		double max = Double.parseDouble(CoreUtils.getProperty("maxbid"));
		double min = Double.parseDouble(CoreUtils.getProperty("minbid"));

		String inRangeMsg = CoreUtils.getProperty("inrange");
		String outRangeMsg = CoreUtils.getProperty("outrange");
		String wrongKeyMessage = CoreUtils.getProperty("wrongkeymsg");
		String subCheckUrl = CoreUtils.getProperty("subCheckUrl");
		String subNoExistMsg = CoreUtils.getProperty("subNotExistMsg");

		if (!sBid.matches(regex)) {
			msg = wrongKeyMessage;
			Logger.sysLog(LogValues.info, SMSController.class.getName(),
					"Keyword not matched with any mapped keyword: " + sBid);
		} else {

			if (subCheckUrl != null && !subCheckUrl.trim().equals("")) {
				try {
					final Properties p = new Properties();
					p.putAll(request.getParameterMap());
					final SimpleParser sp = new SimpleParser(subCheckUrl);
					subCheckUrl = sp.parse(p);
					final String subCheckResponse = this.checkForActiveUser(msisdn, subCheckUrl);
					if (subCheckResponse.equalsIgnoreCase("new") || subCheckResponse.equalsIgnoreCase("unsub")) {
						subNoExistMsg = new SimpleParser(subNoExistMsg).parse(p);
						Logger.sysLog(LogValues.debug, SMSController.class.getName(), "Sub not exist for user: "
								+ msisdn + ", response: " + subCheckResponse + ", msg: " + subNoExistMsg);
						return this.addSmsToQueue(cli, msisdn, msgType, subNoExistMsg, validate, unicode, smsType,
								callback, circle, reschedule, serviceType, dataCoding, script, flag, expiry, sync,
								extraDetail, serviceid, multiple, session, msgid, sessionEnd, 0, false, null, null,
								null, 0, request);
					}
				} catch (Exception e) {
					Logger.sysLog(2, SMSController.class.getName(),
							"Error in parsing: " + Logger.getStack((Throwable) e));
					msg = wrongKeyMessage;
				}
			}

			double bid = Double.parseDouble(sBid.trim());
			Logger.sysLog(LogValues.debug, SMSController.class.getName(), "Bid in double: " + bid);
			if (bid < max && bid > min) {
				Logger.sysLog(LogValues.info, SMSController.class.getName(), "You are  in range " + bid);
				msg = inRangeMsg;

			} else {
				Logger.sysLog(LogValues.info, SMSController.class.getName(), "You are not in range " + bid);
				msg = outRangeMsg;

			}
		}

		return addSmsToQueue(cli, msisdn, msgType, msg, validate, unicode, smsType, callback, circle, reschedule,
				serviceType, dataCoding, script, flag, expiry, sync, extraDetail, serviceid, multiple, session, msgid,
				sessionEnd, 0, false, null, null, null, 0, request);

	}

	public String subscribehttp(final String msisdn, final String suburl) {
		final String urlString = suburl.replaceAll("<<msisdn>>", msisdn);
		Logger.sysLog(2, this.getClass().getName(), "Final Url For SUB  " + urlString);
		String resp = "Error";
		try {
			final URL obj = new URL(urlString);
			final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			final int responseCode = con.getResponseCode();
			final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			final StringBuffer response = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			resp = "" + (Object) response;
			Logger.sysLog(2, this.getClass().getName(), "url response   " + resp);
			resp = resp.trim();
			in.close();
		} catch (Exception ex) {
			Logger.sysLog(3, this.getClass().getName(), Logger.getStack((Throwable) ex));
		}
		return resp;
	}

	public String checkForActiveUser(final String msisdn, final String checkSubUrl) {
		Logger.sysLog(2, this.getClass().getName(), msisdn + " | Checking for user status for url : " + checkSubUrl);
		String userStatus = "new";
		final String checkSubResp = this.subscribehttp(msisdn, checkSubUrl);
		Logger.sysLog(2, this.getClass().getName(), msisdn + " | checkSubResp : " + checkSubResp);
		if (!checkSubResp.equalsIgnoreCase("error")) {
			final SubscriptionResponse subsResp = convertJsonStrToObject(checkSubResp, SubscriptionResponse.class);
			final String response = userStatus = subsResp.getCurrentStatus();
		}
		return userStatus;
	}

	public static synchronized <T> T convertJsonStrToObject(final String json, final Class<T> classOfT) {
		return (T) new Gson().fromJson(json, (Class) classOfT);
	}

	@RequestMapping(value = "/sendMatchAlert")
	public @ResponseBody String sendMatchAlert(@RequestParam(value = "cli", required = false) String cli,
			@RequestParam("subServiceId") String subServiceId, @RequestParam("matchId") int matchId,
			@RequestParam("matchStage") String matchStage,
			// @RequestParam("matchDetails") String matchDetails,
			@RequestParam(value = "priority", required = false) Integer msgType,
			@RequestParam(value = "validate", required = false) Boolean validate,
			@RequestParam(value = "unicode", required = false) Boolean unicode,
			@RequestParam(value = "type", required = false) Integer smsType,
			@RequestParam(value = "callback", required = false) Boolean callback,
			@RequestParam(value = "circle", required = false) String circle,
			@RequestParam(value = "reschedule", required = false) Boolean reschedule,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "dcs", required = false) Integer dataCoding,
			@RequestParam(value = "script", required = false) Integer script,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "expiry", required = false) Integer expiry,
			@RequestParam(value = "sync", required = false) Boolean sync,
			@RequestParam(value = "detail", required = false) String extraDetail,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "multiple", required = false) Boolean multiple,
			@RequestParam(value = "session", required = false) Boolean session,
			@RequestParam(value = "msgid", required = false) String msgid, // Added for Dream Travel Mexico (since MT
																			// message required a special msgId)
			@RequestParam(value = "sessionEnd", required = false) Boolean sessionEnd,
			@RequestParam(value = "delay", required = false) Integer delay,
			@RequestParam(value = "discard", required = false) Boolean discard, @RequestBody String matchDetails,
			HttpServletRequest request) throws IOException, JSONException {

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				"matchId " + matchId + " matchType: " + matchStage + "matchDetails: " + matchDetails);

		String result = "Failure";
		String content1 = null;

		if (matchId == 0 || matchStage == null)
			return result;

		MatchContent cricontent = matchContent.getMatchContent(matchId, matchStage);

		if (cricontent != null)
			content1 = cricontent.getContent();

		if (content1 == null)
			return result;

		Logger.sysLog(LogValues.info, this.getClass().getName(), "match content:: " + content1);

		String content = CoreUtils.parseMatchContent(content1, matchDetails);

		Logger.sysLog(LogValues.info, this.getClass().getName(), "parseContent:: " + content);

		List<SmsSubscription> msisdns = smsSubscriber.getSubUser(subServiceId);

		for (int k = 0; k < msisdns.size(); k++) {
			String msisdn = msisdns.get(k).getMsisdn();

			result = addSmsToQueue(cli, msisdn, msgType, content, validate, unicode, smsType, callback, circle,
					reschedule, serviceType, dataCoding, script, flag, expiry, sync, extraDetail, serviceid, multiple,
					session, null, sessionEnd, 0, false, null, null, null, 0, request);

		}
		return result;
	}

	@RequestMapping(value = "/TestReq", method = RequestMethod.GET)
	public @ResponseBody String TestPostReq(@RequestParam(value = "cli", required = false) String cli,
			HttpServletRequest request) throws IOException {

		return "{\"msisdn\":\"9911085687\",\"type\":\"quizscore\",\"score\":\"2\",\"timestamp\":\"2020-06-09 13:01:05.814\",\"duration\":\"4\"}";

	}// End Of Method

	/* Third party SMS API for libyana */
	@RequestMapping(value = "/sendsmsAPI", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody String thirdPartySendsmsAPI(@RequestBody String bodyData, HttpServletRequest request)
			throws IOException, JSONException {

		Logger.sysLog(LogValues.info, this.getClass().getName(), "json received in the dr is: " + request);

		JSONObject jo = new JSONObject(bodyData);

		HashMap<String, Object> yourHashMap = new Gson().fromJson(jo.toString(), HashMap.class);
		Properties p = new Properties();

		for (Entry<String, Object> entry : yourHashMap.entrySet()) {
			String val = "";
			if (entry.getValue() != null) {
				val = entry.getValue().toString();
			}
			p.put(entry.getKey(), val);
		}

		Logger.sysLog(LogValues.info, this.getClass().getName(), "properties: " + p);

		String msisdn = p.get("msisdn").toString();
		String cli = p.get("cli").toString();
		String content = p.get("content").toString();

		return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0, 0, 0, null,
				false, null, null, false, null, null, null, 0, false, null, null, null, 0, request);
	}
	/* end of Third party SMS API for libyana */

	@RequestMapping(value = "/sendQuizSms", method = RequestMethod.GET) // sending sms from gaming engine
	public @ResponseBody String sendQuizSms(@RequestParam(value = "msisdn", required = false) String msisdn,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "subserviceid", required = false) String subserviceid,
			@RequestParam(value = "questionString", required = false) String questionString,
			@RequestParam(value = "optionString", required = false) String optionString,
			@RequestParam(value = "txnId", required = false) String txnId,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "extraDetail", required = false) String extraDetail, HttpServletRequest request) {

		String result = "Failure";

		String cli = CoreUtils.getProperty("callerID");

		try {
			String content = validation.getQuestionFormat(msisdn, questionString, optionString);

			if (content == null) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						"msisdn [ " + msisdn + " ]" + "Issue with formation of sms with questionString [ "
								+ questionString + " ], optionString [ " + optionString + " ] ");
				return "Failure";
			} else {
				return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0, 0, 0,
						null, false, null, serviceid, false, null, txnId, null, 0, false, null, null, null, 0, request);
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), "Exception: " + Logger.getStack(e));
		}

		return result;
	}

	@RequestMapping(value = "/sendParsedMsg", method = RequestMethod.GET) // send sms with content parse
	public @ResponseBody String sendParsedMsg(@RequestParam(value = "msisdn", required = false) String msisdn,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "cli", required = false) String cli,
			@RequestParam(value = "subserviceid", required = false) String subserviceid,
			@RequestParam(value = "txnId", required = false) String txnId,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "module", required = false) String module,
			@RequestParam(value = "contentkey", required = false) String contentkey,
			@RequestParam(value = "extraDetail", required = false) String extraDetail, HttpServletRequest request) {

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				"sendParsedMsg API Request parameters: " + "msisdn , serviceid , "
						+ "cli , subserviceid , txnId , language , module , contentkey , extradetails --->" + msisdn
						+ " , " + serviceid + " , " + cli + " , " + subserviceid + " , " + txnId + " , " + language
						+ " , " + module + " , " + contentkey + " , " + extraDetail);
		String result = "Failure";
		String response = "";
		String parseValue = "";
		String content = "";
		String parseKey = "";
		msisdn = CoreUtils.stripCodes(msisdn);

		Message msg = new Message(CoreUtils.getProperty("gamingCallerId"), msisdn, 0, "answer", 3, 2);
		if (serviceid != null)
			msg.setServiceid(serviceid);

		if (cli == null)
			cli = CoreUtils.getProperty("gamingCallerId");

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Cli for gaming engine service is" + cli);

		if (subserviceid != null)
			msg.setSubserviceid(subserviceid);

		if (language != null)
			msg.setLang(language);

		HttpGateway g = new HttpGateway();

		try {
			if (module.equalsIgnoreCase("IVR")) {
				String url = CoreUtils.getProperty("ivr_quizScore");

				if (url != null && url.length() > 0) {

					String status = "";
					String substatus = "";

					Logger.sysLog(LogValues.info, this.getClass().getName(), "hitting url: " + url);
					response = g.sendSyncGETRequest(url, msg);

					JSONObject json = new JSONObject(response);

					if (json.getString("score") != "-1") {
						parseValue = json.getString("score");
						parseKey = "score";
					} else {
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"msisdn [ " + msisdn + " ] , score [ 0 ]");
						parseValue = "0";
					}

					content = validation.getContent(contentkey, language);
					parseKey = "$" + parseKey + "$";
					content = content.replace(parseKey, parseValue);
				}

				return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0, 0, 0,
						null, false, null, null, false, null, null, null, 0, false, null, null, null, 0, request);
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception: " + Logger.getStack(e));
		}

		return result;
	}

//	@RequestMapping(value = "/gamingchecksub", method = RequestMethod.GET )  //Subscribe user and then send question to user ( gaming engine)
//	public @ResponseBody String checksubandzdemo(
//			@RequestParam(value = "msisdn") String msisdn,
//			@RequestParam(value = "serviceid") String serviceid,
//			@RequestParam(value = "cli", required = false) String cli,
//			@RequestParam(value = "subserviceid", required = false) String subserviceid,
//			@RequestParam(value = "queid", required = false) String queid,
//			@RequestParam(value = "txnId", required = false) String txnId,
//			@RequestParam(value = "language") String language,
//			@RequestParam(value = "autoRenew", required = false) String autoRenew,
//			@RequestParam(value = "extraDetail", required = false) String extraDetail, HttpServletRequest request){
//		
//		boolean flag = true;
//		
//		String result = "Failure";
//		
//		if(cli == null)
//			cli = CoreUtils.getProperty("gamingCallerId");
//		try {
//			
//			String checjsubrrl = CoreUtils.getProperty("CheckSub_API");   // if sent wrong ans
//			if(checjsubrrl != null  && checjsubrrl.length() > 0) 
//			{
//				Logger.sysLog(LogValues.info, this.getClass().getName(), "CheckSub_API: " + checjsubrrl);
//			}
//			else
//			{
//				Logger.sysLog(LogValues.error, this.getClass().getName(), "Gaming Engine CheckSub_API not exist in sms_properties");
//			    return "Failure";
//			}
//			
//			
//			String chcksubUrl = "";
//			String response = "";
//			String subscribeResult = "failed";
//
//			Message msg = new Message(CoreUtils.getProperty("gamingCallerId"),msisdn,0,"answer", 3,2);
//			if(serviceid != null)
//				msg.setServiceid(serviceid);
//			
//			if(subserviceid != null)
//				msg.setSubserviceid(subserviceid);
//			
//			if(language != null)
//				msg.setLang(language);
//			
//			if(autoRenew != null)
//				msg.setMsgid(autoRenew);
//			
//			HttpGateway g = new HttpGateway();
//
//			try {
//				//checking ans result
//				chcksubUrl = CoreUtils.getProperty("CheckSub_API");
//
//				System.out.println("CheckSub_API"+chcksubUrl);
//				
//				if(chcksubUrl != null  && chcksubUrl.length() > 0) {
//
//					String status = "";
//					String substatus = "";
//
//					Logger.sysLog(LogValues.debug, this.getClass().getName(), "CheckSub_API: "+chcksubUrl );
//
//					response = g.sendSyncGETRequest(chcksubUrl, msg);
//
//					JSONObject json = new JSONObject(response);
//					
//					Logger.sysLog(LogValues.info, this.getClass().getName(), "user [ "+msisdn + " ]" +" gaming subscribe response "+json );
//
//					if(json.getString("subStatus").equalsIgnoreCase("NEW")) 
//					{
//
//				         System.out.println("new user");
//				         
//				         Logger.sysLog(LogValues.info, this.getClass().getName(),"new user");
//  	
//						     	
//								
//				         senddemoquestion(msisdn, serviceid, cli, subserviceid, queid, txnId, language, autoRenew, extraDetail, request);
//						         
//						         
//							}
//							
//							 
//				 		subscribeResult = "success";
//					} //
//					else {
//						Logger.sysLog(LogValues.info, this.getClass().getName(), "User subscription failed [ "+msisdn + " ]");
//						subscribeResult = "failed";
//					}
//			
//			}catch(Exception e) {
//				Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception: "+Logger.getStack(e) );
//			}
//
//			
//		}	
//		
//		catch (Exception e) {
//			// TODO: handle exception
//		}
//		return result;
//		
//	}

	@RequestMapping(value = { "/topupgame" }, method = { RequestMethod.GET })
	@ResponseBody
	public String topupGamingEngine(@RequestParam("msisdn") final String msisdn,
			@RequestParam("serviceid") final String serviceid,
			@RequestParam(value = "cli", required = false) String cli,
			@RequestParam(value = "subserviceid", required = false) final String subserviceid,
			@RequestParam(value = "queid", required = false) final String queid,
			@RequestParam(value = "language", required = false) final String language,
			@RequestParam(value = "txnId", required = false) final String txnId,
			@RequestParam(value = "autoRenew", required = false) final String autoRenew,
			@RequestParam(value = "extraDetail", required = false) final String extraDetail,
			final HttpServletRequest request) {
		boolean flag = true;
		final String result = "Failure";
		if (cli == null) {
			cli = CoreUtils.getProperty("gamingCallerId");
		}
		try {
			final String chcktopup = this.checkAvalilableTopup(msisdn, serviceid, subserviceid, request);
			Logger.sysLog(2, this.getClass().getName(), "Chcktopup Response " + chcktopup);
			if (chcktopup == "failed") {
				System.out.println("failed reponse ");
				request.setAttribute("language", (Object) language);
				return this.addSmsToQueue(cli, msisdn, 0, "Top Up Not Allowed", true, false, null, false, null, false,
						null, 0, 0, 0, null, false, null, null, false, null,
						request.getAttribute("language").toString(), null, 0, false, null, null, null, 0, request);
			}
			final String topupurl = CoreUtils.getProperty("Topup_API");
			if (topupurl == null || topupurl.length() <= 0) {
				Logger.sysLog(3, this.getClass().getName(), "Gaming Engine Subscribe_API not exist in sms_properties");
				return "Failure";
			}
			Logger.sysLog(2, this.getClass().getName(), "Topup_API: " + topupurl);
			final String userSubscribe = this.topupUser(msisdn, serviceid, subserviceid, autoRenew, language, request);
			if (userSubscribe.equalsIgnoreCase("success")) {
				if (CoreUtils.getProperty("Testing") != null
						&& CoreUtils.getProperty("Testing").equalsIgnoreCase("true")) {
					flag = false;
					if (CoreUtils.getProperty("QuizWhitelistNumbers") != null
							&& CoreUtils.getProperty("QuizWhitelistNumbers").length() > 0) {
						final String[] QuizWhitelistNumbersStr = CoreUtils.getProperty("QuizWhitelistNumbers")
								.split(",");
						for (int len = QuizWhitelistNumbersStr.length, i = 0; i < len; ++i) {
							if (msisdn.equals(QuizWhitelistNumbersStr[i])) {
								flag = true;
								break;
							}
						}
					}
				}
				if (!flag) {
					Logger.sysLog(2, this.getClass().getName(),
							"In testing, number is not whitelisted [ " + msisdn + " ]");
					return "Success";
				}
				final String content = this.fetchquestion(msisdn, serviceid, language, request);
				request.setAttribute("language", (Object) language);
				if (content == null || content.length() == 0) {
					Logger.sysLog(2, this.getClass().getName(), "Fetched null question");
					return "Failure";
				}
				return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0, 0, 0,
						null, false, null, null, false, null, request.getAttribute("language").toString(), null, 0,
						false, null, null, null, 0, request);
			} else {
				Logger.sysLog(2, this.getClass().getName(), "user topup failed [ " + msisdn + " ]");
			}
		} catch (Exception e) {
			Logger.sysLog(2, this.getClass().getName(), "Exception: " + Logger.getStack((Throwable) e));
		}
		return result;
	}

	@RequestMapping(value = { "/checktopupUser" }, method = { RequestMethod.GET })
	@ResponseBody
	public String checkAvalilableTopup(@RequestParam(value = "msisdn", required = false) final String msisdn,
			@RequestParam(value = "serviceid", required = false) final String serviceid,
			@RequestParam(value = "subserviceid", required = false) final String subserviceid,
			final HttpServletRequest request) throws IOException {
		final String topupUrl = "";
		String response = "";
		String chcktopupUrl = "failed";
		final Message msg = new Message(CoreUtils.getProperty("gamingCallerId"), msisdn, 0, "answer", 3, 2);
		if (serviceid != null) {
			msg.setServiceid(serviceid);
		}
		if (subserviceid != null) {
			msg.setSubserviceid(subserviceid);
		}
		final HttpGateway g = new HttpGateway();
		try {
			chcktopupUrl = CoreUtils.getProperty("CheckTopup_API");
			if (chcktopupUrl != null && chcktopupUrl.length() > 0) {
				final String status = "";
				final String substatus = "";
				Logger.sysLog(6, this.getClass().getName(), " CheckTopup_API: " + chcktopupUrl);
				response = g.sendSyncGETRequest(chcktopupUrl, msg);
				final JSONObject json = new JSONObject(response);
				Logger.sysLog(2, this.getClass().getName(),
						"user [ " + msisdn + " ]" + " gaming chck topup response " + json);
				if (json.getString("status").equalsIgnoreCase("SUCCESS")
						&& json.getString("subStatus").equalsIgnoreCase("SUCCESS")) {
					chcktopupUrl = "success";
				} else {
					Logger.sysLog(2, this.getClass().getName(), "User topup failed [ " + msisdn + " ]");
					chcktopupUrl = "failed";
				}
			}
		} catch (Exception e) {
			Logger.sysLog(3, this.getClass().getName(), "Exception: " + Logger.getStack((Throwable) e));
		}
		return chcktopupUrl;
	}

	@RequestMapping(value = { "/topupUser" }, method = { RequestMethod.GET })
	@ResponseBody
	public String topupUser(@RequestParam(value = "msisdn", required = false) final String msisdn,
			@RequestParam(value = "serviceid", required = false) final String serviceid,
			@RequestParam(value = "subserviceid", required = false) final String subserviceid,
			@RequestParam(value = "autoRenew", required = false) final String autoRenew,
			@RequestParam(value = "language", required = false) final String language, final HttpServletRequest request)
			throws IOException {
		String topupUrl = "";
		String response = "";
		String topupResult = "failed";
		final Message msg = new Message(CoreUtils.getProperty("gamingCallerId"), msisdn, 0, "answer", 3, 2);
		if (serviceid != null) {
			msg.setServiceid(serviceid);
		}
		if (subserviceid != null) {
			msg.setSubserviceid(subserviceid);
		}
		if (language != null) {
			msg.setLang(language);
		}
		if (autoRenew != null) {
			msg.setMsgid(autoRenew);
		}
		final HttpGateway g = new HttpGateway();
		try {
			topupUrl = CoreUtils.getProperty("Topup_API");
			if (topupUrl != null && topupUrl.length() > 0) {
				final String status = "";
				final String substatus = "";
				Logger.sysLog(6, this.getClass().getName(), "Topup_API: " + topupUrl);
				response = g.sendSyncGETRequest(topupUrl, msg);
				final JSONObject json = new JSONObject(response);
				Logger.sysLog(2, this.getClass().getName(),
						"user [ " + msisdn + " ]" + " gaming topup response " + json);
				if (json.getString("subStatus").equalsIgnoreCase("Unlocked")) {
					topupResult = "success";
				} else {
					Logger.sysLog(2, this.getClass().getName(), "User topup failed [ " + msisdn + " ]");
					topupResult = "failed";
				}
			}
		} catch (Exception e) {
			Logger.sysLog(3, this.getClass().getName(), "Exception: " + Logger.getStack((Throwable) e));
		}
		return topupResult;
	}

	@RequestMapping(value = "/sendQuestion", method = RequestMethod.GET) // Subscribe user and then send question to
																			// user ( gaming engine)
	public @ResponseBody String sendQuestion(@RequestParam(value = "msisdn") String msisdn,
			@RequestParam(value = "serviceid") String serviceid,
			@RequestParam(value = "cli", required = false) String cli,
			@RequestParam(value = "subserviceid", required = false) String subserviceid,
			@RequestParam(value = "queid", required = false) String queid,
			@RequestParam(value = "txnId", required = false) String txnId,
			@RequestParam(value = "language") String language,
			@RequestParam(value = "autoRenew", required = false) String autoRenew,
			@RequestParam(value = "extraDetail", required = false) String extraDetail, HttpServletRequest request) {

		boolean flag = true;

		String result = "Failure";

		if (cli == null)
			cli = CoreUtils.getProperty("gamingCallerId");
		try {

			String SubscribeUrl = CoreUtils.getProperty("Subscribe_API"); // if sent wrong ans
			if (SubscribeUrl != null && SubscribeUrl.length() > 0) {
				Logger.sysLog(LogValues.info, this.getClass().getName(), "SubscribeUrl: " + SubscribeUrl);
			} else {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						"Gaming Engine Subscribe_API not exist in sms_properties");
				return "Failure";
			}

			String userSubscribe;

			if (CoreUtils.getProperty("gatewaybilling") != null
					&& CoreUtils.getProperty("gatewaybilling").equalsIgnoreCase("true")) {

				userSubscribe = "success";

			}

			else {
				userSubscribe = subscribeQuizUser(msisdn, serviceid, subserviceid, language, autoRenew, request); // subscribing
																													// user
																													// before
																													// sending
																													// fetchquestion

			}

			if (userSubscribe.equalsIgnoreCase("success")) {

				if (CoreUtils.getProperty("Testing") != null
						&& CoreUtils.getProperty("Testing").equalsIgnoreCase("true")) {

					flag = false;
					if (CoreUtils.getProperty("QuizWhitelistNumbers") != null
							&& CoreUtils.getProperty("QuizWhitelistNumbers").length() > 0) {

						String[] QuizWhitelistNumbersStr = CoreUtils.getProperty("QuizWhitelistNumbers").split(",");
						int len = QuizWhitelistNumbersStr.length;

						for (int i = 0; i < len; i++) {
							if (msisdn.equals(QuizWhitelistNumbersStr[i])) {
								flag = true;
								break;
							}
						}
					}
				}

				if (!flag) {
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"In testing, number is not whitelisted [ " + msisdn + " ]");
					return "Success";
				}

				String content = fetchquestion(msisdn, serviceid, language, request);

				if (content == null || content.length() == 0) {
					Logger.sysLog(LogValues.info, this.getClass().getName(), "Fetched null question");
					return "Failure";
				} else {
					return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0,
							0, 0, null, false, null, null, false, null, null, null, 0, false, null, null, null, 0,
							request);
				}
			} else {
				Logger.sysLog(LogValues.info, this.getClass().getName(), "user subscription failed [ " + msisdn + " ]");
			}

		} catch (Exception e) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), "Exception: " + Logger.getStack(e));
		}

		return result;
	}

	@RequestMapping(value = "/QuizQueResponse", method = RequestMethod.GET) // For gaming engine quiz answer : AWCC
																			// Afganistan
	public @ResponseBody String quizQueResponse(@RequestParam(value = "msisdn") String msisdn,
			@RequestParam(value = "cli") String cli, @RequestParam(value = "serviceid") String serviceid,
			@RequestParam(value = "subserviceid", required = false) String subserviceid,
			@RequestParam(value = "answer") String answer,
			@RequestParam(value = "txnId", required = false) String txnId,
			@RequestParam(value = "language") String language,
			@RequestParam(value = "extraDetail", required = false) String extraDetail, HttpServletRequest request) {

		String result = "Failure";

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				"getting quiz answer => msisdn: " + msisdn + " , serviceid: " + serviceid + " , subserviceid: "
						+ subserviceid + " , answer: " + answer + " , txnId: " + txnId + " , extraDetail: "
						+ extraDetail + " , language: " + language);

		msisdn = CoreUtils.stripCodes(msisdn);
		String questionId = validation.getQuestionId(msisdn);

		System.out.println("question id =" + questionId);

		if (questionId == null) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), "QuestionId not found for user: " + msisdn);
			return "Failure";
		}

		String submitAnsResult = "";

		try {

			/*
			 * String content = ""; if(submitAnsResult.equalsIgnoreCase("success")) { String
			 * currectAns = CoreUtils.getProperty("currectAns"); // if sent correct ans
			 * if(currectAns != null && currectAns.length() > 0) {
			 * Logger.sysLog(LogValues.info, this.getClass().getName(), "user: "+ msisdn +
			 * " answer is correct for questionId: "+questionId); content = currectAns; } }
			 * else if(submitAnsResult.equalsIgnoreCase("failure")){
			 * 
			 * String wrongAnsContent = CoreUtils.getProperty("Wrong_Ans"); // if sent wrong
			 * ans if(wrongAnsContent != null && wrongAnsContent.length() > 0) {
			 * Logger.sysLog(LogValues.info, this.getClass().getName(), "user: "+ msisdn +
			 * " answer is wrong for questionId: "+questionId); content = wrongAnsContent; }
			 * } else { Logger.sysLog(LogValues.error, this.getClass().getName(),
			 * "not getting result for checkAnswer API of gaming engine"); return "Failure";
			 * }
			 * 
			 * this.addSmsToQueue(CoreUtils.getProperty("callerID"), msisdn, 0, content,
			 * true, false, null, false, null, false, null, 0, 0, 0, null, false, null,
			 * null, false, null,null, null, 0, false,null,null,null, request);
			 */

			// result = checkusereligibility(msisdn, serviceid,language, request);

			// check ans result and send message to user according to his/her eligibility
			submitAnsResult = checkAnsResult(msisdn, cli, answer, questionId, serviceid, language, request);

			String content = fetchquestion(msisdn, serviceid, language, request);

			if (content == null || content.length() == 0) {
				Logger.sysLog(LogValues.info, this.getClass().getName(), "Fetched null question");
				return "Failure";
			} else {
				return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0, 0, 0,
						null, false, null, null, false, null, null, null, 0, false, null, null, null, 0, request);
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Submit_Ans hit false : " + Logger.getStack(e));
		}
		return "success";
	}

	@RequestMapping(value = "/checkAnsResult", method = RequestMethod.GET)
	public @ResponseBody String checkAnsResult(@RequestParam(value = "msisdn") String msisdn,
			@RequestParam(value = "cli") String cli, @RequestParam(value = "answer") String answer,
			@RequestParam(value = "questionId", required = false) String questionId,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "language", required = false) String language, HttpServletRequest request)
			throws IOException {

		String submitAnsUrl = "";
		String response = "";
		String submitAnsResult = "";

		System.out.println("came here");

		Message msg = new Message(CoreUtils.getProperty("gamingCallerId"), msisdn, 0, "answer", 3, 2);
		if (serviceid != null)
			msg.setServiceid(serviceid);
		msg.setMessage(answer.toString());
		msg.setLang(language);

		HttpGateway g = new HttpGateway();

		String parseValue = null, parseKey = null;

		try {
			// checking ans result
			submitAnsUrl = CoreUtils.getProperty("Submit_Ans_API");

			System.out.println("came here 2");

			if (submitAnsUrl != null && submitAnsUrl.length() > 0) {

				System.out.println("came here 3");

				submitAnsUrl = submitAnsUrl.replace("$questionId$", questionId);
				String status = "";
				String substatus = "";

				Logger.sysLog(LogValues.info, this.getClass().getName(), "Submit_Ans: " + submitAnsUrl);

				response = g.sendSyncGETRequest(submitAnsUrl, msg);

				JSONObject json = new JSONObject(response);

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"user [ " + msisdn + " ]" + "Answer check response " + json);

				if (json.getString("status").equalsIgnoreCase("SUCCESS")) {

					if (json.getString("subStatus").equalsIgnoreCase("SUCCESS"))
						submitAnsResult = "success";
					else
						submitAnsResult = "failure";
					boolean prevAns = false;
					String content = "";

					if (submitAnsResult.equals("success")) {
						prevAns = true;

					}

					String url = CoreUtils.getProperty("ivr_quizScore");

					if (url != null && url.length() > 0) {

						String scoreapistatus = "";
						String scoreapisubstatus = "";

						Logger.sysLog(LogValues.info, this.getClass().getName(), "hitting url: " + url);
						response = g.sendSyncGETRequest(url, msg);

						JSONObject scoreapijson = new JSONObject(response);

						if (json.getString("score") != "-1") {
							parseValue = json.getString("score");
							parseKey = "score";
						} else {
							Logger.sysLog(LogValues.info, this.getClass().getName(),
									"msisdn [ " + msisdn + " ] , score [ 0 ]");
							parseValue = "0";
						}

					}

					if (!prevAns) {
						String wrongAnsContent = validation.getContent("Wrong_Ans_earlier", language); // if sent wrong
																										// ans
						if (wrongAnsContent != null && wrongAnsContent.length() > 0) {

							content = wrongAnsContent;
							parseKey = "$" + parseKey + "$";
							content = content.replace(parseKey, parseValue);
							return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false,
									null, 0, 0, 0, null, false, null, null, false, null, null, null, 0, false, null,
									null, null, 0, request);

						}
					} else {
						String correctAns = validation.getContent("correct_ans_msg", language);
						{// if sent correct ans
							if (correctAns != null && correctAns.length() > 0) {
								content = correctAns;

								parseKey = "$" + parseKey + "$";
								content = content.replace(parseKey, parseValue);
							}
							return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false,
									null, 0, 0, 0, null, false, null, null, false, null, null, null, 0, false, null,
									null, null, 0, request);
						}
					}
				}
			} else {
				Logger.sysLog(LogValues.error, this.getClass().getName(), "Submit_Ans API hit failed");
			}

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception: " + Logger.getStack(e));
		}

		return submitAnsResult;
	}// End Of Method

	@RequestMapping(value = "/subscribeQuizUser", method = RequestMethod.GET)
	public @ResponseBody String subscribeQuizUser(@RequestParam(value = "msisdn", required = false) String msisdn,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "subserviceid", required = false) String subserviceid,
			@RequestParam(value = "language", required = false) String language,
			@RequestParam(value = "autoRenew", required = false) String autoRenew, HttpServletRequest request)
			throws IOException {

		String subscribeUrl = "";
		String response = "";
		String subscribeResult = "failed";

		Message msg = new Message(CoreUtils.getProperty("gamingCallerId"), msisdn, 0, "answer", 3, 2);
		if (serviceid != null)
			msg.setServiceid(serviceid);

		if (subserviceid != null)
			msg.setSubserviceid(subserviceid);

		if (language != null)
			msg.setLang(language);

		if (autoRenew != null)
			msg.setMsgid(autoRenew);

		HttpGateway g = new HttpGateway();

		try {
			// checking ans result
			subscribeUrl = CoreUtils.getProperty("Subscribe_API");

			if (subscribeUrl != null && subscribeUrl.length() > 0) {

				String status = "";
				String substatus = "";

				Logger.sysLog(LogValues.debug, this.getClass().getName(), "Subscribe_API: " + subscribeUrl);

				response = g.sendSyncGETRequest(subscribeUrl, msg);

				JSONObject json = new JSONObject(response);

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"user [ " + msisdn + " ]" + " gaming subscribe response " + json);

				if (json.getString("subStatus").equalsIgnoreCase("ACTIVE")) {

					subscribeResult = "success";
				} else {
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"User subscription failed [ " + msisdn + " ]");
					subscribeResult = "failed";
				}
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception: " + Logger.getStack(e));
		}

		return subscribeResult;
	}// End Of Method

	@RequestMapping(value = "/fetchquestion", method = RequestMethod.GET)
	public @ResponseBody String fetchquestion(@RequestParam(value = "msisdn") String msisdn,
			@RequestParam(value = "serviceid") String serviceid, @RequestParam(value = "language") String language,
			HttpServletRequest request) throws IOException {

		String content = "";
		msisdn = CoreUtils.stripCodes(msisdn);

		Message msg = new Message(CoreUtils.getProperty("gamingCallerId"), msisdn, 0, "QuestionFetch", 3, 2);

		if (serviceid != null)
			msg.setServiceid(serviceid);

		if (language != null)
			msg.setLang(language);

		HttpGateway g = new HttpGateway();

		try {
			// finding next question
			String nextQueUrl = CoreUtils.getProperty("Next_Que_API");

			if (nextQueUrl != null && nextQueUrl.length() > 0) {

				Logger.sysLog(LogValues.debug, this.getClass().getName(), "nextQueUrl: " + nextQueUrl);

				String response = g.sendSyncGETRequest(nextQueUrl, msg);

				JSONObject nextqueRes = new JSONObject(response);

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"user [ " + msisdn + " ]" + " fetch question response " + nextqueRes);

				if (nextqueRes.getString("status") != null
						&& nextqueRes.getString("status").equalsIgnoreCase("SUCCESS")) {

					if (nextqueRes.getString("subStatus") != null
							&& nextqueRes.getString("subStatus").equalsIgnoreCase("Available")) {

						String question = nextqueRes.getString("questionString");
						String options = nextqueRes.getString("optionString");
						String getQue = validation.getQuestionFormat(msisdn, question, options);

						if (getQue != null) {
							content = getQue;
						}
					} else if (nextqueRes.getString("subStatus") != null
							&& nextqueRes.getString("subStatus").equalsIgnoreCase("EndOfQuestion")) {

						String maxLimitContent = validation.getContent("Max_Limit_Msg", language); // if played max
																									// limit for today
						if (maxLimitContent != null && maxLimitContent.length() > 0)
							content = maxLimitContent;
					} else {
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"user [ " + msisdn + " ] , substatus [ " + nextqueRes.getString("subStatus") + " ] ");
					}
				} else {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							"Getting issue with fetching next questiion from" + "gaining engine, sttaus [ "
									+ nextqueRes.getString("status") + " ]");
				}
			}

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception: " + Logger.getStack(e));
		}

		return content;
	}// End Of Method

//	@RequestMapping(value = "/demofetchquestion", method = RequestMethod.GET)
//	public @ResponseBody String demofetchquestion(@RequestParam(value = "msisdn") String msisdn,
//			@RequestParam(value = "serviceid") String serviceid,
//			@RequestParam(value = "language") String language,
//			HttpServletRequest request) throws IOException {
//
//		String content = "";
//		msisdn = CoreUtils.stripCodes(msisdn);
//		Message msg = new Message(CoreUtils.getProperty("gamingCallerId"),msisdn,0,"QuestionFetch", 3,2);
//		
//		
//		System.out.println("in demofetchquestion ");
//		
//		
//		
//		if(serviceid != null)
//			msg.setServiceid(serviceid);
//		
//		if(language != null)
//			msg.setLang(language);
//		
//		HttpGateway g = new HttpGateway();
//
//		try {
//			//finding next question
//			String nextQueUrl = CoreUtils.getProperty("Demo_Next_Que_API");
//
//			if(nextQueUrl != null  && nextQueUrl.length() > 0) {
//
//				Logger.sysLog(LogValues.debug, this.getClass().getName(), "nextQueUrl: "+nextQueUrl );
//
//				String response = g.sendSyncGETRequest(nextQueUrl, msg);
//
//				JSONObject nextqueRes = new JSONObject(response);
//				
//				Logger.sysLog(LogValues.info, this.getClass().getName(), "user [ "+msisdn + " ]" +" fetch question response "+nextqueRes );
//
//				if (nextqueRes.getString("status")!= null && nextqueRes.getString("status").equalsIgnoreCase("SUCCESS")) {
//
//					if (nextqueRes.getString("subStatus") != null && nextqueRes.getString("subStatus").equalsIgnoreCase("Available")) {
//
//						String question = nextqueRes.getString("questionString");
//						String options = nextqueRes.getString("optionString");
//						String getQue = validation.getQuestionFormat(msisdn, question , options);
//
//						if(getQue != null) 
//						{
//							content = getQue;
//						}
//					}
//					else if (nextqueRes.getString("subStatus") != null && nextqueRes.getString("subStatus").equalsIgnoreCase("EndOfQuestion")) {
//
//						String maxLimitContent = validation.getContent("Max_Limit_Msg" , language);   // if played max limit for today
//						if(maxLimitContent != null  && maxLimitContent.length() > 0) content = maxLimitContent;
//					}
//					else {
//						Logger.sysLog(LogValues.info, this.getClass().getName(), "user [ "+ msisdn + " ] , substatus [ " + 
//								nextqueRes.getString("subStatus") + " ] ");
//					}
//				}
//				else {
//					Logger.sysLog(LogValues.error, this.getClass().getName(), "Getting issue with fetching next questiion from"
//							+ "gaining engine, sttaus [ " + nextqueRes.getString("status") + " ]");
//				}
//			}
//
//		}catch(Exception e) {
//			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception: "+Logger.getStack(e) );
//		}
//
//		return content;
//	}// End Of Method
//	

	@RequestMapping(value = "/checkeligibility", method = RequestMethod.GET)
	public @ResponseBody String checkusereligibility(@RequestParam(value = "msisdn") String msisdn,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "language", required = false) String language, HttpServletRequest request)
			throws IOException {

		String content = "";
		String cli = CoreUtils.getProperty("callerID");

		Message msg = new Message(cli, msisdn, 0, "CheckEligibility", 3, 2);

		if (serviceid != null)
			msg.setServiceid(serviceid);

		if (language != null)
			msg.setLang(language);

		HttpGateway g = new HttpGateway();

		try {

			boolean prevAns = false;
			boolean quemaxlimit = false;
			String userEligibleUrl = "";
			int leftque = 0;

			// checking user eligibility
			userEligibleUrl = CoreUtils.getProperty("User_Eligibility_API");

			if (userEligibleUrl != null && userEligibleUrl.length() > 0) {

				String response = g.sendSyncGETRequest(userEligibleUrl, msg);

				JSONObject eligible_reponse = new JSONObject(response);

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"user [ " + msisdn + " ]" + " Eligibility response " + eligible_reponse);

				/*
				 * if (eligible_reponse.getString("status").equalsIgnoreCase("SUCCESS")) {
				 * 
				 * if (eligible_reponse.getString("isAnswerCorrect").equalsIgnoreCase("YES"))
				 * prevAns = true; else prevAns = false;
				 * 
				 * leftque = eligible_reponse.getInt("questionleft");
				 * 
				 * if(leftque == 0) //If Reached max limit quemaxlimit = true; } else {
				 * Logger.sysLog(LogValues.error, this.getClass().getName(),
				 * "Submit_Ans API hit failed" ); }
				 */

				if (!prevAns) {
					String wrongAnsContent = validation.getContent("Wrong_Ans_earlier", language); // if sent wrong ans
					if (wrongAnsContent != null && wrongAnsContent.length() > 0)
						content = wrongAnsContent;
					return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0,
							0, 0, null, false, null, null, false, null, null, null, 0, false, null, null, null, 0,
							request);
				} else if (prevAns) {
					String correctAns = validation.getContent("correct_ans_msg", language); // if sent correct ans
					if (correctAns != null && correctAns.length() > 0)
						content = correctAns;
					return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0,
							0, 0, null, false, null, null, false, null, null, null, 0, false, null, null, null, 0,
							request);
				} else if (quemaxlimit) {
					String maxLimitContent = validation.getContent("Max_Limit_Msg", language); // if played max limit
																								// for today
					if (maxLimitContent != null && maxLimitContent.length() > 0)
						content = maxLimitContent;
					return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null, false, null, 0,
							0, 0, null, false, null, null, false, null, null, null, 0, false, null, null, null, 0,
							request);
				}
			}

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception: " + Logger.getStack(e));
		}

		return "Failure";
	}// End Of Method

//	@RequestMapping(value = "/senddemoQuestion", method = RequestMethod.GET )  //Subscribe user and then send question to user ( gaming engine)
//	public @ResponseBody String senddemoquestion(
//			@RequestParam(value = "msisdn") String msisdn,
//			@RequestParam(value = "serviceid") String serviceid,
//			@RequestParam(value = "cli", required = false) String cli,
//			@RequestParam(value = "subserviceid", required = false) String subserviceid,
//			@RequestParam(value = "queid", required = false) String queid,
//			@RequestParam(value = "txnId", required = false) String txnId,
//			@RequestParam(value = "language") String language,
//			@RequestParam(value = "autoRenew", required = false) String autoRenew,
//			@RequestParam(value = "extraDetail", required = false) String extraDetail, HttpServletRequest request){
//		
//		boolean flag = true;
//		String result = "Failure";
//		
//		if(cli == null)
//			cli = CoreUtils.getProperty("gamingCallerId");
//		
//		HttpGateway g = new HttpGateway();
//
//       try {
//			
//			String demoquesurl = CoreUtils.getProperty("DemoQuestion_API");   // if sent wrong ans
//			if(demoquesurl != null  && demoquesurl.length() > 0) 
//			{
//				Logger.sysLog(LogValues.info, this.getClass().getName(), "demoquesurl: " + demoquesurl);
//			}
//			else 
//			{
//				Logger.sysLog(LogValues.error, this.getClass().getName(), "Gaming Engine demoquesurl not exist in sms_properties");
//			    return "Failure";
//			}
//			
//			Message msg = new Message(CoreUtils.getProperty("gamingCallerId"),msisdn,0,"QuestionFetch", 3,2);
//			
//			
//			if(demoquesurl != null  && demoquesurl.length() > 0) 
//			{
//
//				Logger.sysLog(LogValues.debug, this.getClass().getName(), "demoquesurl: "+demoquesurl );
//
//				String response = g.sendSyncGETRequest(demoquesurl, msg);
//
//				JSONObject nextqueRes = new JSONObject(response);
//				
//				Logger.sysLog(LogValues.info, this.getClass().getName(), "user [ "+msisdn + " ]" +" fetch question response "+nextqueRes );
//
//				if (nextqueRes.getString("status")!= null && nextqueRes.getString("status").equalsIgnoreCase("SUCCESS")) {
//
//					System.out.println("here you are");
//						String question = nextqueRes.getString("question");
//						String options = nextqueRes.getString("option");
//						String getQue = validation.getQuestionFormatforDemo(msisdn, question, options);
//						
//						 
//
//						if(getQue != null) 
//						{
//							System.out.println(getQue);
//							Logger.sysLog(LogValues.info, this.getClass().getName(),"  getQue"+getQue );
//							System.out.println("  getQue"+getQue );
//
//						}
//						else
//						{
//						System.out.println("getQue is null");
//						
//						}
//						
//						return this.addSmsToQueue(cli, msisdn, 0, getQue, true, false, null, false, null,
//								false, null, 0, 0, 0, null, false, null, null, false,
//								null,null, null, 0, false,null,null,null,0, request);
//						
//					}
//
//				
//	
//			}
//}
//			
//		catch (Exception e) {
//			{
//				
//				
//				
//			}
//			// TODO: handle exception
//		}
//		return result;
//		}
//	

//	@RequestMapping(value = "/QuizQueResponsedemo", method = RequestMethod.GET )    //For gaming engine quiz answer : AWCC Afganistan
//	public @ResponseBody String quizQueResponsedemo(
//			@RequestParam(value = "msisdn") String msisdn,
//			@RequestParam(value = "cli") String cli,
//			@RequestParam(value = "serviceid") String serviceid,
//			@RequestParam(value = "subserviceid", required = false) String subserviceid,
//			@RequestParam(value = "answer") String answer,
//			@RequestParam(value = "txnId", required = false) String txnId,
//			@RequestParam(value = "language") String language,
//			@RequestParam(value = "extraDetail", required = false) String extraDetail, HttpServletRequest request){
//
//		String result = "Failure";
//		
//		int qid=1;
//		
//		HttpGateway g = new HttpGateway();
//
//		Message msg = new Message(CoreUtils.getProperty("gamingCallerId"),msisdn,0,"QuestionFetch", 3,2);
//		
//		Logger.sysLog(LogValues.info, this.getClass().getName(), "getting quiz answer for demo => msisdn: " + msisdn + " , serviceid: " + serviceid + 
//				" , subserviceid: "+ subserviceid + " , answer: "+ answer + " , txnId: " + txnId + " , extraDetail: " + extraDetail + " , language: "+language);
//
//		msisdn = CoreUtils.stripCodes(msisdn);
//		String questionId = validation.getlastquestionfordemo(msisdn);
//		
//		
//		if(questionId == null) 
//		{
//			Logger.sysLog(LogValues.info, this.getClass().getName(), "QuestionId not found for user: "+ msisdn);
//			questionId="1";
//			
//		}
//		
//		else
//		{
//						
//			 qid=Integer.parseInt(questionId);  
//				
//			 qid=qid+1;
//			
//			 System.out.println("here i get qid "+qid);
//		}
//		
//		String submitAnsResult = "";
//
//		try {
//			
//			//submitAnsResult = checkDemoResult(msisdn,cli, answer, questionId , serviceid , language, request );
//			
//		
//		   String savedemouser = CoreUtils.getProperty("SaveDemoUser_API");
//		
//		if(savedemouser != null  && savedemouser.length() > 0) {
//			Logger.sysLog(LogValues.info, this.getClass().getName(), "SaveDemoUser_API: " + savedemouser);
//		}else 
//		{
//			Logger.sysLog(LogValues.error, this.getClass().getName(), "Gaming Engine savedemouser not exist in sms_properties");
//		    return "Failure";
//		}
//		
//		System.out.println("msisdn"+msisdn);
//		System.out.println("questionId"+questionId);
//		
//		
//		
//		savedemouser = savedemouser.replace("$userId$", msisdn);
//		savedemouser = savedemouser.replace("$questionId$", questionId);
//			
//		String saveresponse = g.sendSyncGETRequest(savedemouser, msg);
//		
//		
//		JSONObject saveuserRes = new JSONObject(saveresponse);
//		
//		Logger.sysLog(LogValues.info, this.getClass().getName(), "user [ "+msisdn + " ]" +" save demo response "+saveuserRes );
//
//		if (saveuserRes.getString("status")!= null && saveuserRes.getString("status").equalsIgnoreCase("SUCCESS")) 
//		{
//
//			
//			
//		String mydemoquesurl = CoreUtils.getProperty("DemoQuestion_API");   // if sent wrong ans
//		if(mydemoquesurl != null  && mydemoquesurl.length() > 0) {
//			Logger.sysLog(LogValues.info, this.getClass().getName(), "demoquesurl: " + mydemoquesurl);
//		}else {
//			Logger.sysLog(LogValues.error, this.getClass().getName(), "Gaming Engine demoquesurl not exist in sms_properties");
//		    return "Failure";
//		}
//		
//		
//		String nexyquestionid=String.valueOf(qid);
//
//		Logger.sysLog(LogValues.info, this.getClass().getName(),"nexyquestionid"+nexyquestionid);
//		if(mydemoquesurl != null  && mydemoquesurl.length() > 0) 
//		{
//
//			Logger.sysLog(LogValues.debug, this.getClass().getName(), "demoquesurl: "+mydemoquesurl );
//
//			System.out.println("nexyquestionid "+nexyquestionid);
//			//demoquesurl = demoquesurl.replace("$questionId$", "1");
//			String demoquesurl = mydemoquesurl.replace("1",nexyquestionid );
//			
//			System.out.println("demoquesurl "+demoquesurl);
//			
//			String response = g.sendSyncGETRequest(demoquesurl, msg);
//
//			JSONObject nextqueRes = new JSONObject(response);
//			
//			Logger.sysLog(LogValues.info, this.getClass().getName(), "user [ "+msisdn + " ]" +" fetch question response "+nextqueRes );
//
//			if (nextqueRes.getString("status")!= null && nextqueRes.getString("status").equalsIgnoreCase("SUCCESS")) 
//			{
//
//				System.out.println("here you are");
//					String question = nextqueRes.getString("question");
//					String options = nextqueRes.getString("option");
//					String getQue = validation.getQuestionFormatforDemo(msisdn, question, options);
//					
//					 
//
//					if(getQue != null) 
//					{
//						System.out.println(getQue);
//						Logger.sysLog(LogValues.info, this.getClass().getName(),"  getQue"+getQue );
//						System.out.println("  getQue"+getQue );
//
//					}
//					
//					else
//					{
//					System.out.println("getQue is null");
//					
//					}
//					
//					
//					if(getQue == null || getQue.length() ==0)
//					{
//						
//						Logger.sysLog(LogValues.info, this.getClass().getName(), "Fetched null question");
//						return "Failure";
//					}
//					else 
//					{
//						return this.addSmsToQueue(cli, msisdn, 0, getQue, true, false, null, false, null,
//								false, null, 0, 0, 0, null, false, null, null, false,
//								null,null, null, 0, false,null,null,null,0, request);
//					}
//					
//					
//					
//					
//					
//					
//			}
//			
//			
//			
//			
//			
//		}
//		
//		}
//			
//		}
//		catch(Exception e) {
//			Logger.sysLog(LogValues.error, this.getClass().getName(), "Submit_Ans hit false : "+ Logger.getStack(e));
//		}
//		return "success";
//	}
//	
//	
//	@RequestMapping(value = "/checkDemoResult", method = RequestMethod.GET)
//	public @ResponseBody String checkDemoResult(@RequestParam(value = "msisdn") String msisdn,
//			@RequestParam(value = "cli") String cli,
//			@RequestParam(value = "answer") String answer,
//			@RequestParam(value = "questionId", required = false) String questionId,
//			@RequestParam(value = "serviceid", required = false) String serviceid,
//			@RequestParam(value = "language", required = false) String language,
//			HttpServletRequest request) throws IOException {
//
//		String submitAnsUrl = "";
//		String response = "";
//		String submitAnsResult = "";
//		
//		Message msg = new Message(CoreUtils.getProperty("callerID"),msisdn,0,"answer", 3,2);
//		if(serviceid != null)
//			msg.setServiceid(serviceid);
//		msg.setMessage(answer.toString());
//		msg.setLang(language);
//		
//		HttpGateway g = new HttpGateway();
//
//		try {
//			//checking ans result
//			submitAnsUrl = CoreUtils.getProperty("CheckDemo_ANS_API");
//			
//			System.out.println("came here 2");
//
//			if(submitAnsUrl != null  && submitAnsUrl.length() > 0) {
//
//				System.out.println("came here 3");
//				
//				submitAnsUrl = submitAnsUrl.replace("$questionId$", questionId);
//				submitAnsUrl = submitAnsUrl.replace("$answer$", answer);
//				String status = "";
//				String substatus = "";
//
//				Logger.sysLog(LogValues.info, this.getClass().getName(), "CheckDemo_ANS_API: "+submitAnsUrl );
//
//				response = g.sendSyncGETRequest(submitAnsUrl, msg);
//
//				JSONObject json = new JSONObject(response);
//				
//				Logger.sysLog(LogValues.info, this.getClass().getName(), "user [ "+msisdn + " ]" +"Answer check response "+json );
//
//				if(json.getString("status").equalsIgnoreCase("SUCCESS")) {
//
//					if(json.getString("answer").equalsIgnoreCase("CORRECT"))  
//						submitAnsResult = "success";
//					else
//						submitAnsResult = "failure";
//					boolean prevAns = false;
//					String content = "";
//					if(submitAnsResult.equals("success")) {
//						prevAns = true;
//						
//					}
//					
//					if(!prevAns) {
//						String wrongAnsContent = validation.getContent("Wrong_Ans_earlier" , language);   // if sent wrong ans
//						if(wrongAnsContent != null  && wrongAnsContent.length() > 0) content = wrongAnsContent;
//						return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null,
//								false, null, 0, 0, 0, null, false, null, null, false,
//								null,null, null, 0, false,null,null,null,0, request);
//					}
//					else{
//						String correctAns = validation.getContent("correct_ans_msg" , language);   // if sent correct ans
//						if(correctAns != null  && correctAns.length() > 0) content = correctAns;
//						return this.addSmsToQueue(cli, msisdn, 0, content, true, false, null, false, null,
//								false, null, 0, 0, 0, null, false, null, null, false,
//								null,null, null, 0, false,null,null,null,0, request);
//					}
//				}
//				else {
//					Logger.sysLog(LogValues.error, this.getClass().getName(), "Submit_Ans API hit failed");
//				}
//		}
//		}catch(Exception e) {
//			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception: "+Logger.getStack(e) );
//		}
//
//		return submitAnsResult;
//	}// End Of Method
//	
//	

}// End Of Controller
