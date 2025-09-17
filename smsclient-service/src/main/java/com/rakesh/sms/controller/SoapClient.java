package com.rakesh.sms.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Controller
public class SoapClient {
	
	@RequestMapping(value = "/test123", method = RequestMethod.GET)
	public @ResponseBody String test123()  {
		String moxml = "";
		String emptymoxml = "";
		
		
		return moxml;
	}
	
	@RequestMapping(value = "/receivesms4455", method = RequestMethod.GET)
	public @ResponseBody String receivesms4455(@RequestParam("test") String test,
			@RequestParam("serviceId") String serviceId, 
			@RequestParam("cli") String cli,HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException {
		//Code to make a webservice HTTP request
		//test url - "http://localhost:8080/SMSClient/test123"
		// for cli 4455 and serviceId = 967012000006983
		String responseString = "";
		String outputString = "";
		String wsEndPoint = "http://172.16.26.50:8310/ReceiveSmsService/services/ReceiveSms";
		URL url = new URL(wsEndPoint);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		
		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header><ns1:RequestSOAPHeader xmlns:ns1=\"http://www.huawei.com.cn/schema/common/v2_1\"><ns1:spId>9670110000170</ns1:spId><ns1:spPassword>65029a64436f671f744d0243ff5a68d8</ns1:spPassword><ns1:serviceId>"+serviceId+"</ns1:serviceId><ns1:timeStamp>20191029133545</ns1:timeStamp></ns1:RequestSOAPHeader></soapenv:Header><soapenv:Body><ns2:getReceivedSms xmlns:ns2=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\"><ns2:registrationIdentifier>"+cli+"</ns2:registrationIdentifier></ns2:getReceivedSms></soapenv:Body></soapenv:Envelope>";
		Logger.sysLog(LogValues.info, SoapClient.class.getName(), "In function receiveSms4455 -- InputString: "+ xmlInput);
		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		httpConn.setRequestProperty("SOAPAction", "http://www.csapi.org/wsdl/parlayx/sms/receive/v2_2/interface/ReceiveSms/getReceivedSmsRequest");
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		out.write(b);
		out.close();
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), Charset.forName("UTF-8"));
		BufferedReader in = new BufferedReader(isr);
		while ((responseString = in.readLine()) != null) {
						outputString = outputString + responseString;
		}
		// Write the SOAP message formatted to the console.
		
		
		//outputString = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:getReceivedSmsResponse xmlns:ns1=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\"><ns1:result><message>م1</message><senderAddress>tel:967738527104</senderAddress><smsServiceActivationNumber>tel:4455</smsServiceActivationNumber><dateTime>2020-05-18T06:16:36Z</dateTime></ns1:result></ns1:getReceivedSmsResponse></soapenv:Body></soapenv:Envelope>";
		
		//outputString = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ns1:getReceivedSmsResponse xmlns:ns1=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\"/><ns1:result><message>م1</message><senderAddress>tel:967738527104</senderAddress><smsServiceActivationNumber>tel:4455</smsServiceActivationNumber><dateTime>2020-05-18T06:16:36Z</dateTime></ns1:result></ns1:getReceivedSmsResponse></soapenv:Envelope>";
		
		String formattedSOAPResponse = formatXML(outputString);
		
		//formattedSOAPResponse=formattedSOAPResponse.trim().replace("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body>","").replace("xmlns:ns1=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\">","");

		//Logger.sysLog(LogValues.info, SoapClient.class.getName(), "formattedSOAPResponse: "+formattedSOAPResponse);
		
		//String rs = parseHttpUniteangola(formattedSOAPResponse);
		
		Logger.sysLog(LogValues.info, SoapClient.class.getName(), "outputString: "+outputString);
		
		String rs1 = parseHttpUniteangola(outputString);
		
		/*if(rs!= null) {
			List<MoReceive> moReceiveList = rs.getMoReceive();
			if(moReceiveList != null) {
				for (MoReceive mr : moReceiveList) {
					String content = mr.getMessage();
					String msisdn = mr.getSenderAddress();
					String dateTime = mr.getDateTime();
					String shortcode = mr.getSmsServiceActivationNumber();
					
					 String stat = new SMSController().receiveSms(shortcode, content, msisdn, "CR1", null);
					 System.out.println("MO SENT: "+stat);
				}
			}
			else {
				System.out.println("ReceivedSms resul list: null");
			}
		}
		
		else {
			System.out.println("ReceivedSms: null");
		}*/
		
		return "success";
	}
	
	@RequestMapping(value = "/receivesms2277", method = RequestMethod.GET)
	public @ResponseBody String receivesms2277(@RequestParam("test") String test,
			@RequestParam("serviceId") String serviceId,
			@RequestParam("cli") String cli, HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException {
		//Code to make a webservice HTTP request
		//test url - "http://localhost:8080/SMSClient/test123"
		
		String responseString = "";
		String outputString = "";
		String wsEndPoint = "http://172.16.26.50:8310/ReceiveSmsService/services/ReceiveSms";
		URL url = new URL(wsEndPoint);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		 //cli is 2277 and serviceId is change here & serviceId = 967012000007222.
		String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header><ns1:RequestSOAPHeader xmlns:ns1=\"http://www.huawei.com.cn/schema/common/v2_1\"><ns1:spId>9670110000170</ns1:spId><ns1:spPassword>65029a64436f671f744d0243ff5a68d8</ns1:spPassword><ns1:serviceId>"+serviceId+"</ns1:serviceId><ns1:timeStamp>20191029133545</ns1:timeStamp></ns1:RequestSOAPHeader></soapenv:Header><soapenv:Body><ns2:getReceivedSms xmlns:ns2=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\"><ns2:registrationIdentifier>"+cli+"</ns2:registrationIdentifier></ns2:getReceivedSms></soapenv:Body></soapenv:Envelope>";
		Logger.sysLog(LogValues.info, SoapClient.class.getName(), "In function receiveSms2277 -- InputString: "+ xmlInput);
		//String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Header><ns1:RequestSOAPHeader xmlns:ns1=\"http://www.huawei.com.cn/schema/common/v2_1\"><ns1:spId>9670110000170</ns1:spId><ns1:spPassword>af7b42af5ce8556894141bc89008286f</ns1:spPassword><ns1:serviceId>967012000006983</ns1:serviceId><ns1:timeStamp>20191106150219</ns1:timeStamp></ns1:RequestSOAPHeader></soapenv:Header><soapenv:Body><ns2:getReceivedSms xmlns:ns2=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\"><ns2:registrationIdentifier>4455</ns2:registrationIdentifier></ns2:getReceivedSms></soapenv:Body></soapenv:Envelope>";
		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		httpConn.setRequestProperty("SOAPAction", "http://www.csapi.org/wsdl/parlayx/sms/receive/v2_2/interface/ReceiveSms/getReceivedSmsRequest");
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		out.write(b);
		out.close();
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream(), Charset.forName("UTF-8"));
		BufferedReader in = new BufferedReader(isr);
		while ((responseString = in.readLine()) != null) {
						outputString = outputString + responseString;
		}
		// Write the SOAP message formatted to the console.
		
		
		//outputString = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:getReceivedSmsResponse xmlns:ns1=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\"><ns1:result><message>م1</message><senderAddress>tel:967738527104</senderAddress><smsServiceActivationNumber>tel:4455</smsServiceActivationNumber><dateTime>2020-05-18T06:16:36Z</dateTime></ns1:result></ns1:getReceivedSmsResponse></soapenv:Body></soapenv:Envelope>";
		
		//outputString = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ns1:getReceivedSmsResponse xmlns:ns1=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\"/><ns1:result><message>م1</message><senderAddress>tel:967738527104</senderAddress><smsServiceActivationNumber>tel:4455</smsServiceActivationNumber><dateTime>2020-05-18T06:16:36Z</dateTime></ns1:result></ns1:getReceivedSmsResponse></soapenv:Envelope>";
		
		String formattedSOAPResponse = formatXML(outputString);
		
		//formattedSOAPResponse=formattedSOAPResponse.trim().replace("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body>","").replace("xmlns:ns1=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\">","");

		//Logger.sysLog(LogValues.info, SoapClient.class.getName(), "formattedSOAPResponse: "+formattedSOAPResponse);
		
		//String rs = parseHttpUniteangola(formattedSOAPResponse);
		
		Logger.sysLog(LogValues.info, SoapClient.class.getName(), "outputString: "+outputString);
		
		String rs1 = parseHttpUniteangola(outputString);
		
		/*if(rs!= null) {
			List<MoReceive> moReceiveList = rs.getMoReceive();
			if(moReceiveList != null) {
				for (MoReceive mr : moReceiveList) {
					String content = mr.getMessage();
					String msisdn = mr.getSenderAddress();
					String dateTime = mr.getDateTime();
					String shortcode = mr.getSmsServiceActivationNumber();
					
					 String stat = new SMSController().receiveSms(shortcode, content, msisdn, "CR1", null);
					 System.out.println("MO SENT: "+stat);
				}
			}
			else {
				System.out.println("ReceivedSms resul list: null");
			}
		}
		
		else {
			System.out.println("ReceivedSms: null");
		}*/
		
		return "success";
	}
	// format the XML in pretty String
	private static String formatXML(String unformattedXml) {
		try {
			Document document = parseXmlFile(unformattedXml);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 3);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StreamResult xmlOutput = new StreamResult(new StringWriter());
			transformer.transform(source, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}
	// parse XML
	private static Document parseXmlFile(String in) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(in));
			return db.parse(is);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String parseHttpUniteangola(String input) {

		DocumentBuilder builder;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		
		Logger.sysLog(LogValues.info, SoapClient.class.getName(),"input is  "+input.trim().trim());

		//String xmlFile=input.trim().trim().replace("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body>","").replace("xmlns:ns1=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\">",">");
		
		String xmlFile=input.trim().trim().replace("xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">",">").replace("xmlns:ns1=\"http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local\">",">");

		Logger.sysLog(LogValues.info, SoapClient.class.getName(),"xml is  "+xmlFile);
		
		HashMap<String,String> respMap = new HashMap<String,String>();
		try{



			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(new InputSource(new StringReader(xmlFile)));
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("ns1:result");
			
			Logger.sysLog(LogValues.info, SoapClient.class.getName() , "nList size: " + nList.getLength());

			
			int len = nList.getLength();
			int temp = 0;
			while(temp<len)
			{
				Node nNode = nList.item(temp);

					Element eElement = (Element) nNode;
					
					String message = eElement.getElementsByTagName("message").item(0).getTextContent();
					String senderAddress = eElement.getElementsByTagName("senderAddress").item(0).getTextContent();
					String smsServiceActivationNumber = eElement.getElementsByTagName("smsServiceActivationNumber").item(0).getTextContent();
					
					senderAddress = senderAddress.substring(4);
					smsServiceActivationNumber = smsServiceActivationNumber.substring(4);
					Logger.sysLog(LogValues.info, SoapClient.class.getName(),"message "+ message +" senderAddress " + senderAddress +" smsServiceActivationNumber "+  smsServiceActivationNumber );

					String circle = "CR1";
					if(smsServiceActivationNumber.equals("2277")) circle = "CR2";
					String stat =  "test";//new SMSController().receiveSms(smsServiceActivationNumber, message, senderAddress, circle, null);
					Logger.sysLog(LogValues.info, SoapClient.class.getName(),"MO SENT: "+stat);
					temp++;
				
			}
	       

		}catch(Exception ex)
		{
			Logger.sysLog(LogValues.error, SoapClient.class.getName(), "Exception: "+Logger.getStack(ex));
		}
		return "success";
	}
		
		
	
}







