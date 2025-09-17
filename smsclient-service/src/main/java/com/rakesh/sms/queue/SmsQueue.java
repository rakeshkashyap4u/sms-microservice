package com.rakesh.sms.queue;

import java.sql.Date;
import java.util.Enumeration;

import jakarta.jms.DeliveryMode;
import jakarta.jms.Destination;
import jakarta.jms.IllegalStateException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class SmsQueue {

//	private static Long[] newMessageCount = new Long[3];
	private static Long[] newMessageCount = new Long[4];  //Airtel Malawi
	public static final String Queue = "smsqueue";
	private static Session session;
	
	@Autowired
	private SimpleActiveMQConnection simpleActiveMQConnection;
	
	Date date = new Date(0);
	
	public SmsQueue()
	{
		
	}

	public static void init(Session session) {
	    if (session == null) {
	        Logger.sysLog(LogValues.error, SmsQueue.class.getName(), "Cannot initialize SmsQueue: JMS session is null");
	        return;
	    }
	    SmsQueue.session = session;

	    synchronized (newMessageCount) {
	        for (int i = 0; i < newMessageCount.length; i++) {
	            newMessageCount[i] = 0L;
	        }
	    }

	    Logger.sysLog(LogValues.info, SmsQueue.class.getName(), 
	        "SmsQueue initialized successfully. JMS session is set: " + (SmsQueue.session != null));
	}



	protected static long getSize(int type) {

		return newMessageCount[type];

	}// End Of Method

	protected static void setSize(int type, int size) {

		newMessageCount[type] = (long) size;

	}// End Of Method

	public boolean push(Message msg) {
		return this.push(msg, 0);
	}// End Of Method

	public boolean push(Message msg, long delay) {
		int type = msg.getType().ordinal();
		
		if (SmsQueue.session == null) {
		    Logger.sysLog(LogValues.error, this.getClass().getName(),
		        "Cannot push SMS: JMS session not initialized.");
		    return false;
		}
		
		return this.push(msg, SmsQueue.Queue + type, delay);
	}// End Of Method

	public boolean push(Message msg, String queueName, long delay) {

		Destination dest;

		try {

			int type = msg.getType().ordinal();
			
			dest = SmsQueue.session.createQueue(queueName);
			MessageProducer producer = SmsQueue.session.createProducer(dest);
//			Logger.sysLog(LogValues.info, SmsQueue.class.getName(),
//					" Session Producer " + queueName + " Successfully Created ");

//			Logger.sysLog(LogValues.info, SmsQueue.class.getName(),
//					" msgid for Mexico: " + msg.getMsgid()+", msg.toString: "+msg.toString());
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);

			TextMessage message = SmsQueue.session.createTextMessage(msg.toString());
			if (delay > 0)
				message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
			producer.send(message);

			synchronized (newMessageCount[type]) 
			{
				newMessageCount[type]++;
			}

			Logger.sysLog(LogValues.debug, SmsQueue.class.getName(), " SMS Pushed to ActiveMQ: "+queueName+", Delay: "+delay);
			producer.close();
			return true;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		} // End Of Try Catch

		return false;

	}// End Of Method

	public static MessageConsumer createConsumer(Session sess, int type) {

		try {

			String queueName = SmsQueue.Queue + type;
			Destination dest = sess.createQueue(queueName);
			MessageConsumer consumer = sess.createConsumer(dest);
			Logger.sysLog(LogValues.info, SmsQueue.class.getName(),
					" Session Consumer " + queueName + " Successfully Created ");

			return consumer;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsQueue.class.getName(), Logger.getStack(e));
		}

		return null;

	}// End Of Method

	public static QueueBrowser createBrowser(Session sess, int type) {

		try {

			String queueName = SmsQueue.Queue + type;
			Queue queue = (jakarta.jms.Queue) new ActiveMQQueue(queueName + "?consumer.dispatchAsync=false&consumer.prefetchSize=10");
			QueueBrowser browser = sess.createBrowser(queue);
			Logger.sysLog(LogValues.debug, SmsQueue.class.getName(),
					" Browser for " + queueName + " Successfully Created ");

			return browser;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsQueue.class.getName(), Logger.getStack(e));
		}

		return null;

	}// End Of Method

	public static Message pop(MessageConsumer consumer, int type) {

		Message msg = null;

		try {
			
			System.out.println("here u come now in pop");

			String jsonMessage = "{}";
			if (newMessageCount[type] > 0) {

				javax.jms.Message message = (javax.jms.Message) consumer.receive(1000);
				
				//javax.jms.Message message = consumer.receiveNoWait();
				
				if (message != null) {
					if (message instanceof TextMessage) {
						jsonMessage = ((TextMessage) message).getText();
					//	System.out.println("here u in jsonMessage"+jsonMessage);
					}

//					Logger.sysLog(LogValues.info, SmsQueue.class.getName(),
//							" [" + CoreEnums.SMSType.values[type].toString() + "]  SMS Popped From ActiveMQ smsqueue" + type);
					msg = Message.parse(jsonMessage);
					
					//System.out.println("we get msg here from prase"+msg);
					
					//System.out.println("inside pop we have msg"+msg.toString());

					Logger.sysLog(LogValues.info, SmsQueue.class.getName(), " jsonMessage: " + jsonMessage + " msgid for Mexico is: " + msg.getMsgid());
					
					synchronized (newMessageCount[type]) 
					{
						newMessageCount[type]--;
					} // End Of Synchronization Block
				} else {
					//Logger.sysLog(LogValues.info, SmsQueue.class.getName(), " Timed Out to Pop SMS | Returning Null ");
				} // End Of Null Check

			} else {
				// Logger.sysLog(LogValues.debug, SmsQueue.class.getName(),
				// " No SMS to Pop | Returning Null " );
			} // End Of Message Count Check

		} catch (IllegalStateException ise) {
			Logger.sysLog(LogValues.info, SmsQueue.class.getName(), " [" + CoreEnums.SMSType.values[type].toString()
					+ "]  IllegalStateException on SMSQueue.pop() | Returning NULL ");
			return null;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsQueue.class.getName(), Logger.getStack(e));
		} // End Of Try Catch

		//System.out.println("msg we retrun "+msg);
		return msg;

	}// End Of Method

	public static Message peek(int type) {

		Message msg = null;

		try {

			String jsonMessage = "{}";
			javax.jms.Message message = null;
			QueueBrowser browser = SmsQueue.createBrowser(session, type);
			Enumeration<?> messages = browser.getEnumeration();

			if (messages != null && messages.hasMoreElements()) {

				message = (javax.jms.Message) messages.nextElement();

				if (message instanceof TextMessage) {
					jsonMessage = ((TextMessage) message).getText();
				}

				msg = Message.parse(jsonMessage);

			} else {
				Logger.sysLog(LogValues.info, SmsQueue.class.getName(),
						" [" + CoreEnums.SMSType.values[type].toString() + "] No Messages In Queue: smsqueue" + type);
			}

			browser.close();

		} catch (ArrayIndexOutOfBoundsException ae) {
			Logger.sysLog(LogValues.error, SmsQueue.class.getName(),
					" Wrong Queue Number at SMSQueue.peek() | Returning NULL ");
			return null;
		} catch (IllegalStateException ise) {
			Logger.sysLog(LogValues.debug, SmsQueue.class.getName(), " [" + CoreEnums.SMSType.values[type].toString()
					+ "]  IllegalStateException on SMSQueue.peek() | Returning NULL ");
			return null;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SmsQueue.class.getName(), Logger.getStack(e));
		} // End Of Try Catch

		return msg;

	}// End Of Method

}// End Of Class
