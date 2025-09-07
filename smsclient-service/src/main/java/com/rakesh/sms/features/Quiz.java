package com.rakesh.sms.features;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.rakesh.sms.beans.Message;
import com.bng.sms.queue.SmsQueue;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.features.beans.QuizMessage;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.util.CoreEnums.SMSType;

public class Quiz implements Feature {

	private String serviceid, subserviceid, circle, language, shortcode, destQueue;
	private CoreEnums.Protocol protocol;
	private Session session;
	private SmsQueue queue;
	private int priority;

	public Quiz(String serviceid, String subserviceid, String circle) {

		this.queue = new SmsQueue();

		this.session = null;
		this.circle = circle;
		this.shortcode = null;
		this.serviceid = serviceid;
		this.subserviceid = subserviceid;

		String queueNameKey = this.serviceid.toLowerCase().trim() + "." + this.subserviceid.toLowerCase().trim()
				+ ".dest.queuename";
		this.destQueue = CoreUtils.getProperty(queueNameKey);

	}// End Of Constructor

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public CoreEnums.Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(CoreEnums.Protocol protocol) {
		this.protocol = protocol;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	protected Session getSession() {
		return this.session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getServiceid() {
		return serviceid;
	}

	public String getSubserviceid() {
		return subserviceid;
	}

	public String getCircle() {
		return circle;
	}

	@Override
	public void onMessage(javax.jms.Message msg) {

		if (msg == null)
			return;

		final String subLog = " [" + this.serviceid + "][" + this.subserviceid + "]";

		try {

			String msgJson = ((TextMessage) msg).getText();
			QuizMessage quizMsg = QuizMessage.parse(msgJson);

			Logger.sysLog(LogValues.info, this.getClass().getName(),
					subLog + " Feature Response :: " + quizMsg.toString());
			Message sms = new Message(quizMsg.getShortcode(), quizMsg.getMsisdn());

			sms.setType(SMSType.values[this.priority]);
			sms.setMessage(quizMsg.getMessage());
			sms.setServiceid(this.serviceid);
			sms.setUsage(CoreEnums.Type.MT);
			sms.setMode(this.protocol);
			sms.setCircle(this.circle);

			LanguageSpecification spec = CoreUtils.getLanguageSpecifications(this.language);

			if (sms.setLanguageSpecifications(spec) == false) {
				sms.setEncoding("true");
			}

			this.queue.push(sms);
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					subLog + " Sending Feature SMS :: " + sms.toString());

		} catch (JMSException jmse) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), subLog + " JMSException :: " + jmse.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), subLog + "\n" + Logger.getStack(e));
		} // End of Try Catch

	}// End Of Listener

	@Override
	public synchronized void sendReply(Message mo) {

		if (mo == null)
			return;

		final String subLog = "[" + this.serviceid + "][" + this.subserviceid + "][" + mo.getMsisdn() + "] ";

		try {

			QuizMessage reply = new QuizMessage();
			// String[] content = mo.getMessage().split(" ");

			reply.setMsisdn(mo.getMsisdn());
			reply.setShortcode(mo.getCli());
			reply.setQuizname(this.subserviceid);
			reply.setMessage(mo.getMessage());

			if (this.destQueue != null) {

				Destination dest = this.session.createQueue(this.destQueue.trim());
				MessageProducer producer = this.session.createProducer(dest);

				Logger.sysLog(LogValues.info, this.getClass().getName(),
						subLog + " Sending Feature Reply :: " + reply.toString());
				TextMessage message = this.session.createTextMessage(reply.toString());
				producer.send(message);

			} else {
				Logger.sysLog(LogValues.warn, this.getClass().getName(),
						subLog + " Destination queue for feature Not found | Unable to send reply... ");
			}

		} catch (JMSException jmse) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), subLog + " JMSException :: " + jmse.getMessage());
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), subLog + "\n" + Logger.getStack(e));
		} // End of Try Catch

	}// End Of Method

}
