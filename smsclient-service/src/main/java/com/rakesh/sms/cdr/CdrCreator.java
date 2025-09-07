package com.rakesh.sms.cdr;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.regex.Matcher;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.bng.sms.queue.SmsQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

public class CdrCreator {

	private static Marshaller marshallerS, marshallerR;
	public static Gson jsonReader;
	private static Boolean isJson;
	private static Session session;
	private static Destination dest;
	private static MessageProducer producer;

	static {
		try {

			JAXBContext sendContext = JAXBContext.newInstance(SmsCdrBean.class);
			CdrCreator.marshallerS = sendContext.createMarshaller();
			CdrCreator.marshallerS.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			CdrCreator.marshallerS.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			CdrCreator.marshallerS.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			JAXBContext recvContext = JAXBContext.newInstance(ReceivedSmsBean.class);
			CdrCreator.marshallerR = recvContext.createMarshaller();
			CdrCreator.marshallerR.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			CdrCreator.marshallerR.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			CdrCreator.marshallerR.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			CdrCreator.jsonReader = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
					.create();

			String isJsonCDR = CoreUtils.getExtraParam("isJsonCDR");

			if (isJsonCDR != null && isJsonCDR.trim().equalsIgnoreCase("true")) {
				CdrCreator.isJson = Boolean.valueOf(true);
			} else {
				CdrCreator.isJson = Boolean.valueOf(false);
			}

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CdrCreator.class.getName(),
					" Unable to initialize JAXB Marshler \n" + Logger.getStack(e));
		} // End Of Try Catch
	}// End Of Static Initialization Block

	public static void init(Session session) {

		try {
			CdrCreator.session = session;
			CdrCreator.dest = session.createQueue("smscdr");
			CdrCreator.producer = CdrCreator.session.createProducer(dest);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsQueue.class.getName(), Logger.getStack(e));
		} // End Of TryCatch

	}// End Of Method

	public static boolean isJsonCDR() {
		return true;
		//return CdrCreator.isJson.booleanValue();
	}// End Of Method

	private static class Writer implements Runnable {

		// private static String queueName;
		private ReceivedSmsBean recvCDR;
		private NumberFormat format;
		private SmsCdrBean sendCDR;

		public Writer() {
			this.format = NumberFormat.getIntegerInstance();
			this.format.setMinimumIntegerDigits(2);
			// Writer.queueName = "smscdr";
		}// End Of Constructor

		public void setSendCDR(SmsCdrBean sms) {
			this.sendCDR = sms;
			this.recvCDR = null;
		}// End Of Setter

		public void setRecvCDR(ReceivedSmsBean sms) {
			this.recvCDR = sms;
			this.sendCDR = null;
		}// End Of Setter

		private String generateCDRname() {

			Long timestamp = System.currentTimeMillis();
			String filename = "";

			if (isJson == false) {

				if (sendCDR != null && sendCDR.getType().equalsIgnoreCase("MT"))
					filename = this.sendCDR.getReceiverMsisdn() + "_" + this.sendCDR.getMessageId() + "_" + timestamp
							+ ".xml";
				else if (sendCDR != null && sendCDR.getType().equalsIgnoreCase("MO"))
					filename = this.sendCDR.getSenderCli() + "_" + this.sendCDR.getMessageId() + "_" + timestamp
							+ ".xml";
				else if (sendCDR != null)
					filename = this.sendCDR.getReceiverMsisdn() + "_" + this.sendCDR.getMessageId() + "_" + timestamp
							+ ".xml";
				else if (recvCDR != null && recvCDR.isDeliveryReport())
					filename = this.recvCDR.getMessageId() + "_DR_" + timestamp + ".xml";
				else if (recvCDR != null)
					filename = this.recvCDR.getSender() + "_MO_" + timestamp + ".xml";

			} else {

				/** If ATHOME JSON CDR **/

				if (sendCDR != null) {
					String status = sendCDR.getStatus();

					if (status != null && status.equalsIgnoreCase("Success") == false) {
						sendCDR.setStatus("Failure");
						if (status.length() > 7) {
							sendCDR.setReason(status.substring(8).trim());
						} else {
							sendCDR.setReason("");
						} // End Of Length Check
					} else if (status == null) {
						sendCDR.setStatus("UNKNOWN");
						sendCDR.setReason("");
					} // End Of Failure Case
				} // End Of SendCDR check

				if (sendCDR != null && sendCDR.getType().equalsIgnoreCase("MT"))
					filename = timestamp + "_" + this.sendCDR.getMessageId() + "_" + this.sendCDR.getReceiverMsisdn();
				else if (sendCDR != null && sendCDR.getType().equalsIgnoreCase("M0"))
					filename = timestamp + "_" + this.sendCDR.getMessageId() + "_" + this.sendCDR.getSenderCli();
				else if (sendCDR != null)
					filename = timestamp + "_" + this.sendCDR.getMessageId() + "_" + this.sendCDR.getReceiverMsisdn();
				else if (recvCDR != null && recvCDR.isDeliveryReport())
					filename = timestamp + "_DR_" + this.recvCDR.getMessageId();
				else if (recvCDR != null)
					filename = timestamp + "_MO_" + this.recvCDR.getSender();

			} // End Of IF ELSE

			return filename;
		}// End Of Method

		public void run() {

			try {

				String path = "";
				String filename = this.generateCDRname();

				if (recvCDR != null && (filename.length() == 0 || filename.contains("null"))) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							" CDR with NO ResponseID Requested :: " + CoreUtils.GSON.toJson(recvCDR));
				} else {

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					CdrEvent cdrEvent = null;

					if (isJson.booleanValue() == false) {

						if (sendCDR != null) {
							String content = sendCDR.getContent();
							sendCDR.setContent("##content##");
							synchronized (CdrCreator.marshallerS) {
								CdrCreator.marshallerS.marshal(this.sendCDR, baos);
								String xml = baos.toString().replaceAll("##content##",
										Matcher.quoteReplacement("<![CDATA[" + content + "]]>"));
								cdrEvent = new CdrEvent(xml, filename);
								push(cdrEvent);
							} // End Of Synchronized Block
						} else if (recvCDR != null) {
							String content = recvCDR.getContent();
							recvCDR.setContent("##content##");
							synchronized (CdrCreator.marshallerR) {
								CdrCreator.marshallerR.marshal(this.recvCDR, baos);
								String xml = baos.toString().replaceAll("##content##",
										Matcher.quoteReplacement("<![CDATA[" + content + "]]>"));
								cdrEvent = new CdrEvent(xml, filename);
								push(cdrEvent);
							} // End Of Synchronized Block
						} // End Of Marshaling

					} else {

						File cdr = new File(path, filename);
						FileOutputStream fos = new FileOutputStream(cdr);
						BufferedOutputStream dob = new BufferedOutputStream(fos);

						if (sendCDR != null) {
							synchronized (CdrCreator.jsonReader) {
								dob.write(sendCDR.toString().getBytes("UTF-8"));
							} // End Of Synchronized Block
						} else if (recvCDR != null) {
							synchronized (CdrCreator.jsonReader) {
								dob.write(recvCDR.toString().getBytes("UTF-8"));
							} // End Of Synchronized Block
						} // End Of Conversion
						dob.close();
					} // End Of JSON check

					Logger.sysLog(LogValues.info, this.getClass().getName(), " CDR Successfully Saved :: " + filename);

				} // End Of IF Else

				// }catch( FileNotFoundException fnfe ) {
				// Logger.sysLog(LogValues.error, this.getClass().getName(), fnfe.getMessage()
				// +":: Incomplete File Name to Create CDR ");
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" Unable to Create CDR \n" + Logger.getStack(e));
			} // End Of Try Catch

		}// End Of Thread

		public void push(CdrEvent event) {

			try {

				// dest = CdrCreator.session.createQueue( Writer.queueName );
				// MessageProducer producer = CdrCreator.session.createProducer(dest);
				// producer.setDeliveryMode(DeliveryMode.PERSISTENT);

				TextMessage message = session.createTextMessage(event.toString());

				producer.send(message);

				Logger.sysLog(LogValues.debug, this.getClass().getName(),
						"CDR Json pushed to ActiveMQ :  " + event.toString());
				producer.close();

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			} // End Of Try Catch

		}// End Of Method

	}// End Of Inner Class

	synchronized public static void saveAsXML(SmsCdrBean sms) {

		Logger.sysLog(LogValues.info, CdrCreator.class.getName(), "details: " + sms.getStatus() + " " + sms.getResponsetime());
		
		Writer writer = new Writer();
		writer.setSendCDR(sms);
		new Thread(writer).start();

	}// End Of Method

	synchronized public static void saveAsXML(ReceivedSmsBean sms) {

		Writer writer = new Writer();
		writer.setRecvCDR(sms);
		new Thread(writer).start();

	}// End Of Method

	synchronized public static void closeWriter() {

		try {
			session.close();
			Logger.sysLog(LogValues.info, CdrCreator.class.getName(), "Closing ActiveMQ connection for CDR");
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, CdrCreator.class.getName(), Logger.getStack(e));
		}
	}

}// End Of Class
