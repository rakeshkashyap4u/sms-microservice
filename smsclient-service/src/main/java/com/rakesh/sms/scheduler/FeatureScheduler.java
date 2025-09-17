package com.rakesh.sms.scheduler;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.jms.connection.CachingConnectionFactory;

import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.features.Quiz;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.main.SmsValidation;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class FeatureScheduler implements JobScheduler {

	private String serviceid, subserviceid, circle, shortcode, language;
	private static Connection connection;
	private CoreEnums.Protocol protocol;
	private Session session;
	private int priority;

	static {
		FeatureScheduler.connection = null;
	}// End Of Static Block

	protected FeatureScheduler(ActiveAlerts jobAlert) {

		this.protocol = CoreEnums.Protocol.values[jobAlert.getProtocol()];
		this.serviceid = jobAlert.getServiceid();
		this.priority = jobAlert.getPriority();

		String subservice = jobAlert.getSubserviceid();
		if (subservice != null && subservice.length() > 0)
			this.subserviceid = subservice;
		else
			this.subserviceid = new String("");

		String shortcode = jobAlert.getCli();
		if (shortcode != null && shortcode.length() > 0)
			this.shortcode = shortcode;
		else
			this.shortcode = CoreUtils.getProperty("callerID");

		String circle = jobAlert.getCircle();
		if (circle != null && circle.length() > 0)
			this.circle = circle;
		else
			this.circle = Pusher.getDefaultCircle();

		this.language = jobAlert.getLanguage();
		this.session = null;

		if (FeatureScheduler.connection == null) {
		    try {
		    	// Create Artemis ConnectionFactory (Jakarta-compatible)
		    	ActiveMQConnectionFactory artemisCF =
		    	        new ActiveMQConnectionFactory("tcp://localhost:61616");

		    	// Wrap in Spring's CachingConnectionFactory
		    	CachingConnectionFactory cachingCF = new CachingConnectionFactory((jakarta.jms.ConnectionFactory) artemisCF);
		    	cachingCF.setSessionCacheSize(10);

		    	// Create the connection
		    	Connection connection = (Connection) cachingCF.createConnection();
		    	connection.start();

		    } catch (jakarta.jms.JMSException jmse) {
		        Logger.sysLog(LogValues.error, this.getClass().getName(),
		                " Unable to start Artemis connection \n" + Logger.getStack(jmse));
		    } catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		    
		}


	@Override
	public void run() {

		final String subLog = "[" + this.serviceid + "][" + this.subserviceid + "]";

		if (this.serviceid == null || this.subserviceid == null) {

			Logger.sysLog(LogValues.warn, this.getClass().getName(),
					subLog + " Invalid Feature details | Check serviceid/subserviceid ");

		} else if (this.serviceid.equalsIgnoreCase("Quiz")) {

			String queueNameKey = this.serviceid.toLowerCase().trim() + "." + this.subserviceid.toLowerCase().trim()
					+ ".src.queuename";
			String queueName = CoreUtils.getProperty(queueNameKey);

			Logger.sysLog(LogValues.debug, this.getClass().getName(), subLog + " --- QueueName= " + queueName);

			if (queueName != null) {

				queueName = queueName.toLowerCase().trim();

				Quiz featureQuiz = new Quiz(this.serviceid, this.subserviceid, this.circle);
				featureQuiz.setShortcode(this.shortcode);
				featureQuiz.setLanguage(this.language);
				featureQuiz.setPriority(this.priority);
				featureQuiz.setProtocol(this.protocol);

				try {

					this.session = FeatureScheduler.connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
					Destination queue = this.session.createQueue(queueName);
					MessageConsumer consumer = this.session.createConsumer(queue);
					consumer.setMessageListener(featureQuiz);

					featureQuiz.setSession(this.session);
					SmsValidation.addFeature(this.shortcode, featureQuiz);
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							subLog + " Feature successfully started.. ");

				} catch (JMSException jmse) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							subLog + " Unable to start feature listener :: " + jmse.getMessage());
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							subLog + " Feature initialization Failed. ");
				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							subLog + " Error while starting feature --- \n" + Logger.getStack(e));
				} // End Of Try Catch

			} else {
				Logger.sysLog(LogValues.warn, this.getClass().getName(), subLog + " Feature Queue not defined ");
			} // End Of Null Check

		} else {
			Logger.sysLog(LogValues.info, this.getClass().getName(), subLog + " Feature Type NOT Defined ");
		} // End Of Feature Type check

	}// End Of Thread

	@Override
	public void end() {
		try {
			if (this.session != null)
				this.session.close();
			if (FeatureScheduler.connection != null)
				FeatureScheduler.connection.close();
		} catch (JMSException e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Stopping Feature Listener FAILED... ");
		} // End Of Try Catch
	}// End Of Method

}
