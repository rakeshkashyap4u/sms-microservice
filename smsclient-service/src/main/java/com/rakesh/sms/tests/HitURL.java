package com.rakesh.sms.tests;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.rakesh.sms.util.Logger;

public class HitURL extends Thread {

	private HttpClient httpClient;
	private HttpMethod method;

	public HitURL(String requestURI) throws Exception {

		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		DefaultHttpMethodRetryHandler retryhandler = new DefaultHttpMethodRetryHandler();

		HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
		managerParams.setMaxTotalConnections(1000);
		managerParams.setStaleCheckingEnabled(true);
		managerParams.setConnectionTimeout(60000);
		managerParams.setTcpNoDelay(false);
		managerParams.setLinger(-1);

		connectionManager.setParams(managerParams);
		URL url = new URL(requestURI);
		GetMethod getMethod = new GetMethod(url.toString());
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryhandler);
		getMethod.setFollowRedirects(true);
		this.method = getMethod;

		this.httpClient = new HttpClient(connectionManager);
		this.httpClient.getParams().setVersion(HttpVersion.HTTP_1_1);
		this.httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");

	}// End Of Constructor

	public static void main(String args[]) {

		try {

			/*
			 * String content = "موعد أذان الفجر ليوم غد في تمام الساعة 4:00 صباحاً";
			 * content=
			 * "%D8%A3%D9%86%D8%AA+%D9%85%D8%B4%D8%AA%D8%B1%D9%83+%D8%A8%D8%A7%D9%84%D8%AE%D8%AF%D9%85%D8%A9+%D8%A7%D9%84%D8%B5%D9%88%D8%AA%D9%8A%D8%A9+%D8%A7%D9%84%D8%A5%D8%B3%D9%84%D8%A7%D9%85%D9%8A%D8%A9%D8%8C+%D8%A7%D8%AA%D8%B5%D9%84+%D8%B9%D9%84%D9%89+090022333+%D9%88+%D8%A7%D8%B3%D8%AA%D9%85%D8%B9+%D9%84%D9%84%D9%82%D8%B1%D8%A2%D9%86+%D8%A7%D9%84%D9%83%D8%B1%D9%8A%D9%85+%D9%88+%D8%BA%D9%8A%D8%B1%D9%87%D8%A7+%D9%85%D9%86+%D8%A7%D9%84%D8%AE%D8%AF%D9%85%D8%A7%D8%AA+%D8%A7%D9%84%D8%AF%D9%8A%D9%86%D9%8A%D8%A9";
			 * content = URLEncoder.encode(content, "UTF-8");
			 */

			String content = "SSSHIUD";

			for (int i = 0; i < 100; i++) {

				// HitURL hitter = new
				// HitURL("http://localhost:8080/SMSClient/sendSms?msisdn=962796015213&cli=IslamicIVR&priority=0&content="+content+"&unicode=true&script=1&validate=true&serviceType=BGIVR");
				HitURL hitter = new HitURL(
						"http://localhost:8080/SMSClient/sendSms?msisdn=9971314566&priority=0&content=" + content + i
								+ "&unicode=true&validate=true");
				StringBuilder builder = new StringBuilder();
				String response = "null";

				if (hitter.httpClient == null)
					System.out.println(" HttpConnectionManager is NULL ");
				else if (hitter.method == null)
					System.out.println(" HttpMethod is NULL ");
				else {
					int statusCode = hitter.httpClient.executeMethod(hitter.method);

					if (statusCode != HttpStatus.SC_OK) {
						System.out.println(" GETRequest Failed::   RequestURI= " + hitter.method.getURI());
						System.out.println(" GETRequest Failed::   StatusCode= " + statusCode + "  |  StatusText="
								+ HttpStatus.getStatusText(statusCode) + "  |  Message= "
								+ hitter.method.getStatusLine());
					}

					try {

						InputStream httpStream = hitter.method.getResponseBodyAsStream();
						InputStreamReader reader = new InputStreamReader(httpStream, "UTF-8");

						while (true) {

							int out = reader.read();

							if (out < 0)
								break;

							builder.append((char) out);

						} // End Of Loop

						response = builder.toString().trim();

					} catch (Exception streamException) {
						System.out.println(" Error Reading URL Response Stream | Fetching as String ");
						response = hitter.method.getResponseBodyAsString();
					}

					response = response.replaceAll("\n", "").trim();
					String finalResponse = response;
					System.out.println(" HTTP GetURL Response= " + finalResponse);

					hitter.method.releaseConnection();

				} // End Of IF Else

			}
		} catch (Exception e) {
			System.out.println(Logger.getStack(e));
		}

	}// End Of Thread

}// End Of Class
