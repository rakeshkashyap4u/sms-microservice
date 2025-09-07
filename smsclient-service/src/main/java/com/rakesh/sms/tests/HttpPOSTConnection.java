package com.rakesh.sms.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPOSTConnection {

	private static final String USER_AGENT = "Mozilla/5.0";

	private static final String POST_URL = "http://127.0.0.1:8080/SMSClient/pushSms";

	private static final String POST_PARAMS = "cli=BNG&msisdn=8801700870009&content=This is testing&validate=true&unicode=true&callback=true&transactionID=12345678&price=0&action=SMS&serviceID=G";

	public static void main(String[] args) throws IOException {
		sendPOST();
	}

	private static void sendPOST() throws IOException {
		URL obj = new URL(POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
		} else {
		}
	}

}
