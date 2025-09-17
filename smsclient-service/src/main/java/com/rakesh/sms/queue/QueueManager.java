package com.rakesh.sms.queue;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.rakesh.sms.beans.SMSC;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.main.ReConnector;
import com.rakesh.sms.scheduler.BlackoutHourMonitor;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.util.CoreEnums.SMSType;

public class QueueManager extends Thread {

	private static final long MAX_TIMEOUT = 5000L, MIN_TIMEOUT = 500L;
	private int serviceWindow, alertWindow, promoWindow;
	public static ConcurrentHashMap<String, SMSC> smscList;
	private static Pusher currentListener;
	private static int lastQueueScheduled;
	private static Pusher listeners[];
	private CoreEnums.SMSType queue;
	private static Object runLock;
	private static boolean Paused;
	private static long track;
	private Random generator;
	private boolean manage;
	public static int TPS;
	//	public static ConcurrentHashMap<String, SMSC> removedSmscList;  // commented when found error in NCell Nepal smsc connections.

	private QueueSizeBean queueBroker = null; // Conditional Loading

	static {
		QueueManager.smscList = new ConcurrentHashMap<String, SMSC>();
		//		QueueManager.removedSmscList = new ConcurrentHashMap<String, SMSC>(); // commented when found error in NCell Nepal smsc connections.
		QueueManager.track = System.currentTimeMillis();
		QueueManager.listeners = new Pusher[4]; //Airtel Malawi
		QueueManager.lastQueueScheduled = 2;
		QueueManager.runLock = new Object();
		QueueManager.Paused = false;
	}

	{
		/** Please DO NOT make it a Static block, To maintain Randomness **/
		this.generator = new Random();
		this.manage = true;
	}

	public int getServiceWindow() {
		return serviceWindow;
	}

	public void setServiceWindow(int serviceWindow) {
		this.serviceWindow = serviceWindow;
	}

	public int getAlertWindow() {
		return alertWindow;
	}

	public void setAlertWindow(int alertWindow) {
		this.alertWindow = alertWindow;
	}

	public int getPromoWindow() {
		return promoWindow;
	}

	public void setPromoWindow(int promoWindow) {
		this.promoWindow = promoWindow;
	}

	public QueueSizeBean getQueueBroker() {
		return queueBroker;
	}

	public void setQueueBroker(QueueSizeBean queueBroker) {
		this.queueBroker = queueBroker;
	}

	/**
	 * 
	 * Although QueueManager.smscList Hashtable is public, it is recommended to use
	 * the below methods to access hashtable. These methods contains all required
	 * checks and logs in it, making it easy to Debug.
	 * 
	 * Please Note: QueueManager.smscList must be public only, as per it's usage
	 * demands.
	 * 
	 */
	public static void addSMSC(String circle, SMSC smsc) {
		if (circle != null && smsc != null) {
			QueueManager.smscList.put(circle, smsc);
		} else {
			Logger.sysLog(LogValues.error, QueueManager.class.getName(),
					" Error Adding SMSC to Hashtable ||  circle=" + circle + " | SMSC=" + smsc);
		}
	}// End Of Method


	//changed when error found in NCell Nepal

	public static void unbindSmsc(String circle) {
		ConcurrentHashMap<String, SMSC> smscList = QueueManager.smscList;
		String result = "Failure";

		if (circle == null || circle.length() == 0)
			circle = Pusher.getDefaultCircle();
		else
			circle = circle.trim().toUpperCase();

		SMSC smsc = smscList.get(circle);

		if (smsc != null) {

			smsc.shutdown();
			smscList.remove(circle);
			result = "Success";

			if (result.equalsIgnoreCase("success")) {
				Logger.sysLog(LogValues.info, QueueManager.class.getName(),
						" SMSC Unbind Session request Successfull for Circle: " + circle + " | Reason: " + result);
			} else
				Logger.sysLog(LogValues.warn, QueueManager.class.getClass().getName(),
						" SMSC Unbind Session request Successfull for Circle: " + circle + " WITHOUT ANY Reason ");

		} else
			Logger.sysLog(LogValues.error, QueueManager.class.getClass().getName(),
					" SMSC Unbind Session Request | No SMSC Session found [2] ");


	}


	//changed when error found in NCell Nepal



	public static void removeSMSC(String circle) {
		if (circle != null && QueueManager.smscList.containsKey(circle)) {
			//			QueueManager.smscList.remove(circle);  // commented when found error in NCell Nepal smsc connections.
			unbindSmsc(circle);
		} else {
			Logger.sysLog(LogValues.debug, QueueManager.class.getName(), " Unable to Remove SMSC from Hashtable ");
		}
	}// End Of Method

	public static void removeSMSC(String circle, SMSC smsc) {
		if (circle != null && QueueManager.smscList.containsKey(circle)) {
			//			QueueManager.removedSmscList.put(circle, smsc); // commented when found error in NCell Nepal smsc connections.
			//			QueueManager.smscList.remove(circle); // commented when found error in NCell Nepal smsc connections.
			unbindSmsc(circle);
		} else {
			Logger.sysLog(LogValues.debug, QueueManager.class.getName(), " Unable to Remove SMSC from Hashtable ");
		}
	}// End Of Method

	public static SMSC getSMSC(String circle) {

		if (circle != null && QueueManager.smscList.containsKey(circle)) {
			return QueueManager.smscList.get(circle);
		} else {
			// re-add the circle config
			// addSMSC(circle, smsc);
			Logger.sysLog(LogValues.info, QueueManager.class.getName(), "smsclist:" + smscList);
			Logger.sysLog(LogValues.debug, QueueManager.class.getName(), " NO SMSC found in Hashtable ");
		}
		return null;
	}// End Of Method

	public static SMSC getRemovedSMSC(String circle) {

		Logger.sysLog(LogValues.info, QueueManager.class.getName(), " Circle: "+QueueManager.smscList);
		if (circle != null && QueueManager.smscList.containsKey(circle)) {
			return QueueManager.smscList.get(circle);
		} else {
			// re-add the circle config
			// addSMSC(circle, smsc);
			//Logger.sysLog(LogValues.info, QueueManager.class.getName(), "smsclist:" + smscList);
			Logger.sysLog(LogValues.debug, QueueManager.class.getName(), " NO SMSC found in Hashtable ");
		}
		return null;
	}// End Of Method

	public static boolean containsSMSC(String circle) {
		if (circle != null && QueueManager.smscList.containsKey(circle)) {
			return true;
		} else {
			return false;
		}
	}// End Of Method

	public static void reduceTPS() {

		long now = System.currentTimeMillis();
		long diff = now - QueueManager.track;

		diff /= 1000L; // In Seconds

		if (diff >= 10 && QueueManager.TPS > 1) {
			QueueManager.TPS -= 1;
			QueueManager.track = now;
			Pusher.ThreadBurstTime = 1000 / QueueManager.TPS;
			Logger.sysLog(LogValues.info, QueueManager.class.getName(), " TPS Reduced | NEW TPS=" + QueueManager.TPS);
		} else if (diff >= 10) {
			Logger.sysLog(LogValues.warn, QueueManager.class.getName(), " Cannot REDUCE TPS | Already very Low ");
		} // End Of IF ELSE

	}// End Of Method

	/**
	 * 
	 * Since the listeners are being scheduled as per its priority, this method
	 * calculates the burst time i.e. the time for which the listener will run.
	 * 
	 * As soon as the burst time is over, the current listener is stopped and
	 * listeners again contend for the slot to get scheduled
	 * 
	 */
	private long getTime() {

		long burstTime = 0;

		switch (QueueManager.lastQueueScheduled) {

		case 1:
			this.queue = CoreEnums.SMSType.Promotional;
			QueueManager.lastQueueScheduled = 2;
			burstTime = promoWindow;
			break;

		case 2:
			this.queue = CoreEnums.SMSType.Service;
			//			QueueManager.lastQueueScheduled = 0; //Airtel Malawi
			QueueManager.lastQueueScheduled = 3;
			burstTime = serviceWindow;
			break;

			//Airtel Malawi
		case 3:
			this.queue = CoreEnums.SMSType.UNKNOWN;
			QueueManager.lastQueueScheduled = 0;
			burstTime = serviceWindow;
			break;

		case 0:
		default:
			this.queue = CoreEnums.SMSType.Alert;
			QueueManager.lastQueueScheduled = 1;
			burstTime = alertWindow;
			break;

		}// End Of Switch

		long temp = (long) (burstTime * this.generator.nextInt(5));

		if (temp < MIN_TIMEOUT)
			return MIN_TIMEOUT;
		else if (temp > MAX_TIMEOUT)
			return MAX_TIMEOUT;
		else
			return temp;

	}// End Of Method

	@Override
	public void run() {

		long bTime = 0L, count = 0L;
		int queue = 0;

		try {
			/** TPS needs to be initialized before Pusher.init() */
			QueueManager.TPS = Integer.parseInt(CoreUtils.getProperty("TPS"));
		} catch (Exception e) {
			QueueManager.TPS = 7;
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Unable to Parse 'global.sms_properties.TPS' ");
		} finally {
			Pusher.init();
		}

		if (this.queueBroker == null) {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" MBeans Disabled | Assuming initial queue size to be 0 ");
		}

		

		while (this.manage) {

			bTime = this.getTime();
			queue = this.queue.ordinal();
			count = (bTime * QueueManager.TPS) / 1000; /** For Maintaining the TPS */

			if (BlackoutHourMonitor.isBlackout() == true) {
				QueueManager.currentListener = QueueManager.listeners[SMSType.Service.ordinal()];
				this.queue = SMSType.Service;
				queue = this.queue.ordinal();
			} else {
				QueueManager.currentListener = QueueManager.listeners[queue];
			}

			Logger.sysLog(LogValues.trace, this.getClass().getName(),
					" NextQueueType= " + this.queue.toString() + "  |  BurstTime=" + bTime + "  |  smsLimit=" + count);

			try {

				/** Start SMS popping from Queue and pushing to SMSC via ESME */
				// if(SmsQueue.getSize(QueueManager.currentListener.getType()) > 0) {
				QueueManager.currentListener.begin(count);
				Thread.sleep(bTime + 10L);
				// Logger.sysLog(LogValues.info, this.getClass().getName(), " Listener going to
				// sleep ... " );
				QueueManager.currentListener.pause();
				// Logger.sysLog(LogValues.info, this.getClass().getName(), " Thread Round
				// Ended/Paused! " );
				// }else {
				// Logger.sysLog(LogValues.debug, this.getClass().getName(), "Queue " +
				// QueueManager.currentListener.getType() + " empty!");
				// }

				
				
			} catch (InterruptedException e) {
				Logger.sysLog(LogValues.info, this.getClass().getName(), " QueueManager Interrupted [0] ");
				QueueManager.currentListener.pause();
				break;
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			}

			if (QueueManager.Paused == true) {
				try {
					synchronized (QueueManager.runLock) {
						Logger.sysLog(LogValues.info, this.getClass().getName(), " ~~~~~ SMS Client PAUSED ~~~~~ ");
						QueueManager.runLock.wait();
					}
				} catch (InterruptedException ie) {
					Logger.sysLog(LogValues.info, this.getClass().getName(), " Paused QueueManager Interrupted ");
				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(), " Error RESUMING SMS Client ");
				} // End Of Try Catch
			} // End Of IF

		} // End Of Loop

		Logger.sysLog(LogValues.info, this.getClass().getName(), " QueueManager Thread Ended ");

	}// End Of Thread

	/** Used for Sending Sync SMS **/
	public static Pusher getPusherObject() {
		return QueueManager.listeners[0];
	}// End of Method

	/**
	 * The below two method are used to Pause the client and Resume the client
	 * respectively, without closing the client. So OnPause, It closes all SMSC
	 * connections and stops listening on ActiveMQ queues. Leading to NO pushing of
	 * SMS BUT Accepting of SMS into queues.
	 */
	public static void pauseManager() {
		try {

			QueueManager.Paused = true;
			ReConnector.killAll();
			QueueManager.currentListener.pause();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, QueueManager.class.getName(),
					"Error Stopping SMS Queue Listener \n " + Logger.getStack(e));
		} // End Of Try Catch
	}// End of Method

	public static void resumeManager() {
		try {

			ReConnector reconnect = new ReConnector(Pusher.getDefaultCircle());
			reconnect.safeStart();
			QueueManager.Paused = false;
			reconnect.join();

			synchronized (QueueManager.runLock) {
				QueueManager.runLock.notifyAll();
			}

			Logger.sysLog(LogValues.info, QueueManager.class.getName(), " ~~~~~ SMS Client RESUMED ~~~~~ ");
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, QueueManager.class.getName(),
					"Error Resuming SMS Queue Listener \n " + Logger.getStack(e));
		} // End Of Try Catch
	}// End of Method

	/**
	 * This method, unlike above two is called when the client/application is
	 * closed. Hence stops all the listeners and connections and does the clean up
	 * activity.
	 */
	public void abort() {
		try {

			Logger.sysLog(LogValues.info, this.getClass().getName(), " Waiting for current task to End... ");

			Pusher.closeConnection();
			this.manage = false;
			this.interrupt();

			if (QueueManager.listeners[0] != null) {
				QueueManager.listeners[0].abort();
			}
			if (QueueManager.listeners[1] != null) {
				QueueManager.listeners[1].abort();
			}
			if (QueueManager.listeners[2] != null) {
				QueueManager.listeners[2].abort();
			} //Airtel Malawi
			if (QueueManager.listeners[3] != null) {
				QueueManager.listeners[3].abort();
			}
			this.join();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		} // End Of Try Catch
	}// End Of Method

}// End Of Class
