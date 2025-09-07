package com.rakesh.sms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.rakesh.sms.beans.DeliveryInfoNotification;
import com.rakesh.sms.beans.MTInfoResponse;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.entity.MessageActions;
import com.rakesh.sms.entity.MtResponse;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.util.CoreEnums.Type;

public class UrlHitter extends Thread {
	private HashMap<String, String> httpHeaders;
	private CoreEnums.HttpMethod method;
	private HttpRequestManager manager;
	private String finalResponse;
	private int finalResponseCode;
	private URL requestUrl;
	private Message sms;
	private String postParam;
	private static CharSequence seq = "MEAI_OnlineServices";

	public UrlHitter(HttpRequestManager manager, URL url) {
		this(manager, url, CoreEnums.HttpMethod.GET);
	}

	public UrlHitter(HttpRequestManager manager, URL url, CoreEnums.HttpMethod method) {
		this.manager = manager;
		requestUrl = url;
		postParam = null;
		this.method = method;
		sms = null;
	}

	public void setSms(Message sms) {
		this.sms = sms;
	}

	public void setMode(CoreEnums.HttpMethod method) {
		this.method = method;
	}

	public void setPostParam(String data) {
		postParam = data;
	}

	public void setHttpHeaders(HashMap<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public void addHttpHeader(String property, String value) {
		httpHeaders.put(property, value);
	}

	public String getResponse() {
		try {
			join();
			return finalResponse;
		} catch (Exception e) {
			Logger.sysLog(3, getClass().getName(), " Error fetching Response \n" + Logger.getStack(e));
		}

		return "";
	}

	public int getResponseCode() {
		try {
			join();
			return finalResponseCode;
		} catch (Exception e) {
			Logger.sysLog(3, getClass().getName(), " Error fetching Response \n" + Logger.getStack(e));
		}

		return 0;
	}

	static {
		disableSslVerification();
	}

	private static void disableSslVerification() {
		try
		{
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() 
			{
				public java.security.cert.X509Certificate[] getAcceptedIssuers() 
				{
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

	
	public void run() {
		try {
			StringBuilder builder = new StringBuilder("");
			String response = "null";

			long startTime = System.currentTimeMillis();

			if (method == null) { 	
				Logger.sysLog(3, getClass().getName(), " Http RequestType is NotDefined ");
			} else if (requestUrl == null) {
				Logger.sysLog(3, getClass().getName(), " Http RequestURL is NotDefined ");
			}
			else 
			{

				Logger.sysLog(LogValues.trace, UrlHitter.class.getName(), "going to make HTTPUrl Connection");

				HttpURLConnection con = (HttpURLConnection) requestUrl.openConnection();
			//	con.setConnectTimeout(manager.getConnectionTimeout());
			//con.setReadTimeout(manager.getReadTimeout());
			//	con.setRequestMethod(method.toString());
				
				con.setConnectTimeout(80000);
				con.setReadTimeout(90000);
				con.setRequestMethod(method.toString());
				
			//	System.out.println("we set ConnectTimeout "+con.getConnectTimeout()+"and set ReadTimeout"+con.getReadTimeout());

				SMSController sm = new SMSController();
				
				Logger.sysLog(LogValues.trace, UrlHitter.class.getName(), "connection made");

				try {

					if (method.ordinal() == CoreEnums.HttpMethod.POST.ordinal()) 
					{
						con.setRequestProperty("User-Agent", manager.getUserAgent());
						con.setDoOutput(true);

						if (httpHeaders != null) {
							for (String property : httpHeaders.keySet()) {
								con.setRequestProperty(property, (String) httpHeaders.get(property));
								Logger.sysLog(LogValues.info, UrlHitter.class.getName(), "headers: " + (String) httpHeaders.get(property));
							}
						}
						Logger.sysLog(2, getClass().getName(), " HTTP POST QueryString= " + requestUrl.getQuery());

						Logger.sysLog(LogValues.info, UrlHitter.class.getName(), "postParam" + postParam );
						
						System.out.println("postParam" +postParam);

						if (postParam == null) 
						{

							postParam = requestUrl.getQuery();
						}

						OutputStream os = con.getOutputStream();
						os.write(postParam.getBytes());
						os.flush();
						os.close();

					} 

					finalResponseCode = con.getResponseCode();

					if(con.getResponseCode() == 200 || con.getResponseCode() == 201) 
					{

					//	System.out.println("inside if of 200 we set ConnectTimeout "+con.getConnectTimeout()+"and set ReadTimeout"+con.getReadTimeout());

						
						Logger.sysLog(LogValues.info, this.getClass().getName(), "The url hit is: " + requestUrl.toString());
						BufferedReader reader = null;

						//						Logger.sysLog(LogValues.info, this.getClass().getName(), "1. " + "con.getContentEncoding().trim() " + con.getContentEncoding().trim() );
						//						Logger.sysLog(LogValues.info, this.getClass().getName(), "2. " + "con.getContentEncoding().trim().equalsIgnoreCase(\"gzip\") " + con.getContentEncoding().trim().equalsIgnoreCase("gzip"));
						Logger.sysLog(LogValues.info, this.getClass().getName(), "3. " + "requestUrl.toString()" + requestUrl.toString());
						Logger.sysLog(LogValues.info, this.getClass().getName(), "4. " + "requestUrl.toString().contains(seq) " + requestUrl.toString().contains(seq));

						if((requestUrl.toString()).contains(seq)) {
							Logger.sysLog(LogValues.info, this.getClass().getName(), "in if block for url " + requestUrl.toString().trim());
							reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream()), "UTF-8")); 
						} else {
							Logger.sysLog(LogValues.info, this.getClass().getName(), "Reached else block");
							reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
						}

						for (;;) 
						{
							int out = reader.read();

							if (out < 0) {
								break;
							}
							builder.append((char) out);
						}
						Logger.sysLog(LogValues.info, this.getClass().getName(), "The input stream received is: " + builder.toString().trim());
						reader.close();
						con.disconnect();
					}
					else if (con.getResponseCode() == 401 && CoreUtils.getProperty("country").equals("BFA"))
					{
						Logger.sysLog(LogValues.info, this.getClass().getName(), "Access token expired, regenrating it.");
						CoreUtils.generateAndSaveToken();
					} else {
						Logger.sysLog(3, getClass().getName(), " HTTPRequest Failed with ResponseCode: "
								+ con.getResponseCode() + " \n URL: " + requestUrl.toString());
					}

					response = builder.toString().trim();   


					if(CoreUtils.getProperty("country").equals("Indonesia") && CoreUtils.getProperty("operator").equals("XL")
							&& CoreUtils.getProperty("protocol").equals("HTTP") && sms.getUsage() == CoreEnums.Type.MT)
					{
						
						//sms.setUsage(Type.MO);    Mark this for XL Indonesia
						Logger.sysLog(LogValues.info, this.getClass().getName(), "checking for XL Indonesia HTTP  ");

						MtResponse mtresponse = new MtResponse();

						if(sms.getCli() == null) 
						{
							mtresponse.setCli(CoreUtils.getProperty("callerID"));
						}else {
							mtresponse.setCli(sms.getCli());
						}
						mtresponse.setMsisdn(sms.getMsisdn());
						mtresponse.setSubserviceid(sms.getServiceid());

						//response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><push-response><tid>100001200101200422085115581771</tid><status-id>0</status-id><message>DELIVERED</message></push-response>";

						//response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><push-response><status-id>0</status-id><message>failed</message><sdc>0</sdc></push-response>";
						
						
						MTInfoResponse notification = (MTInfoResponse) XmlParser.parseXml(response,
								new MTInfoResponse());

						mtresponse.setTid(notification.getTid());
						mtresponse.setStatus_id(notification.getStatus_id());
						mtresponse.setMessage(notification.getMessgae());
						mtresponse.setSdc(notification.getSdc());

						if(notification.getStatus_id()!= null && notification.getStatus_id().equals("0")) 
						{
							String rsp = new SMSController().addMTResponse(mtresponse);
						}
						
						else {
							Logger.sysLog(LogValues.info, this.getClass().getName(), "MT Failed with status id : " + notification.getStatus_id() + " for msisdn [ " +mtresponse.getMsisdn() + " ] " );
							String response1 = sm.receiveDrGET(sms.getMsisdn(), "failed", sms.getCli(), null, null,  null,null ,null, null, null, null, null,sms.getServiceid() ,null);
						}
					}
					
				} catch (SocketTimeoutException ste)
				{
					Logger.sysLog(3, getClass().getName(),
							" HTTPRequest Timeout from Server \n URL: " + requestUrl.toString() + " " );
					response = "SocketTimeout";
					
					if(CoreUtils.getProperty("country").equals("Indonesia") && CoreUtils.getProperty("operator").equals("XL")
							&& CoreUtils.getProperty("protocol").equals("HTTP")) 
					{
					String response1 = sm.receiveDrGET(sms.getMsisdn(), "failed", sms.getCli(), null, null, null, null, null,null,null, null, null,sms.getServiceid() ,null);
					}
				} catch (ConnectException ce) {
					Logger.sysLog(3, getClass().getName(),
							ce.getMessage() + " HTTP request Failed \n URL: " + requestUrl.toString() + " " + Logger.getStack(ce));
					response = "ConnectionTimeout";
					
					if(CoreUtils.getProperty("country").equals("Indonesia") && CoreUtils.getProperty("operator").equals("XL")
							&& CoreUtils.getProperty("protocol").equals("HTTP")) {
					String response1 = sm.receiveDrGET(sms.getMsisdn(), "failed", sms.getCli(), null, null, null, null, null,null, null, null, null,sms.getServiceid() ,null);
					}
					
				} catch (IOException ioe) {
					Logger.sysLog(3, getClass().getName(), ioe.getMessage()
							+ " Check URL | Server might NOT Active or Running \n URL: " + requestUrl.toString() + " " + Logger.getStack(ioe));
					response = "IOError";
				} catch (Exception e) {
					Logger.sysLog(3, getClass().getName(), " Error Reading URL Response Stream " + Logger.getStack(e));
					response = "Failure: " + con.getResponseCode();
				}

				response = response.replaceAll("\n", "").trim();
				finalResponse = response;

				Logger.sysLog(2, getClass().getName(), " HTTP " + method.toString() + " Response = " + finalResponse);

				long endTime = System.currentTimeMillis();
				long duration = endTime - startTime + 1L;
				long remaining = 0L;

				if (duration < Pusher.ThreadBurstTime) {
					remaining = Pusher.ThreadBurstTime - duration;
				}

				Logger.sysLog(6, getClass().getName(), " HTTP SMS Request ProcessTime = " + duration);

				if (remaining > 0L) {
					try {
						Thread.sleep(remaining);
					} catch (Exception e) {
					}
				}
				int queue = sms == null ? 0 : sms.getType().ordinal();
				manager.finish(queue);
				Logger.sysLog(6, getClass().getName(), " HTTP SMS Request thread Finished!!! ");
			}
		} catch (Exception e) {
			Logger.sysLog(3, getClass().getName(), Logger.getStack(e));
		}
	}
}