package com.rakesh.sms.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Time;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;

import org.hibernate.Session;

import org.jsmpp.PDUException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.beans.BlackoutHour;
import com.rakesh.sms.beans.Header;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.beans.Response;
import com.rakesh.sms.beans.SMSC;
import com.rakesh.sms.bo.MatchContentBo;
import com.rakesh.sms.bo.UtilityBo;
import com.rakesh.sms.boImpl.GatewayBoImpl;
import com.rakesh.sms.cdr.CdrCreator;
import com.rakesh.sms.cdr.ReceivedSmsBean;
import com.rakesh.sms.cdr.SmsCdrBean;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.daoImpl.DBConnection;
import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.MatchContent;
import com.rakesh.sms.entity.MessageFormats;
import com.rakesh.sms.entity.MsisdnSeries;
import com.rakesh.sms.entity.SMSBlacklist;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;
import com.rakesh.sms.entity.SmsSubscription;
import com.rakesh.sms.entity.MsgContents;
import com.rakesh.sms.main.HttpGateway;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.main.SmsValidation;
import com.bng.sms.queue.QueueManager;
import com.bng.sms.queue.SmsQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rakesh.sms.util.CoreEnums.ExpiryUnit;
import com.rakesh.sms.util.CoreEnums.Protocol;
import com.rakesh.sms.util.CoreEnums.SMSType;
import com.rakesh.sms.util.CoreEnums.Type;

public class CoreUtils {

	private static HashMap<String, LanguageSpecification> languageSpecs;
	private static HashMap<String, Integer> moFailureNotification;
	private static HashMap<String, SMSCConfigs> numberSeries;
	private static HashMap<String, String> extraParams;
	private static HashMap<String, String> properties;
	private static HashMap<String, ArrayList<String>> contents;
	private static List<BlackoutHour> blackoutHours;
	private static WhitelistCheck whitelistNumbers;
	private static BlacklistCheck blacklistNumbers;
	private static HttpGateway DefaultHttpGateway;
	private static SimpleDateFormat SDF;
	private static UtilityBo utilboimpl;

	private static Integer msisdnLength;
	public static String dbUsername;


	public static HashMap<String, ArrayList<String>> getContents() {
		return contents;
	}

	public static ArrayList<String> getContents(String key) {
		Logger.sysLog(LogValues.info, CoreUtils.class.getName()," Contents size for Random Serving: " + CoreUtils.contents.size());
		Logger.sysLog(LogValues.info, CoreUtils.class.getName()," Contents for Random Serving: " + CoreUtils.contents);
		ArrayList<String> value = CoreUtils.contents.get(key);
		return value;
	}// End Of Method

	public static void setContents(HashMap<String, ArrayList<String>> contents) {
		CoreUtils.contents = contents;
	}


	public static String dbPassword;
	public static Gson GSON, eGSON;
	public static String subsDbUrl;
	public static String token1;
	public static String token2;

	static {
		CoreUtils.eGSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		CoreUtils.SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		CoreUtils.moFailureNotification = null;
		CoreUtils.DefaultHttpGateway = null;
		CoreUtils.whitelistNumbers = null;
		CoreUtils.blacklistNumbers = null;
		CoreUtils.blackoutHours = null;
		CoreUtils.numberSeries = null;
		CoreUtils.GSON = new Gson();
		CoreUtils.msisdnLength = -1;
		CoreUtils.token1 = null;
		CoreUtils.token2 = null;
	}// End Of Static Block



	public void setUtilboimpl(UtilityBo utilboimpl) {
		CoreUtils.utilboimpl = utilboimpl;
	}

	public static void setProperties(HashMap<String, String> properties) {
		if (CoreUtils.properties != null) {
			CoreUtils.properties.clear();
			CoreUtils.properties = null;
		}
		CoreUtils.properties = properties;
	}

	public void setExtraParams(HashMap<String, String> extraParams) {
		if (CoreUtils.extraParams != null) {
			CoreUtils.extraParams.clear();
			CoreUtils.extraParams = null;
		}
		CoreUtils.extraParams = extraParams;
	}

	public void setSubsDbUrl(String subsDbUrl) {
		CoreUtils.subsDbUrl = subsDbUrl;
	}

	public void setDbUsername(String dbUsername) {
		CoreUtils.dbUsername = dbUsername;
	}

	public void setDbPassword(String dbPassword) {
		CoreUtils.dbPassword = dbPassword;
	}

	public static String getProperty(String key) {
		String value = CoreUtils.properties.get(key);
		Logger.sysLog(LogValues.debug, CoreUtils.class.getName(),
				" Returning Property:: ID=" + key + " |  Value=" + value);
		return value;
	}// End Of Method

	
	public static HttpGateway getDefaultHttpGateway() {

		try {
			if (CoreUtils.DefaultHttpGateway == null) {
				CoreUtils.DefaultHttpGateway = new HttpGateway();
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), " Unable to Initialize Default HTTP Gateway ");
		}

		return CoreUtils.DefaultHttpGateway;

	}// End Of Method

	public static void clear() {

		if (CoreUtils.blackoutHours != null)
			CoreUtils.blackoutHours.clear();

		if (CoreUtils.numberSeries != null)
			CoreUtils.numberSeries.clear();

		if (CoreUtils.extraParams != null)
			CoreUtils.extraParams.clear();

		if (CoreUtils.languageSpecs != null)
			CoreUtils.languageSpecs.clear();

		CoreUtils.properties.clear();

	}// End Of Method

	public static CoreEnums.Protocol getProtocol() {

		String protocol = CoreUtils.getProperty("protocol");

		if (protocol == null || protocol.trim().equals("") || protocol.toLowerCase().contains("null")) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
					" Unknown Protocol defined in Database :: " + protocol);
		} else {

			protocol = protocol.trim();

			for (int i = 0; i < CoreEnums.Protocol.values.length; i++) {

				if (CoreEnums.Protocol.values[i].toString().equalsIgnoreCase(protocol))
					return CoreEnums.Protocol.values[i];

			} // End Of Loop

		} // End Of IF Else

		return CoreEnums.Protocol.UNKNOWN;

	}// End Of Method

	public static String getRequiredMessage(SMSCFormats format, Message msg) {
		
		
		String request = null;

		try {

			String encoding = CoreUtils.getProperty("encoding");
			
			if (encoding == null || encoding.length() == 0) 
			{
				encoding = "UTF-8";
			}

			if (msg.getMode() == CoreEnums.Protocol.SMPP) 
			{

				String demand = msg.getEncoding();

				if (demand != null && demand.trim().equalsIgnoreCase("true"))
					msg.setEncoding(encoding);
				else
					msg.setEncoding("UTF-8");

				request = msg.getMessage();

			} else if (msg.getMode() == CoreEnums.Protocol.SOAP) {

				// request = format.getRequestFormat().replaceAll("<<<CallerID/>>>",
				// msg.getCli());
				// request = request.replace("<<<SendTo/>>>",
				// msg.getMsisdn()).replace("<<<MessageContent/>>>", msg.getMessage()).trim();
				if (msg.getEncoding() != null && msg.getEncoding().trim().equalsIgnoreCase("true"))
					msg.setEncoding(encoding);
				else
					msg.setEncoding("UTF-8");

				request = msg.getMessage();

			} else if (msg.getMode() == CoreEnums.Protocol.UNKNOWN) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
						" No RequestMessageFormat found for the defined protocol ");
				request = "N.A.";
			} else if (msg.getMode() == CoreEnums.Protocol.HTTP) 
			{
				System.out.println(msg.getEncoding());
				
				System.out.println(msg.getMessage());
				
				System.out.println(encoding);

				if (encoding.length() > 0
						&& (encoding.equalsIgnoreCase("HEXCODE") || encoding.equalsIgnoreCase("UNICODE")))
					request = CoreUtils.toHex(msg.getMessage());
				else if (encoding.length() > 0
						&& (encoding.equalsIgnoreCase("HEX")))
					request = CoreUtils.toHex1(msg.getMessage());
//				else if (msg.getEncoding() != null && msg.getEncoding().trim().equalsIgnoreCase("true"))
//					request = URLEncoder.encode(msg.getMessage(), encoding);
				else
					request = msg.getMessage();
			} else
				request = msg.getMessage();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), Logger.getStack(e));
		} // End Of Try Catch

		Logger.sysLog(LogValues.info, CoreUtils.class.getName(),
				" Encoding Used: " + msg.getEncoding() + " | EncodedMessage: " + request);

		return request;

	}// End Of Method

	public static String stripCodes(String msisdn) {
		
		Logger.sysLog(LogValues.info, CoreUtils.class.getName() ,"in stripCodes we get "+msisdn);

		if (msisdn != null) {
			try {

				if (CoreUtils.msisdnLength == -1) 
				{
					msisdnLength = Integer.parseInt(CoreUtils.getProperty("msisdnLength"));
				}
				
				//System.out.println("msisdn"+msisdn);

				msisdn = msisdn.trim();
				int length = msisdn.length();

				if (length >= msisdnLength) 
				{
					System.out.println("length >= msisdnLengt for "+msisdn);
					msisdn = msisdn.substring(length - msisdnLength);
					Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Stripped MSISDN is " + msisdn);
					System.out.println(" Stripped MSISDN is " + msisdn);
					Logger.sysLog(LogValues.debug, CoreUtils.class.getName(), " Stripped MSISDN is " + msisdn);
				} else {
					Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
							" MSISDN length is less than expected : " + msisdn);
					msisdn = null;
				}

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
						"Error Stripping Msisdn \n" + Logger.getStack(e));
				
				msisdn = null;
			} // End Of Try Catch
		} // End Of IF

		System.out.println("msisdn"+msisdn);
		return msisdn;

	}// End Of Method


	public static String stripcountryCodes(String msisdn) {

		int regioncodelength = Integer.parseInt(CoreUtils.getProperty("regionCodeLength"));
		Logger.sysLog(LogValues.debug, CoreUtils.class.getName(), msisdn);

		String countryCode = "*";

		if (msisdn != null) {
			try {

				if (CoreUtils.msisdnLength == -1) {
					msisdnLength = Integer.parseInt(CoreUtils.getProperty("msisdnLength"));
				}

				msisdn = msisdn.trim();
				int length = msisdn.length();

				if (length >= msisdnLength) {
					countryCode = msisdn.substring(0, regioncodelength);
					Logger.sysLog(LogValues.debug, CoreUtils.class.getName(), " Stripped COUNTRYCODE is " + countryCode);
				} else {
					Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
							" MSISDN length is less than expected : " + msisdn);
					msisdn = null;
				}

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
						"Error Stripping COUNTRYCODE \n" + Logger.getStack(e));
				msisdn = null;
			} // End Of Try Catch
		} // End Of IF

		return countryCode;

	}// End Of Method

	public static String getUnicode(String str) {

		BreakIterator characterIterator = BreakIterator.getWordInstance();
		StringBuffer unicode = new StringBuffer("");
		int boundary;

		try {

			characterIterator.setText(str);
			boundary = characterIterator.first();

			while (boundary != BreakIterator.DONE) {
				unicode.append("\\u");
				unicode.append(String.format("%04x", boundary).toUpperCase());
				boundary = characterIterator.next();
			}

			Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Generated Unicode: " + unicode.toString());

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), Logger.getStack(e));
		} // End Of Try Catch

		return unicode.toString().trim();

	}// End Of Method

	public static String toHex(String arg) {

		try {

			char[] arr = arg.toCharArray();
			StringBuffer hex = new StringBuffer();

			for (int i = 0; i < arr.length; i++) {

				if (arr[i] <= 127)
					hex.append(arr[i]);
				else
					hex.append("\\u").append(String.format("%04x", (int) arr[i]).toUpperCase());

			} // End Of Loop

			String hexcode = hex.toString().trim();
			Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Generated Hexcode: " + hexcode);
			return hexcode;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
					e.getMessage() + " :: Unable to Convert String to HEXCode ");
		}

		return "";

	}// End Of Method
	
	public static String toHex1(String str) {
	      StringBuffer sb = new StringBuffer();
	      //Converting string to character array
	      char ch[] = str.toCharArray();
	      for(int i = 0; i < ch.length; i++) {
	         String hexString = Integer.toHexString(ch[i]);
	         sb.append(hexString);
	      }
	      String result = sb.toString().toUpperCase();
	      return result;
	   }

	public static SmsCdrBean getSmsCDR(Message sms) {

		SmsCdrBean cdr = new SmsCdrBean();

		if (sms == null) {
			return null;
		}

		cdr.setSenderCli(sms.getCli());
		cdr.setReceiverMsisdn(sms.getMsisdn());
		cdr.setServiceType(sms.getServiceType());
		cdr.setType(sms.getUsage().toString());
		cdr.setMode(sms.getMode().toString());
		cdr.setMsgType("MT");
		cdr.setSubmittime(CoreUtils.getCurrentTimeStamp());

		String circle = sms.getCircle();
		if (circle != null && circle.length() > 0)
			cdr.setCircle(circle);

		String info = sms.getExtraDetail();
		if (info.length() > 0)
			cdr.setInfo(info);

		try {
			if (sms.getMessage().startsWith("%"))
				cdr.setContent(sms.getMessage());
			else
				cdr.setContent(URLEncoder.encode(sms.getMessage(), "UTF-16BE"));
		} catch (Exception e) {
			cdr.setContent(sms.getMessage());
		}

		return cdr;

	}// End Of Method

	public static ReceivedSmsBean getResponseCDR(Response resp) {

		ReceivedSmsBean cdr = new ReceivedSmsBean();
		cdr.setContent(resp.getText().trim());
		cdr.setMessageId(resp.getId());

		try {
			Date submitDate = new Date(resp.getSubmitDate());
			cdr.setTime(SDF.format(submitDate));
		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, CoreUtils.class.getName(), " Unable to Parse SubmitDate of Smsc Response ");
			cdr.setTime(CoreUtils.getCurrentTimeStamp());
		}

		return cdr;

	}// End Of Method

	public static String getCurrentTimeStamp() {
		return SDF.format(new Date());
	}// End Of Method

	public static String getTimeStamp(Date datetime) {
		return SDF.format(datetime);
	}// End Of Method

	public static String getTimeStamp(Long datetime) {
		return CoreUtils.getTimeStamp(new Date(datetime));
	}// End Of Method

	public static Date getDate(String timestamp) {
		return CoreUtils.getDate(timestamp, "");
	}// End of Method

	public static Date getDate(String timestamp, String extraLogDetail) {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			sdf.setLenient(true);
			return sdf.parse(timestamp);
		} catch (Exception e) {
			String eTimestamp = null;
			try {
				eTimestamp = URLEncoder.encode(timestamp, "UTF-8");
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
						extraLogDetail + " Error Parsing DateTime::: " + eTimestamp + " : " + Logger.getStack(e));
			} catch (UnsupportedEncodingException uee) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
						extraLogDetail + " Error Parsing DateTime:: " + timestamp + " : " + Logger.getStack(e));
			}
			return null;
		}
	}// End Of Method

	public static Properties convertSMSToProp(Object bean) {

		Properties prop = new Properties();
		Method[] methods = bean.getClass().getDeclaredMethods();

		for (Method method : methods) {

			if (method.getName().startsWith("get") || method.getName().startsWith("is")) {

				try {
					Object obj = method.invoke(bean);
					if (obj != null) {
						Logger.sysLog(LogValues.debug, CoreUtils.class.getName(),
								"@#@ Key= " + method.getName() + " | Value= " + obj);
						if (method.getName().startsWith("get"))
							prop.put(method.getName().substring("get".length()).toLowerCase(), obj);
						else if (method.getName().startsWith("is"))
							prop.put(method.getName().substring("is".length()).toLowerCase(), obj);
					}
				} catch (Exception e) {
					Logger.sysLog(LogValues.debug, CoreUtils.class.getName(),
							"Caught Exception while loading SMS to Properties." + e.getMessage());
				} // End Of Try Catch
			} // End Of If
		} // End Of Loop

		return prop;

	}// End Of Method

	public static void setCallbackDetails(Message msg, HttpServletRequest request) {

		String action = request.getParameter("action");
		String serviceid = request.getParameter("serviceID");
		String subserviceid = request.getParameter("subServiceId");
		String transactionId = request.getParameter("transactionID");
		String additional = request.getParameter("additionals");
		String channel = request.getParameter("channel");
		String price = request.getParameter("price");

		Logger.sysLog(LogValues.info, CoreUtils.class.getName(),
				"Callback details received: action=" + action + " | serviceID=" + serviceid + " | suberviceid="
						+ subserviceid + " | transactionId=" + transactionId + " price=" + price);

		if (msg.getUsage() == CoreEnums.Type.MO) {
			msg.getCallbackDetails().setShortcode(msg.getMsisdn());
		} else
			msg.getCallbackDetails().setShortcode(msg.getCli());

		if (serviceid != null && serviceid.length() > 0) {
			msg.getCallbackDetails().setServiceid(serviceid);
		} else {
			msg.getCallbackDetails().setServiceid("ERR123");
		}

		if (action != null)
			msg.getCallbackDetails().setAction(action);
		if (subserviceid != null)
			msg.getCallbackDetails().setSubServiceid(subserviceid);
		if (transactionId != null)
			msg.getCallbackDetails().setTransactionId(transactionId);
		if (additional != null)
			msg.getCallbackDetails().setAdditionals(additional);
		if (channel != null)
			msg.getCallbackDetails().setChannel(channel);
		if (price != null)
			msg.getCallbackDetails().setPrice(price);

	}// End Of Method

	public static void printProperties(Properties prop) {

		if (prop == null) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), " Properties found NULL ");
		} else {

			Enumeration<?> keys = prop.keys();
			String key, value;

			while (keys.hasMoreElements()) {
				key = String.valueOf(keys.nextElement());
				value = String.valueOf(prop.get(key));
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "Props: " + key + "= " + value);
			} // End Of Loop

		}

	}// End Of Method

	public static int getFailureRetries() {

		String retries = CoreUtils.getProperty("failureRetries");
		int retryCount = Message.DEFAULT_FAILURE_RETRIES;

		try {
			if (retries != null && retries.length() != 0)
				retryCount = Integer.parseInt(retries);
		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, CoreUtils.class.getName(),
					" Unable to Parse failureRetries :: Setting default value 1");
		}

		return retryCount;

	}// End Of Method

	public static long getRetryDuration() {

		String retryPeriod = CoreUtils.getProperty("retryPeriod");
		long duration = 15;

		try {
			if (retryPeriod != null && retryPeriod.length() != 0)
				duration = Long.parseLong(retryPeriod);
		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, CoreUtils.class.getName(),
					" Unable to Parse retryPeriod :: Setting default value as 15mins");
		}

		duration *= (60 * 1000); // To MilliSeconds
		return duration;

	}// End Of Method

	public static List<BlackoutHour> getBlackoutHours() {

		if (CoreUtils.blackoutHours == null) {
			CoreUtils.blackoutHours = CoreUtils.utilboimpl.getBlackoutHours();
		}
		return CoreUtils.blackoutHours;

	}// End Of Method

	public static void reloadBlackoutHours() {

		if (CoreUtils.blackoutHours != null) {
			CoreUtils.blackoutHours.clear();
			CoreUtils.blackoutHours = null;
		}

		CoreUtils.blackoutHours = CoreUtils.utilboimpl.getBlackoutHours();

	}// End Of Method

	public static boolean isBlackoutHours() {
		return CoreUtils.withinBlackoutHours(new Date());
	}// End Of Method

	public static boolean withinBlackoutHours(Date timestamp) {

		List<BlackoutHour> bhList = CoreUtils.getBlackoutHours();

		if (bhList != null && timestamp != null) {

			for (int i = 0; i < bhList.size(); i++) {
				BlackoutHour bh = bhList.get(i);

				if (bh.getStart().before(timestamp) && bh.getEnd().after(timestamp)) {
					return true;
				}

			} // End Of Loop

		} // End Of IF

		return false;

	}// End Of Method

	public static Date getEndOfCurrentBlackoutHour() {

		List<BlackoutHour> bhList = CoreUtils.getBlackoutHours();
		Date timestamp = new Date();

		if (bhList != null) {

			for (int i = 0; i < bhList.size(); i++) {
				BlackoutHour bh = bhList.get(i);

				if (bh.getStart().before(timestamp) && bh.getEnd().after(timestamp))
					return bh.getEnd();

			} // End Of Loop

		} // End Of IF

		return null;

	}// End Of Method

	public static String parseUrl(String url, Object obj, Properties p) {

		if (url != null) {

			try {

				//System.out.println("url:"+url+ "addition properties: " + p);
				
				//System.out.println("url:"+url+ "addition properties: " + p);
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(),"url:"+url+ "addition properties: " + p);

				SimpleParser simpleParser = new SimpleParser(url.trim());
				Properties prop = CoreUtils.convertSMSToProp(obj);

				if(p!=null) prop.putAll(p);
				
			
				
				String msisdn = prop.get("msisdn").toString();
				Logger.sysLog(LogValues.info, CoreUtils.class.getName() ,"msisdn i get here"+msisdn);
				String msisdnWithoutCc = CoreUtils.stripCodes(msisdn);
				prop.put("msisdnwithoutcc", msisdnWithoutCc);

				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Properties in parseurl" + prop);
				
			//	System.out.println( " Properties in parseurl" + prop);
				

				if (obj instanceof Message) {

					Message msg = (Message) obj;
					
					Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "corelatorId: " + msg.getCorelatorId() + " senderName: " + msg.getSenderName());//BurkinaFaso

					if (msg.getCallbackDetails() != null)
						prop.putAll(CoreUtils.convertSMSToProp(msg.getCallbackDetails()));

				} // End Of instanceof check

				String parsedURL = simpleParser.parse(prop);
				// CoreUtils.printProperties(prop);
				prop.clear();
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), url + ", New Parsed Url: " + parsedURL);
				
			//	System.out.println( url + ", New Parsed Url: " + parsedURL);
				
			//	System.out.println("New Parsed Url: " + parsedURL);
				
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Properties in parseurl" + prop);
				
				//System.out.println("New Parsed Url: " + parsedURL);
				
				return parsedURL;

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
						"Error Parsing URL :: " + url + " \n" + Logger.getStack(e));
			} // End Of Try Catch

		} else if (obj == null) {
			return url;
		} else {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), " No URL to Parse ");
		}

		return "";

	}// End Of Method

	
	
	
	
	public static String parsercvdrUrl(String url, Object obj, Properties p) {

		if (url != null) {

			try {

			//	System.out.println("url:"+url+ "addition properties: " + p);
				
				//System.out.println("url:"+url+ "addition properties: " + p);
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(),"url:"+url+ "addition properties: " + p);

				SimpleParser simpleParser = new SimpleParser(url.trim());
				Properties prop = CoreUtils.convertSMSToProp(obj);

				if(p!=null) prop.putAll(p);
				
				String tid= prop.get("transactionID").toString();
			//	System.out.println("here"+tid);
				
				String action= prop.get("action").toString();
			//	System.out.println("here"+action);
				
				String sta= prop.get("status").toString();
				//System.out.println("here"+sta);

				String msisdn = prop.get("msisdn").toString();
				String msisdnWithoutCc = CoreUtils.stripCodes(msisdn);
				prop.put("msisdnwithoutcc", msisdnWithoutCc);

				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Properties in parseurl" + prop);
				
			//	System.out.println( " Properties in parseurl" + prop);
				

				if (obj instanceof Message) {

					Message msg = (Message) obj;
					
					Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "corelatorId: " + msg.getCorelatorId() + " senderName: " + msg.getSenderName());//BurkinaFaso

					if (msg.getCallbackDetails() != null)
						prop.putAll(CoreUtils.convertSMSToProp(msg.getCallbackDetails()));

				} // End Of instanceof check

				String parsedURL = simpleParser.parse(prop);
				// CoreUtils.printProperties(prop);
				prop.clear();
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), url + ", New Parsed Url: " + parsedURL);
				
				
				//System.out.println("New Parsed Url: " + parsedURL);
				
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Properties in parseurl" + prop);
				
				//System.out.println("New Parsed Url: " + parsedURL);
				
				return parsedURL;

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
						"Error Parsing URL :: " + url + " \n" + Logger.getStack(e));
			} // End Of Try Catch

		} else if (obj == null) {
			return url;
		} else {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), " No URL to Parse ");
		}

		return "";

	}// End Of Method


	
	
	
	public static List<Header> parseHeader(List<Header> headers, Object obj,Properties p) {

		if (headers != null && !headers.isEmpty()) { 

			List<Header> parsedHeader=new ArrayList<Header>();
			Properties prop = CoreUtils.convertSMSToProp(obj);
			if(p!= null)prop.putAll(p);

			try {
				if (obj instanceof Message) {

					Message msg = (Message) obj;

					if (msg.getCallbackDetails() != null)
						prop.putAll(CoreUtils.convertSMSToProp(msg.getCallbackDetails()));

				} // End Of instanceof check

				//				for(Header h:headers) {
				//					SimpleParser simpleParser = new SimpleParser(h.getValue());
				//					String parsedValue = simpleParser.parse(prop);
				//					h.setValue(parsedValue);
				//					parsedHeader.add(h);
				//					
				//				}


				Iterator<Header> iterator = headers.iterator();
				while(iterator.hasNext()) {
					Header h = iterator.next();
					SimpleParser simpleParser = new SimpleParser(h.getValue());
					String parsedValue = simpleParser.parse(prop);
					h.setValue(parsedValue);
					parsedHeader.add(h);
				}


				Logger.sysLog(LogValues.info, SmsValidation.class.getName(), " Properties in parseheader" + parsedHeader.toString());


				// CoreUtils.printProperties(prop);
				prop.clear();
				return parsedHeader;

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
						"Error Parsing header :: " + headers.toString() + " \n" + Logger.getStack(e));
			} // End Of Try Catch

		} else if (headers == null) {
			return headers;
		} else {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), " No URL to Parse ");
		}

		return headers;

	}// End Of Method


	/**
	 * Method Specially used to convert MO Message CDR to MT SMS bean, used in
	 * defined MO Actions
	 */
	public static Message createMTfromMOcdr(ReceivedSmsBean cdr) {

		Message sms = new Message();

		sms.setMsisdn(cdr.getSender());
		sms.setCli(cdr.getReceiverMsisdn());
		sms.setServiceType(cdr.getServiceType());
		sms.setMessage(cdr.getContent());
		sms.setType(SMSType.UNKNOWN);
		sms.setMode(Protocol.SMPP);
		//sms.setUsage(Type.MT);
		sms.setUsage(Type.MO);    //For XL Indonesia it is MO 
		sms.setMsgid(cdr.getMsgid()); //Added for Dream Travel Mexico (since MT message required a special msgId)
		
		sms.setSource(cdr.getMode());   //Elsalvador

		sms.setCircle(cdr.getCircle());
		sms.setTime(System.currentTimeMillis());

		return sms;

	}// End Of Method

	public static void loadNumberSeries() {

		List<MsisdnSeries> series = CoreUtils.utilboimpl.getNumberSeriesDetails();

		if (CoreUtils.numberSeries == null)
			CoreUtils.numberSeries = new HashMap<String, SMSCConfigs>();
		else
			CoreUtils.numberSeries.clear();

		for (int i = 0; i < series.size(); i++) {

			MsisdnSeries ms = series.get(i);
			String numb = ms.getSeries();
			SMSCConfigs smsc = ms.getCircle();

			if (numb != null && numb.length() > 0 && smsc != null)
				CoreUtils.numberSeries.put(numb.trim(), smsc);

		} // End Of Loop

	}// End Of Method




	public static void loadMsgContents() {

		List<MsgContents> series = CoreUtils.utilboimpl.getMsgContentsData();

		if (CoreUtils.contents == null)
			CoreUtils.contents = new HashMap<String, ArrayList<String>>();
		else
			CoreUtils.contents.clear();

		for (int i = 0; i < series.size(); i++) {

			MsgContents mc = series.get(i);
			String key = mc.getService()+"_"+mc.getEvent();

			if(contents.containsKey(key.trim())==false) {
				ArrayList<String> str = new ArrayList<String>();
				str.add(mc.getContent());
				CoreUtils.contents.put(key.trim(), str );
			} else {
				//				ArrayList<String> str = contents.get(key.trim());
				//				str.add(mc.getContent());
				//				CoreUtils.contents.replace(key.trim(), str );

				contents.get(key.trim()).add(mc.getContent());
			}



		} // End Of Loop

	}

	public static SMSCConfigs getCircleForNumberSeries(String msisdn) {

		SMSCConfigs smsc = null;
		int seriesLength = 0;

		try {
			seriesLength = Integer.parseInt(CoreUtils.getProperty("msisdnSeriesLength"));
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
					" Error Parsing global.sms_properties.msisdnSeriesLength :: Default Value = 3");
			seriesLength = 3;
		}

		String msisdnWithoutCodes = CoreUtils.stripCodes(msisdn);

		if (msisdnWithoutCodes != null) {

			String series = msisdnWithoutCodes.substring(0, seriesLength);

			if (CoreUtils.numberSeries == null || CoreUtils.numberSeries.isEmpty())
				CoreUtils.loadNumberSeries();

			if (series != null && CoreUtils.numberSeries.containsKey(series))
				smsc = CoreUtils.numberSeries.get(series);

		} // End Of IF

		return smsc;

	}// End Of Method

	public static void pushAlertToSmsQueue(Message sms) {

		try {

			if (sms != null) {
				SmsQueue queue = new SmsQueue();
				queue.push(sms);
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Alert SMS Pushed :: " + sms.toString());
			}

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
					" Unable to Push SMSAlert into Queue \n" + Logger.getStack(e));
		} // End Of Try Catch

	}// End Of Method

	public static void pushAlertsToSmsQueue(List<Message> alerts) {

		try {

			if (alerts.size() > 0) {

				SmsQueue queue = new SmsQueue();

				for (int i = 0; i < alerts.size(); i++) {
					Message sms = alerts.get(i);
					queue.push(sms);
					Thread.sleep(1000); // added by rk
					Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " Alert SMS Pushed :: " + sms.toString());
					
				} // End Of Loop

				alerts.clear();
			} else
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " No Alert Message to Push into Queue ");

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
					" Unable to Push SMSAlerts into Queue \n" + Logger.getStack(e));
		} // End Of Try Catch

	}// End Of Method

	public static void retrySmsNow(Message sms) {
		CoreUtils.retrySms(sms, false, 60000);
	}// End Of Method

	public static void retrySms(Message sms) {
		CoreUtils.retrySms(sms, false);
	}// End Of Method

	public static void retrySms(Message sms, boolean forcefulRetry) {
		CoreUtils.retrySms(sms, false, CoreUtils.getRetryDuration());
	}// End Of Method

	public static void retrySms(Message sms, boolean forcefulRetry, long retryPeriod) {

		try {

			if (sms.getEncoding() != null && sms.getEncoding().equalsIgnoreCase("UTF-8") == true) {
				sms.setEncoding("false");
			} else if (sms.getEncoding() != null && sms.getEncoding().equalsIgnoreCase("false") == false) {
				sms.setEncoding("true");
			} else {
				sms.setEncoding("false");
			}

			if (forcefulRetry == true || (sms.isReschedule() == false && sms.getRemainingRetries() > 0)) {
				SmsQueue queue = new SmsQueue();
				sms.decreaseRetry();
				queue.push(sms, retryPeriod);
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(),
						" On Submit SMS Failure, Retry scheduled after " + (long) (retryPeriod / (60000)) + " mins :: "
								+ sms.toString());
			} else {

				Logger.sysLog(LogValues.info, CoreUtils.class.getName(),
						" Forceful Retry= " + forcefulRetry + "  |  Max SMS Retry reached... ");

				if (sms.isCallback()) {
					sms.getCallbackDetails().setCallbackStatus("Failure");
					sms.getCallbackDetails().setFailureReason("Others");
					Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "Sending Callback..." + sms.toString());
					CoreUtils.sendCallback(sms);
				}

				SmsCdrBean cdr = CoreUtils.getSmsCDR(sms);
				cdr.setMessageId(SMSController.DefaultMessageID);
				cdr.setStatus("Failure");
				CdrCreator.saveAsXML(cdr);

			} // End Of Retry

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(), " Unable to Retry SMS \n" + Logger.getStack(e));
		}

	}// End Of Method

	public static void sendCallback(Message msg) {

		String callbackUrl = CoreUtils.getProperty("callbackUrl");

		if (callbackUrl != null) {

			HttpGateway gateway = Pusher.getHttpGateway(msg.getCircle());

			if (gateway != null) {
				gateway.sendGETRequest(callbackUrl, msg,null);
			} else {
				CoreUtils.getDefaultHttpGateway().sendGETRequest(callbackUrl, msg,null);
			}

		} else
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
					" Error Sending Callback | global.sms_properties.callbackUrl Not defined ");

	}// End Of Method

	public static void printByteArray(String str, String encoding) {

		StringBuffer bytesConcatenated = new StringBuffer("");
		byte[] messageInBytes;

		try {
			messageInBytes = str.getBytes(encoding);
		} catch (Exception e) {
			messageInBytes = str.getBytes();
		}

		for (int i = 0; i < messageInBytes.length; i++) {
			bytesConcatenated.append(String.valueOf(messageInBytes[i]));
			bytesConcatenated.append(" ");
		}

		Logger.sysLog(LogValues.info, CoreUtils.class.getName(),
				" Printing Byte Array(" + messageInBytes.length + ") = " + bytesConcatenated.toString());

	}// End Of Method

	public static void printByteArray(String str) {

		CoreUtils.printByteArray(str, "UTF-8");

	}// End Of Method

	public static String getExtraParam(String key) {

		if (CoreUtils.extraParams != null && CoreUtils.extraParams.containsKey(key)) {
			return CoreUtils.extraParams.get(key);
		}

		return "";

	}// End Of Method

	public static ExpiryUnit getExpiryUnit() {

		String expiryUnit = CoreUtils.getExtraParam("smsExpiryUnit");

		if (expiryUnit.length() > 0) {
			for (int i = 0; i < ExpiryUnit.values().length; i++) {

				if (ExpiryUnit.values[i].toString().toLowerCase().contains(expiryUnit)) {
					return ExpiryUnit.values[i];
				}

			} // End Of Loop
		} // End Of IF

		return ExpiryUnit.UNKNOWN;

	}// End Of Method

	public static Date getExpiryTime(Integer offset) {
		return CoreUtils.getExpiryTime(offset, CoreUtils.getExpiryUnit());
	}// End Of Method

	public static Date getExpiryTime(Integer offset, ExpiryUnit unit) {

		Calendar cal = Calendar.getInstance();

		switch (unit) {

		case DATE:
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
			break;
		case DAYS:
			cal.add(Calendar.DAY_OF_MONTH, offset);
			break;
		case HOURS:
			cal.add(Calendar.HOUR_OF_DAY, offset);
			break;
		case MINS:
			cal.add(Calendar.MINUTE, offset);
			break;
		default:
			return null;

		}// End Of Switch Case

		return cal.getTime();

	}// End Of Method

	public static boolean isArabic(String str) {

		int length = str.length();

		for (int i = 0; i < length; i++) {

			final char c = str.charAt(i);

			if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.ARABIC) {
				return true;
			}

		} // End Of Loop

		return false;

	}// End Of Method

	public static boolean containsSpecialCharacters(String str) {

		if (str == null) {
			return false;
		} else {
			str = str.replaceAll("_", "").trim();
		}

		char[] arr = str.toCharArray();

		for (int i = 0; i < arr.length; i++) {
			if (Character.isWhitespace(arr[i]) == false && Character.isLetterOrDigit(arr[i]) == false) {
				return true;
			}
		} // End Of Loop

		return false;
	}// End Of Method

	public static boolean isSMSCActive(String circle) {

		if (QueueManager.containsSMSC(circle)) {
			SMSC smsc = QueueManager.getSMSC(circle);
			if (smsc != null && ((smsc.getSmppGateway() != null && smsc.getSmppGateway().isConnected())
					|| smsc.getHttpGateway() != null)) {
				Logger.sysLog(LogValues.debug, CoreUtils.class.getName(),
						" SMSC [" + circle + "] is Connected and Alive ");
				return true;
			}
		} // End Of Connection Check
		Logger.sysLog(LogValues.debug, CoreUtils.class.getName(), " SMSC [" + circle + "] Connection found Dead ");
		return false;
	}// End Of Method

	private static void loadLanguageSpecifications() {

		if (CoreUtils.languageSpecs == null) {
			CoreUtils.languageSpecs = new HashMap<String, LanguageSpecification>(10);
		} else if (CoreUtils.languageSpecs.size() > 0) {
			CoreUtils.languageSpecs.clear();
		}

		List<LanguageSpecification> languages = CoreUtils.utilboimpl.fetchLanguageSpecifications();

		for (int i = 0; i < languages.size(); i++) {

			LanguageSpecification lang = languages.get(i);
			CoreUtils.languageSpecs.put(lang.getLanguage(), lang);

		} // End Of Loop

	}// End Of Method

	public static LanguageSpecification getLanguageSpecifications(String language) {

		if (language != null) {
			language = language.trim();
		} else {
			return null;
		}

		if (CoreUtils.languageSpecs == null) {
			CoreUtils.loadLanguageSpecifications();
		}

		if (CoreUtils.languageSpecs.containsKey(language)) {
			return CoreUtils.languageSpecs.get(language);
		}

		return null;
	}// End Of Method

	public static Time getTime(String time) {

		if (time == null) {
			Logger.sysLog(LogValues.warn, CoreUtils.class.getName(), " Unable to Parse Time ");
			return new Time(System.currentTimeMillis());
		} else {
			time = time.trim();
		}

		SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm:ss.SSS");

		try {
			/**
			 * Appending SSS is mandatory OR ELSE it will not throw exception in 12Hour
			 * Format
			 */
			return new Time(tFormat.parse(time + ".000").getTime());
		} catch (Exception oe) {
			try {
				SimpleDateFormat format12 = new SimpleDateFormat("hh:mm:ss aa");
				Date timestamp = format12.parse(time);
				return new Time(timestamp.getTime());
			} catch (Exception ie) {
				Logger.sysLog(LogValues.warn, CoreUtils.class.getName(), " Error Parsing Time :: " + time.trim());
				return new Time(System.currentTimeMillis());
			} // End Of Inner Try Catch
		} // End Of Try Catch

	}// End Of Method

	public static HashMap<String, Integer> getMOFailureNotificationShortCodes() {

		if (CoreUtils.moFailureNotification == null) {
			List<MessageFormats> formats = SMSController.validation.failureActionShortcodes();
			CoreUtils.moFailureNotification = new HashMap<String, Integer>(5);

			for (int i = 0; i < formats.size(); i++) {
				MessageFormats format = formats.get(i);
				CoreUtils.moFailureNotification.put(format.getServiceCode(), format.getId());
			} // End Of Loop

		} // End Of IF

		return CoreUtils.moFailureNotification;
	}// End Of Method

	public static int getMoFailureNotify(String servicecode) {

		if (CoreUtils.moFailureNotification == null) {
			CoreUtils.getMOFailureNotificationShortCodes();
		}

		if (CoreUtils.moFailureNotification.containsKey(servicecode))
			return CoreUtils.moFailureNotification.get(servicecode);
		else {
			return -1;
		}
	}// End Of Method

	public static int getScFailureNotify() {

		int id = SMSController.validation.getFailureScFormat();

		if (id > 0)
			return id;
		else
			return -1;
	}

	public static String convertToStandardDateFormat(String datetime) {

		SimpleDateFormat sdfRecv = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		SimpleDateFormat sdfSent = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {
			Date newTimestamp = sdfRecv.parse(datetime);
			return sdfSent.format(newTimestamp).trim();
		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, CoreUtils.class.getName(), " Unable to Convert DateTime :: " + datetime);
			return datetime;
		} // End Of Try Catch

	}// End Of Method

	public static String generateEncryptedPassword(String password) {

		String encryptedPassword = "";
		try {
			String nonce = Long.toString((long) (Math.random() * Math.pow(10, 19)));

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			String date = dateFormat.format(new Date());
			String key = nonce.concat(date).concat(password);

			MessageDigest md = MessageDigest.getInstance("SHA-1");

			encryptedPassword = (Base64.encodeBase64String(md.digest(key.getBytes())));

			encryptedPassword += "," + nonce + "," + date;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CoreUtils.class.getName(),
					"Error in generating encrypted password: " + Logger.getStack(e));
		}
		return encryptedPassword;
	}// End Of Method

	public static String getcorelatorId() {
		String corelatorId = "";
		UUID uuid = UUID.randomUUID();
		corelatorId = "bng" + uuid + Long.toString((long) (Math.random() * Math.pow(10, 19)));
		return corelatorId;
	}

	public static int getValidMsisdnLength() {
		return CoreUtils.msisdnLength;
	}// End Of Method

	protected static List<SMSBlacklist> getSMSBlacklist() {
		return CoreUtils.utilboimpl.getBlacklisted();
	}// End Of Method

	public static boolean isBlacklisted(String msisdn) {

		if (CoreUtils.blacklistNumbers == null) {
			CoreUtils.blacklistNumbers = new BlacklistCheck(CoreUtils.utilboimpl.getBlacklisted());
		}
		if (msisdn == null || msisdn.trim().length() == 0)
			return false;

		return CoreUtils.blacklistNumbers.isBlacklistedUser(msisdn);
	}// End Of Method

	public static boolean isWhitelisted(String shortcode, String msisdn, String msgType) {

		if (CoreUtils.whitelistNumbers == null) {
			CoreUtils.whitelistNumbers = new WhitelistCheck(CoreUtils.utilboimpl.getWhitelisted());
		}
		if (msisdn == null || msisdn.trim().length() == 0)
			return false;

		return CoreUtils.whitelistNumbers.isWhitelistedUser(shortcode, msisdn, msgType);
	}// End Of Method

	public static int getInteger(byte[] pdu, int start, int offset) throws PDUException {

		//Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " i am getting in getInteger ( pdu"+pdu+" ,start= "+start+" offset="+offset+ ")");
		
		//System.out.println( "i am getting in getInteger ( pdu"+pdu+" ,start= "+start+" offset="+offset+ ")");
				
		//Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " and pdu length ="+pdu.length);
		
		//System.out.println(" and pdu length ="+pdu.length);
		
		byte[] buff = new byte[offset];
		int value = 0;

		
		
		if (pdu.length > start && pdu.length >= offset) {
			for (int i = start, j = 0; j < offset; i++, j++) 
			{
				buff[j] = pdu[i];
				
				//Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " and buff[j] ="+buff[j]);
			} // End Of String

			
		//	Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " and buff.length ="+buff.length);
			
			for (int i = 0; i < buff.length; i++) {

				int temp = (offset - (i + 1)) * 8;
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), " and temp ="+temp);
				value += ((buff[i] & 0xFF) << temp);
			}

			return value;
		} else {
			throw new PDUException(" Unable to Parse PDU as Integer ");
		}
	}// End Of Method

	public static String getString(byte[] pdu, int start, int offset) throws PDUException {

	//	Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "in get string method we get lenght="+offset+ " and pdu"+pdu);
		
		
		byte[] buff = new byte[offset];
		char[] val = new char[offset - 1];

		String value = null;

		if (pdu.length > start && pdu.length >= offset) {
			
		//	Logger.sysLog(LogValues.info, CoreUtils.class.getName(),"pdu lenght"+pdu.length);
			
			for (int i = start, j = 0; j < offset; i++, j++) {
				buff[j] = pdu[i];
			} // End Of String

			for (int i = 0; i < buff.length - 1; i++) {
				val[i] = (char) buff[i];
			}

			value = new String(val);

			return value;
		} else {
			throw new PDUException(" Unable to Parse PDU as Integer ");
		}
	}// End Of Method

	/*change for auth key generation TIGO TANZANIA*/

	public static String getAuthKey() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if(CoreUtils.getProperty("partnerServiceId") == null || CoreUtils.getProperty("presharedKey") == null)
			return null;
		String phraseToEncrypt = CoreUtils.getProperty("partnerServiceId") + "#" + System.currentTimeMillis();
		String encryptionAlgorithm = "AES/ECB/PKCS5Padding";
		Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
		SecretKeySpec key = new SecretKeySpec((CoreUtils.getProperty("presharedKey")).getBytes(), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		final byte[] crypted = cipher.doFinal(phraseToEncrypt.getBytes());
		String encrypted = Base64.encodeBase64String(crypted); //getEncoder().encodeToString(crypted);
		return encrypted;
	}

	/*end of change for auth key generation TIGO TANZANIA*/

	/*change for token generation BURKINA FASO*/

	public static void generateAndSaveToken() throws IOException {

		int noOfServices = Integer.parseInt(CoreUtils.getProperty("servicesCount"));

		for(int i = 1; i <= noOfServices; i++) {

			String serviceid = CoreUtils.getProperty("serviceid" + i);
			String clientId = CoreUtils.getProperty("clientid" + i);
			String clientSecret = CoreUtils.getProperty("clientsecret" + i);
			String url = CoreUtils.getProperty("tokenurl");

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setDoOutput(true);
			String dataString = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(dataString);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();

			Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "Response Code: " + responseCode);

			if(responseCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String output;
				StringBuffer response = new StringBuffer();
				while((output = reader.readLine()) != null) {
					response.append(output);
				}
				reader.close();
				Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "The response is: " + response.toString());

				JsonParser jsonParser = new JsonParser();

				JsonElement jsonTree = jsonParser.parse(response.toString());

				if(jsonTree.isJsonObject()) {
					JsonObject jsonObject = jsonTree.getAsJsonObject();
					JsonElement accessToken = jsonObject.get("access_token");

					switch(i) {
					case 1:
						CoreUtils.token1 = accessToken.toString();
						break;
					case 2:
						CoreUtils.token2 = accessToken.toString();
						break;
					}
				} 

			} else if (responseCode == 401) {
				Logger.sysLog(LogValues.error, CoreUtils.class.getName(), "Token not generated for serviceid: " + serviceid + ", kindly check it manually and with the development team");
			}
		}
	}

	/*end of change for token generation BURKINA FASO*/


	static {
		disableSslVerification();
	}

	private static void disableSslVerification() {
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
			};

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	public static String parseMatchContent(String content , String matchDetails) throws JSONException {

		if(content == null)
			return "contentNull";

		Logger.sysLog(LogValues.debug, CoreUtils.class.getName(), "in getMatchContent function");
		
		JSONObject jo = new JSONObject(matchDetails);
		
		HashMap<String, Object> yourHashMap = new Gson().fromJson(jo.toString(), HashMap.class);
		Properties p = new Properties();
		
		for (Entry<String, Object> entry : yourHashMap.entrySet()) {
			String val = "";
			if(entry.getValue()!=null) {
				val = entry.getValue().toString();
			}
			p.put(entry.getKey(), val);
		}

		Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "properties: "+p);
		
		String parsecontent= null;
		SimpleParser simpleParser;
		try {
			simpleParser = new SimpleParser(content.trim());
			parsecontent = simpleParser.parse(p);

		} catch (Exception e) {
			Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "error in parsing data");
			e.printStackTrace();
		}

		return parsecontent;
	}


	
	public static  String gsmencode(String ascci){
		
		
		final int[] GSM7CHARS = {
		        0x0040, 0x00A3, 0x0024, 0x00A5, 0x00E8, 0x00E9, 0x00F9, 0x00EC,
		        0x00F2, 0x00E7, 0x000A, 0x00D8, 0x00F8, 0x000D, 0x00C5, 0x00E5,
		        0x0394, 0x005F, 0x03A6, 0x0393, 0x039B, 0x03A9, 0x03A0, 0x03A8,
		        0x03A3, 0x0398, 0x039E, 0x00A0, 0x00C6, 0x00E6, 0x00DF, 0x00C9,
		        0x0020, 0x0021, 0x0022, 0x0023, 0x00A4, 0x0025, 0x0026, 0x0027,
		        0x0028, 0x0029, 0x002A, 0x002B, 0x002C, 0x002D, 0x002E, 0x002F,
		        0x0030, 0x0031, 0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037,
		        0x0038, 0x0039, 0x003A, 0x003B, 0x003C, 0x003D, 0x003E, 0x003F,
		        0x00A1, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047,
		        0x0048, 0x0049, 0x004A, 0x004B, 0x004C, 0x004D, 0x004E, 0x004F,
		        0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057,
		        0x0058, 0x0059, 0x005A, 0x00C4, 0x00D6, 0x00D1, 0x00DC, 0x00A7,
		        0x00BF, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067,
		        0x0068, 0x0069, 0x006A, 0x006B, 0x006C, 0x006D, 0x006E, 0x006F,
		        0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077,
		        0x0078, 0x0079, 0x007A, 0x00E4, 0x00F6, 0x00F1, 0x00FC, 0x00E0,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		        -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,
		    };
		
		
		
        StringBuilder sb = new StringBuilder();
        Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "we get sms text to encode is "+ascci);
        System.out.println("we get sms text to encode is "+ascci);
        int length = ascci.length();
        int gsm7Length = GSM7CHARS.length;
        for (int i = length; i > 0; i--) {
             
            char c = ascci.charAt((i - 1));
            for (int j = 0; j < gsm7Length; j++) {
                 
                if ((char) GSM7CHARS[j] == c) {
                    int num = GSM7CHARS[j];
                    sb.append(makeSevenBits(Integer.toBinaryString(num)));
                }
            }
        }
        String encoded = from7BitBinaryToHexReversed(sb.toString());
        System.out.println("encoded "+encoded);
        return encoded;
    
  
  } 
 



private static String  from7BitBinaryToHexReversed(String binary){
    String ret = "";
    String temp = "";
    int length = binary.length();
    int rem = length % 8;
    int missingLenth = 8 - rem;
    StringBuilder zeros = new StringBuilder();
    for(int i = 0; i < missingLenth; i++) {
        zeros.append("0");
    }
    binary = zeros.toString() + binary;
    length = binary.length();
    for (int i = length; i >= 8; i -= 8) {//read in reverse direction
        temp = binary.substring((i - 8), i);//chop into 8 bits
        int val = Integer.parseInt(temp, 2);//get decimal value of binary
        String code = Integer.toHexString(val);
        if (code.length() < 2) {
            code = "0" + code;
        }
        ret += code;
    }
    return ret.toUpperCase();
}


private  static String makeSevenBits(String binaryStr){
    String ret = "";
    StringBuilder zeros = new StringBuilder();
    int length = binaryStr.length();
    int appends = 7 - length;
    for (int i = 0; i < appends; i++) {
        zeros.append("0");//Append missing 0's to make 7 bits
    }
    ret = zeros + binaryStr;
    return ret;
}

	
	
	
	public static String getUid(String msisdn) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException
	{
		
		
		
			String phraseToEncrypt = CoreUtils.getProperty("uid");
			
			String finalmsisdn =  CoreUtils.stripCodes(msisdn);
			
			phraseToEncrypt = phraseToEncrypt.replace("$msisdn$", finalmsisdn );
			 Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "phraseToEncrypt   "+phraseToEncrypt);
			 System.out.println("phraseToEncrypt   "+phraseToEncrypt);
			String encryptionAlgorithm = "AES/ECB/PKCS5Padding";
			Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
			System.out.println(phraseToEncrypt);
			SecretKeySpec key = new SecretKeySpec(phraseToEncrypt.getBytes(), "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			final byte[] crypted = cipher.doFinal(phraseToEncrypt.getBytes());
			String uid = Base64.encodeBase64String(crypted); //getEncoder().encodeToString(crypted);
			 Logger.sysLog(LogValues.info, CoreUtils.class.getName(), "finalUid    "+uid);
			
			return uid;
			
			
			
			
			
			
		}	
		
		
		
		
	
	
	

}// End OfClass
