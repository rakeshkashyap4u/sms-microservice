package com.rakesh.sms.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestBody;

import com.rakesh.sms.beans.Header;
import com.rakesh.sms.beans.SMSC;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.queue.QueueManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Expression {

	public Properties msgAdditionParameter(String input){

		Properties fix = new Properties();
		String[] strings = input.split("&");

		Logger.sysLog(LogValues.info, this.getClass().getName(), "msgformat option: "+ input  );

		List<String> seqStr = sequenceStr(strings);

		Logger.sysLog(LogValues.info, this.getClass().getName(), "msgformat option after format: "+ seqStr  );

		for(String str : seqStr) {
			String[] pair = str.split("=");
			
			if(pair[0].equals("APIKey")) {
				String keyvalue = pair[1].substring(1, pair[1].length()-2);
				String calculateData = "";
				if(keyvalue.equals("API")) {
					if(CoreUtils.getProperty("operator").equals("TIGO") && CoreUtils.getProperty("country").equals("TZA"))
						calculateData = "Bearer "+TokenAPI();
				}

				fix.put(pair[0], calculateData);
			}

			else if(!pair[1].contains("[")) {
				String value = null;
				try {
					value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} 
				fix.put(pair[0], value);
			}

			else {
				String keyvalue = pair[1].substring(1, pair[1].length()-1);

				String calculateData = "";

				if(keyvalue.contains("#")) {
					String data = "";
					String value = null;
					String hashmtd = "";
					String[] parameter = keyvalue.split("#");
					for(int i = 0 ;i<parameter.length ; i++) {

						if(i==0) 
							hashmtd = parameter[0];
						else {
							value = null;
							String current = parameter[i];
							if(current.contains("@")) {
								value = findFormatValue(current);
							}
							else {
								if(fix.containsKey(current))
									value = fix.getProperty(current);
							}
							data += value;
						}
					}

					if(hashmtd.equals("md5"))
						try {
							calculateData = md5Hash(data);
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						}

					if(hashmtd.equals("concat")) {
						calculateData = data;
					}

					if(hashmtd.equals("UUID")) {
						calculateData = uuidGenerator().toString();
					}

					/*if(hashmtd.equals("API")) {
						if(CoreUtils.getProperty("operator").equals("TIGO") && CoreUtils.getProperty("country").equals("TZA"))
						calculateData = "Bearer "+TokenAPI();
					}*/

					Logger.sysLog(LogValues.info, this.getClass().getName(), "hash mtd: "+ hashmtd + " data string: "
							+ data + " calculated data: "+calculateData);
					fix.put(pair[0], calculateData);
				}

				else {

					if(keyvalue.contains("@")) {
						String value = findFormatValue(keyvalue);
						fix.put(pair[0], value);
					}
				}
			}
		}
		Logger.sysLog(LogValues.info, this.getClass().getName(), "addition properties: "+ fix );
		return fix;

	}


	public List<String> sequenceStr(String[] strings) {

		List<Integer> staticStr = new ArrayList<Integer>(); 
		List<Integer> formatStr = new ArrayList<Integer>(); 
		List<Integer> calculateStr = new ArrayList<Integer>();

		for(int i = 0 ; i<strings.length ; i++) {
			if(!strings[i].contains("[")) {
				staticStr.add(i);
			}

			else {
				if(strings[i].contains("@"))
					formatStr.add(i);

				else if(strings[i].contains("#"))
					calculateStr.add(i);
			}
		}

		List<String> seqStr = new ArrayList<String>();

		for(int p = 0 ; p<staticStr.size() ; p++)
			seqStr.add(strings[staticStr.get(p)]);

		for(int q = 0 ; q<formatStr.size() ; q++)
			seqStr.add(strings[formatStr.get(q)]);

		for(int r = 0 ; r<calculateStr.size() ; r++)
			seqStr.add(strings[calculateStr.get(r)]);

		return seqStr;

	}


	public String findFormatValue(String data) {

		String[] pair = data.split("@");
		String value = null;
		if(pair[0].equals("dateformat")) {

			SimpleDateFormat formatter = new SimpleDateFormat(pair[1]); 
			Date date = new Date(); 
			value = formatter.format(date);
		}
		return value;
	}


	public String md5Hash(String plaintext) throws NoSuchAlgorithmException {

		MessageDigest m = MessageDigest.getInstance("MD5");
		m.reset();
		m.update(plaintext.getBytes());
		byte[] digest = m.digest();
		BigInteger bigInt = new BigInteger(1,digest);
		String hashtext = bigInt.toString(16);

		while(hashtext.length() < 32 ){
			hashtext = "0"+hashtext;
		}
		return hashtext;
	}

	public UUID uuidGenerator() {

		//		UUID uuid1 = Generators.randomBasedGenerator().generate();
		UUID uuid1 = UUID.randomUUID();

		return uuid1;
	}

	public String generateAuthKey(String PServiceId , String PpresharedKey) {

		//static implementation for batelco and mtn yemen
		/*String presharedKey = "FjlsXOa2SfWAytGy";
		//PSK shared by TIMWE 
		String phrasetoEncrypt = "2345" + "#" +System.currentTimeMillis();   
		String encryptionAlgorithm = "AES/ECB/PKCS5Padding";
		String encrypted = "";
		try {    Cipher cipher = Cipher.getInstance(encryptionAlgorithm); 
		SecretKeySpec key = new SecretKeySpec(presharedKey.getBytes(), "AES"); 
		cipher.init(Cipher.ENCRYPT_MODE, key);  
		final byte[] crypted = cipher.doFinal(phrasetoEncrypt.getBytes());
		encrypted = Base64.getEncoder().encodeToString(crypted);  
		} 
		catch (Exception e) { 
		}  
		return encrypted; */
		
		if(PServiceId == null || PServiceId.length() == 0 || PpresharedKey == null && PpresharedKey.length() == 0) {
			Logger.sysLog(LogValues.error, this.getClass().getName()," serviceid and presharedKey not present in options column with static value");	
		}
		
		//System.out.println("we get serviceid  "+PServiceId+" and PpresharedKey "+PpresharedKey);
		String phrasetoEncrypt = PServiceId + "#" +System.currentTimeMillis();   
		String encryptionAlgorithm = "AES/ECB/PKCS5Padding";
		String encrypted = "";
		try 
		{    
		Cipher cipher = Cipher.getInstance(encryptionAlgorithm); 
		SecretKeySpec key = new SecretKeySpec(PpresharedKey.getBytes(), "AES"); 
		cipher.init(Cipher.ENCRYPT_MODE, key);  
		final byte[] crypted = cipher.doFinal(phrasetoEncrypt.getBytes());
		encrypted = Base64.getEncoder().encodeToString(crypted);  
		} 
		catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception "+ Logger.getStack(e));
		}  
		return encrypted; 

	}

	public String TokenAPI(){
		
		if(CoreUtils.getProperty("testmode")!=null || CoreUtils.getProperty("testmode").equalsIgnoreCase("ON") )
		{
			String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJCTkdfVGlnbyIsImF1ZCI6IkFDQ0VTUyIsInNjb3BlcyI6IkFETUlOIiwiaXNzIjoiaHR0cDovL3NpeGRlZS5jb20iLCJpYXQiOjE2MjA4MjA3MTIsImV4cCI6MTYyMDgyOTcxMn0.jIF5_ItrYOfEizFRukEm1E83pgRQ0xx1cyrCMgIdOBR4X6WTV2tIiaWKOpmEus4AX4VISdAeRBgrJaVZitVzxQ";
			return token;
		}

		String token = "";
		String API = CoreUtils.getProperty("AuthAPI");
		Logger.sysLog(LogValues.info, this.getClass().getName(), "we have token ="+API);
		String userName = CoreUtils.getProperty("username");
		Logger.sysLog(LogValues.info, this.getClass().getName(), "username ="+userName);
		String passWord = CoreUtils.getProperty("password");
		Logger.sysLog(LogValues.info, this.getClass().getName(), "password ="+passWord);

		String POST_PARAMS = "username="+userName+"&password="+passWord;

		try {
			
			JSONObject obj=new JSONObject();    
			  try {
				obj.put("username",userName);
				obj.put("password",passWord);    
				  System.out.print(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}    
			
			token = sendPOSTINBody(API, obj.toString());
			token = token.substring(1, token.length()-1);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("token ="+token);
		return token;
	}

	
	private static String sendPOSTINBody(String API, String POST_PARAMS) throws IOException {
		
		String token = "";
		
		URL obj = new URL(API);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("X-Requested-With", "XMLHttpRequest");

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		
		os.write(POST_PARAMS.getBytes()); 
		os.flush();
		os.close();
		// For POST only - END

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

			JSONObject obj1=new JSONObject();    
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonTree = jsonParser.parse(response.toString());
			
			if(jsonTree.isJsonObject()) {
				JsonObject jsonObject = jsonTree.getAsJsonObject();
				token = jsonObject.get("token").toString();
				
			}

		} else {
		}

		return token;
	}
	
	public String getTimeZone(String timezone)
	{
		ZoneId zoneId = ZoneId.of("Asia/Muscat");
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        String time1 = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        Logger.sysLog(LogValues.info, this.getClass().getName(), time1);
        return time1;
	}
	
	
	//only for Orange Tunisia added by Rakesh
	public  String getTokenAPIforOT(){

		String token = "";
		
		
		
		String API = CoreUtils.getProperty("TokenAuthAPI");
		//String API = "https://api.orange.com/oauth/v3/token";
		
	//	System.out.println("we are generating token now using api "+API);
		
		try {
			
			JSONObject obj=new JSONObject();    
			  try {
				obj.put("grant_type","client_credentials");
				   
				//  System.out.print(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}    
			
			token = sendPOSTINBodyforOT(API, obj.toString());
			token = token.substring(1, token.length()-1);
			
			
			
			//System.out.println("token :"+token);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return token;
	}

	
	//only for Orange Tunisia added by Rakesh
	private  String sendPOSTINBodyforOT(String API, String POST_PARAMS) throws IOException {
		
		String token = "";
		
		
		URL obj = new URL(API);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Authorization", "Basic V1Y2TEVzdVl4TUNaOEJhZWhmTUdBdHJ0ZTRUWEQ5TDg6WmFKVEswNEt0MU5WYWdXdQ==");
        
	  
		
		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		
		
		
		OutputStream os1 = con.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");    
		osw.write("grant_type=client_credentials");
		osw.flush();
		osw.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		
	
		

		if (responseCode == HttpURLConnection.HTTP_OK) 
		{ //success
			BufferedReader in = new BufferedReader(new InputStreamReader
					(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			Logger.sysLog(LogValues.info, this.getClass().getName(), "response for tokenapi: "+ response );
			JSONObject obj1=new JSONObject();    
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonTree = jsonParser.parse(response.toString());
			
			if(jsonTree.isJsonObject()) 
			{
				JsonObject jsonObject = jsonTree.getAsJsonObject();
				token = jsonObject.get("access_token").toString();
				//System.out.println("token :"+token);
				//Logger.sysLog(LogValues.info, this.getClass().getName(), "access_token: "+ token );
				
			}

		} 
		else 
		{
			System.out.println("Failure"+responseCode);
		}

		return token;
	}

     

	
}
