package com.rakesh.sms.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rakesh.sms.beans.Header;
import com.rakesh.sms.beans.MODetails;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.beans.RegistrationRequest;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.HttpRequestManager;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.util.UrlHitter;

import jakarta.xml.bind.DatatypeConverter;

public class HttpGateway implements Gateway {

	private static final String USER_AGENT = "Mozilla/5.0";
	private static final int CONNECTION_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 40000;
	private HttpRequestManager manager;
	private Integer liveThreads = 0;
	private Object tpsLock;

	synchronized public Integer getRunningThreadsCount() {
		return this.liveThreads;
	}// End Of Method

	synchronized public void decreaseThreadsCount() {
		this.liveThreads--;
	}// End Of Method

	synchronized private void increaseThreadsCount() {
		this.liveThreads++;
	}// End Of Method

	public HttpGateway() {
		this(CONNECTION_TIMEOUT, READ_TIMEOUT, false);
	}// End Of Constructor

	public HttpGateway(int readtimeout) {
		this(CONNECTION_TIMEOUT, readtimeout, false);
	}// End Of Constructor

	public HttpGateway(int readtimeout, boolean validation) {
		this(CONNECTION_TIMEOUT, readtimeout, validation);
	}// End Of Constructor

	public HttpGateway(int conntimeout, int readtimeout) {
		this(conntimeout, readtimeout, false);
	}// End Of Constructor

	public HttpGateway(int conntimeout, int readtimeout, boolean validation) {

		this.manager = new HttpRequestManager(this, conntimeout, readtimeout);
		this.liveThreads = new Integer(0);
		this.tpsLock = new Object();

		if (validation == true) {
			SmsValidation.init(10);
		}

	}// End Of Constructor

	public boolean isConnected() {
		boolean connected = false;
		if (this.manager != null) {
			connected = true;
		}
		return connected;
	}// End Of Method

	public String getUserAgent() {
		return HttpGateway.USER_AGENT;
	}// End Of Method

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

	public void sendPOSTRequest(String requestURI, String requestData, Message sms, Properties p) {

		try {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					"RequestURI: " + requestURI + ", requestData: " + requestData + ", sms: " + sms);
			URL url = new URL(CoreUtils.parseUrl(requestURI, sms, p));

			try {

				this.increaseThreadsCount();

				UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.POST);
				hitter.setPostParam(requestData);
				hitter.setSms(sms);
				hitter.start();

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			} // End Of Try Catch

		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed POST URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting POST URL \n" + e.getMessage());
		} // End Of Try Catch

	}// End Of Method

	public void sendPOSTRequest(String requestURI, Message sms, Properties p) {

		try {

			URL url = new URL(CoreUtils.parseUrl(requestURI, sms,p));

			try {

				this.increaseThreadsCount();
				UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.POST);
				hitter.setSms(sms);
				hitter.start();

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			} // End Of Try Catch

		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed POST URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting POST URL \n" + e.getMessage());
		} // End Of Try Catch

	}// End of Method

	public String sendSyncPOSTRequest(String requestURI, String requestData, List<Header> requestHeaders) {

		String response = "";

		try {

			URL url = new URL(requestURI);

			HashMap<String, String> headers = new HashMap<String, String>();

			for (int i = 0; requestHeaders != null && i < requestHeaders.size(); i++) {
				headers.put(requestHeaders.get(i).getProperty(), requestHeaders.get(i).getValue());
			}

			UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.POST);
			hitter.setHttpHeaders(headers);

			if (requestData != null) {
				hitter.setPostParam(requestData);
			}

			hitter.start();

			Logger.sysLog(LogValues.info, this.getClass().getName(), "Waiting for URL response...");

			response = hitter.getResponse();

			Logger.sysLog(LogValues.info, this.getClass().getName(), "response: " + response);
		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed POST URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting POST URL \n" + e.getMessage());
		} // End Of Try Catch

		return response;
	}// End Of Method

	public String sendSyncDELETERequest(String requestURI, String requestData, List<Header> requestHeaders) {

		String response = "";

		try {

			URL url = new URL(requestURI);

			HashMap<String, String> headers = new HashMap<String, String>();

			for (int i = 0; requestHeaders != null && i < requestHeaders.size(); i++) {
				headers.put(requestHeaders.get(i).getProperty(), requestHeaders.get(i).getValue());
			}

			UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.DELETE);
			hitter.setHttpHeaders(headers);

			if (requestData != null) {
				hitter.setPostParam(requestData);
			}

			hitter.start();

			Logger.sysLog(LogValues.info, this.getClass().getName(), "Waiting for URL response...");
			response = hitter.getResponse();

		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed POST URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting POST URL \n" + e.getMessage());
		} // End Of Try Catch

		return response;
	}// End Of Method

	public void sendGETRequest(String requestURI, Message sms, Properties p) {

		try {

			if (sms.getMessage() != null) {

				if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-16BE"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-16BE").replaceAll("%00", ""));
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-8"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-8"));
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-32"))  // for mtn syria
				{
					System.out.print("we get UTF-32 ");
					//sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-32"));
					sms.setMessage(DatatypeConverter.printHexBinary(sms.getMessage().getBytes("UTF-16")).replaceAll("FEFF", ""));
				}
			}

			URL url = new URL(CoreUtils.parseUrl(requestURI, sms,p));

			try {

				this.increaseThreadsCount();
				UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.GET);
				hitter.setSms(sms);
				// ayu comment
				Logger.sysLog(LogValues.info, this.getClass().getName(), "sms" + sms);
				hitter.start();

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			} // End Of Try Catch

		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed GET URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting GET URL \n" + e.getMessage());
		} // End Of Try Catch

	}// End Of Method

	public void sendGETRequest(String requestURI, Message sms) {

		try {

			
			
			if (sms.getMessage() != null) {
				
				System.out.println("here i get requestURI"+requestURI);
				

				

				if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-16BE"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-16BE").replaceAll("%00", ""));
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-8"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-8"));
//				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-32"))  // for mtn syria
//				{
//					System.out.print("we get UTF-32 ");
//					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-32"));
//					
//				}
				
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-32"))  // for mtn syria
				{
					System.out.print("we get UTF-32 ");
					//sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-32"));
					sms.setMessage(DatatypeConverter.printHexBinary(sms.getMessage().getBytes("UTF-16")));
				}
			}

			URL url = new URL(CoreUtils.parseUrl(requestURI, sms,null));

			try {

				this.increaseThreadsCount();
				UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.GET);
				hitter.setSms(sms);
				// ayu comment
				Logger.sysLog(LogValues.info, this.getClass().getName(), "sms" + sms);
				
				System.out.println("sms" + sms);
				hitter.start();

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			} // End Of Try Catch

		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed GET URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting GET URL \n" + e.getMessage());
		} // End Of Try Catch

	}// End Of Method

	public String sendSyncGETRequest(String requestURI, Message sms, Properties p) {

		String response = "";

		try {

			if (sms.getMessage() != null) {

				if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-16BE"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-16BE").replaceAll("%00", ""));
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-8"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-8"));
				
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-32"))  // for mtn syria
				{
					System.out.print("we get UTF-32 ");
					//sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-32"));
					sms.setMessage(DatatypeConverter.printHexBinary(sms.getMessage().getBytes("UTF-16")).replaceAll("FEFF", ""));
				}
			}

			URL url = new URL(CoreUtils.parseUrl(requestURI, sms,p));

			try {

				this.increaseThreadsCount();
				UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.GET);
				hitter.setSms(sms);
				hitter.start();

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" [" + sms.getRequiredMsisdn() + "]  Waiting for URL Response... ");
				response = hitter.getResponse();

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			} // End Of Try Catch

		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed GET URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting GET URL \n" + e.getMessage());
		} // End Of Try Catch

		return response;

	}// End Of Method

	public String sendSyncGETRequest(String requestURI, Message sms) {

		String response = "";
		
		//System.out.println("yahan ho");
		

		try {

			if (sms.getMessage() != null) {

				if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-16BE"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-16BE").replaceAll("%00", ""));
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-8"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-8"));
				
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-32"))  // for mtn syria
				{
					System.out.print("we get UTF-32 ");
					
					sms.setMessage(DatatypeConverter.printHexBinary(sms.getMessage().getBytes("UTF-16")).replaceAll("FEFF", ""));
				}
			}

			URL url = new URL(CoreUtils.parseUrl(requestURI, sms,null));
			
			//System.out.println("URL "+url.toString());

			try {

				this.increaseThreadsCount();
				UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.GET);
				hitter.setSms(sms);
				hitter.start();

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" [" + sms.getRequiredMsisdn() + "]  Waiting for URL Response... ");
				response = hitter.getResponse();
				
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" Response=  "+response);

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			} // End Of Try Catch

		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed GET URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting GET URL \n" + e.getMessage());
		} // End Of Try Catch

		return response;

	}// End Of Method

	public String registerMO(RegistrationRequest request) {

		String responseURL = "";

		if (request != null) {
			try {
				Logger.sysLog(LogValues.info, this.getClass().getName(), "Registering for MO notification");

				if (request.getRegisterEachService().equalsIgnoreCase("true")) {
					List<MODetails> details = SMSController.validation.getAllMOs();
					for (int i = 0; details != null && i < details.size(); i++) {
						String requestData = CoreUtils.parseUrl(request.getRequestFormat(), details.get(i),null);
						String resp = this.sendSyncPOSTRequest(request.getRegistrationURL(), requestData,
								request.getHeaders());

						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"Response for MO Registration for [" + details.get(i).getKeyword() + "]["
										+ details.get(i).getServicecode() + "][" + details.get(i).getServiceid()
										+ "] : " + resp);
					}

				} else {

					String response = this.sendSyncPOSTRequest(request.getRegistrationURL(), request.getRequestFormat(),
							request.getHeaders());

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							"Response for MO Registration: " + response);

					if (CoreUtils.getProperty("operator").equalsIgnoreCase("SMART")
							&& CoreUtils.getProperty("operator").equalsIgnoreCase("PHL")) {
						// Resource URL field is specific for Smart Philippines

						int startIndex = response.indexOf("resourceURL");
						int endIndex = response.indexOf("resourceURL", startIndex + 11);

						if (startIndex != -1 && endIndex != -1) {
							responseURL = response.substring(startIndex + 12, endIndex - 1);
						} else {
							Logger.sysLog(LogValues.info, this.getClass().getName(), "Response is empty!");
						}
					}
				}

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						"Error registering for MO notification::" + Logger.getStack(e));
			}
		} else {
			Logger.sysLog(LogValues.warn, this.getClass().getName(), "No request defined!");
		} // End Of MO Registration Process

		return responseURL;

	}// End Of Method

	public String registerMT(RegistrationRequest request) {

		String responseURL = "";

		if (request != null) {
			try {
				Logger.sysLog(LogValues.info, this.getClass().getName(), "Registering for MT notification");
				String response = this.sendSyncPOSTRequest(request.getRegistrationURL(), request.getRequestFormat(),
						request.getHeaders());

				// resourceURL filed is specific for Smart Philippines

				int startIndex = response.indexOf("resourceURL");
				int endIndex = response.indexOf("resourceURL", startIndex + 11);

				if (startIndex != -1 && endIndex != -1) {
					responseURL = response.substring(startIndex + 12, endIndex - 1);
				} else {

				}
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						"Error registering for MO notification::" + Logger.getStack(e));
			}
		} else {
			Logger.sysLog(LogValues.warn, this.getClass().getName(), "No request defined!");
		} // End Of MT Registration Process

		return responseURL;

	}// End Of Method

	public void deregister(RegistrationRequest request) {

		if (request.getRegisterEachService().equalsIgnoreCase("true")) {
			List<MODetails> details = SMSController.validation.getAllMOs();

			for (int i = 0; details != null && i < details.size(); i++) {

				MODetails moDetail = details.get(i);
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						"Sending stop notification for MO to SDP: [" + moDetail.getKeyword() + "]["
								+ moDetail.getServicecode() + "][" + moDetail.getServiceid() + "]");

				String requestData = CoreUtils.parseUrl(request.getDeregisterRequestFormat(), moDetail,null);
				this.sendSyncDELETERequest(request.getDeregistrationURL(), requestData, request.getHeaders());
			}
		} else {

			Logger.sysLog(LogValues.info, this.getClass().getName(), "De-Registering...");
			this.sendSyncDELETERequest(request.getDeregistrationURL(), request.getDeregisterRequestFormat(),
					request.getHeaders());
		}

	}// End Of Method

	public void close() {
		SmsValidation.stop();
	}// End Of Method


	public String checkBngEligible(String API, String msisdn, String mode) { //For africell bngeligible API

		String Code = "";

		URL obj;
		try {
			
			String data = "{\"UserID\":\"A70791A8-8FBC-4BA0-B991-07CAF9BFEA20\",\"MSISDN\":\""+msisdn+"\"}";
			obj = new URL(API);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod(mode);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("username", "BlackAndGreen");
			con.setRequestProperty("Password", "BNGP@ssw0rd");

			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();

			os.write(data.getBytes()); 
			os.flush();
			os.close();
			int responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				Logger.sysLog(LogValues.info, this.getClass().getName(), " [ "+ msisdn + " ] bng eligible API response: "+ response.toString());
				//String response = "{\"Code\": \"0\", \"Description\":\"1\"}";
				JSONObject obj1=new JSONObject();    
				JsonParser jsonParser = new JsonParser();
				JsonElement jsonTree = jsonParser.parse(response.toString());

				if(jsonTree.isJsonObject()) {
					JsonObject jsonObject = jsonTree.getAsJsonObject();
					Code = jsonObject.get("Code").getAsString();
				}

			} else {
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception while hitting bng eligible API: "+ Logger.getStack(e));
		}
		return Code;
	}

	public void sendDrGETRequest(String requestURI, Message sms, String status1, String action, String tid, String subserviceid) 
	{
		
		
try {

			
			
			if (sms.getMessage() != null) {
				
				//System.out.println("here i get requestURI"+requestURI);
				

				

				if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-16BE"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-16BE").replaceAll("%00", ""));
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-8"))
					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-8"));
//				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-32"))  // for mtn syria
//				{
//					System.out.print("we get UTF-32 ");
//					sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-32"));
//					
//				}
				
				else if(CoreUtils.getProperty("encoding").equalsIgnoreCase("UTF-32"))  // for mtn syria
				{
					//System.out.print("we get UTF-32 ");
					//sms.setMessage(URLEncoder.encode(sms.getMessage(), "UTF-32"));
					sms.setMessage(DatatypeConverter.printHexBinary(sms.getMessage().getBytes("UTF-16")));
				}
			}

			Properties p = new Properties(); //this is for xl indonesia
					
				p.put("status", status1);
				p.put("action", action);
				p.put("transactionID", tid);
				p.put("subServiceId", subserviceid);
				
			//	System.out.println("subserviceid"+subserviceid);
			 
			
		//	URL url = new URL(CoreUtils.parseUrl(requestURI, sms,p));
				
				
				URL url = new URL(CoreUtils.parsercvdrUrl(requestURI, sms,p));
				

			try {

				this.increaseThreadsCount();
				UrlHitter hitter = new UrlHitter(this.manager, url, CoreEnums.HttpMethod.GET);
				hitter.setSms(sms);
				// ayu comment
				Logger.sysLog(LogValues.info, this.getClass().getName(), "sms" + sms);
				hitter.start();

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			} // End Of Try Catch

		} catch (MalformedURLException me) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Malformed GET URL :: " + requestURI + " --- " + me.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Error Hitting GET URL \n" + e.getMessage());
		} // End Of Try Catch

		
	}

}
