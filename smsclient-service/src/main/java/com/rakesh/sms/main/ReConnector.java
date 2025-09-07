package com.rakesh.sms.main;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jsmpp.bean.BindType;

import com.rakesh.sms.beans.SMSC;
import com.bng.sms.queue.QueueManager;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

/**
 * 
 * Implemented as Thread so as to JOIN whenever possible. DO NOT change the
 * implementation to Runnable
 * 
 **/
public class ReConnector extends Thread {

	private static final long MaxTimeOut = 2 * 60 * 1000; // 2 Mins
	private static final long MinTimeOut = 15 * 1000; // 15 seconds

	private static ConcurrentHashMap<String, ReConnector> Trying;
	private static long KillTimeout;

	private SMSCConfigs config;
	private boolean status;
	private String circle;

	static {
		Trying = new ConcurrentHashMap<String, ReConnector>();
	}// End Of Static Block

	public ReConnector(SMSCConfigs config) throws Exception {

		super();
		this.config = config;
		this.status = false;

		if (this.config != null) {
			this.circle = config.getCircle();
		}
		if (this.circle == null || this.config == null) {
			throw new NullPointerException(" Cannot Initiate Reconnection Process for circle [" + this.circle
					+ "]  | NULL Circle/Configuration found ");
		}
		if (CoreUtils.isSMSCActive(this.circle)) {
			throw new Exception(" SMSC Circle [" + this.circle + "] Connection already Active ");
		}
		if (Trying.containsKey(this.circle)) {
			throw new Exception(" SMSC Circle [" + this.circle + "] Connection already in Retry State ");
		}

		ReConnector.KillTimeout = ESME.getReconnectionTimeout();
		ReConnector.Trying.put(this.circle, this);
		Logger.sysLog(LogValues.debug, this.getClass().getName(),
				" ReConnector Instantiated for SMSC [" + this.circle + "] ");

	}// End Of Constructor

	public ReConnector(String circle) throws Exception {

		super();

		if (circle == null) {
			this.circle = null;
			throw new NullPointerException(
					" Cannot Initiate Reconnection Process for circle [" + this.circle + "]  | NULL Circle found ");
		} else {
			this.circle = circle;
			this.status = false;
		}

		if (CoreUtils.isSMSCActive(this.circle)) {
			throw new Exception(" SMSC Circle [" + this.circle + "] Connection already Active ");
		}
		if (Trying.containsKey(this.circle)) {
			this.config = null;
			throw new Exception(" SMSC Circle [" + this.circle + "] Connection already in Retry State ");
		} else {
			this.config = Pusher.getGatewayBoImpl().getConfigDetails(this.circle);
		}

		if (this.config == null) {
			throw new NullPointerException(" Cannot Initiate Reconnection Process for circle [" + this.circle
					+ "]  | NULL Configuration found ");
		}

		ReConnector.KillTimeout = ESME.getReconnectionTimeout();
		ReConnector.Trying.put(this.circle, this);
		Logger.sysLog(LogValues.debug, this.getClass().getName(),
				" ReConnector Instantiated for SMSC [" + this.circle + "] ");

	}// End Of Constructor

	public void safeStart() {
		if (this.isAlive() == false && this.getState().ordinal() == Thread.State.NEW.ordinal()) {
			this.start();
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" Connection Retrying safely started for SMSC [" + this.circle + "] ");
		}
	}// End Of Method

	public boolean isConnected() {
		return this.status;
	}// End Of Method

	public String getCircle() {
		return this.circle;
	}// End Of Method

	@Override
	public void run() {

		try {

			SMSC smsc = new SMSC();
			this.config.setCircle(this.circle);
			smsc.setConfig(this.config);
			smsc.setFormat(Pusher.getGatewayBoImpl().getFormatDetails(this.config.getCid().toString()));

			if (CoreUtils.getProtocol() == CoreEnums.Protocol.SMPP) {

				ESME esme = new ESME(config);

				if (esme.isConnected()) {
					smsc.setSmppGateway(esme);
					smsc.setHttpGateway(new HttpGateway(config.getTimeout()));
					QueueManager.addSMSC(this.circle, smsc);
					this.status = true;
				} else {

					esme = this.reconnect(ReConnector.MinTimeOut);

					if (this.status && esme != null) {
						smsc.setSmppGateway(esme);
						smsc.setHttpGateway(new HttpGateway(config.getTimeout()));
						QueueManager.addSMSC(this.circle, smsc);
					} else {
						Logger.sysLog(LogValues.fatal, this.getClass().getName(),
								" SMSC [" + this.circle + "] Connection Failed ");
					}

				} // End Of Connected ESME

			} else {
				smsc.setHttpGateway(new HttpGateway(config.getTimeout()));
				QueueManager.addSMSC(this.config.getCircle(), smsc);
			} // End Of Protocol Check

			try {
				Thread.sleep(150);
			} catch (Exception se) {
			}

			ReConnector.Trying.remove(circle);

		} catch (SecurityException se) {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" SMSC Connection Initialization with Circle " + config.getCircle() + " Interrupted & Closed ");
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Gateway with Circle " + config.getCircle() + " | Initialization ERROR \n" + Logger.getStack(e));
		} // End Of Try Catch

	}// End Of Thread

	private ESME reconnect(long timeout) {

		boolean reconnect = true;
		long totalRetried = 0L;
		this.status = false;

		while (reconnect) {

			try {
				totalRetried += timeout;
				Thread.sleep(timeout);

				ESME esme = new ESME(config);

				if (esme.isConnected() == false) {
					Logger.sysLog(LogValues.warn, this.getClass().getName(),
							" Unable to Re-establish SMSC [" + this.circle + "]  --- (Retrying) --- ");
				} else {
					reconnect = false;
					this.status = true;
					return esme;
				} // End of IF Connected

			} catch (InterruptedException ie) {
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" Unable to Re-establish SMSC Connection  |  Circle=" + this.circle
								+ "  |  Connection Interrupted  |  Retrying Stopped ");
				reconnect = false;
				break;
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), e.getMessage()
						+ "\n --- Unable to Re-establish SMSC [" + this.circle + "] Connection --- (Retrying) --- ");
			} // End Of Try Catch

			if (timeout < ReConnector.MaxTimeOut) {
				timeout *= 2;
			}

			/** DO NOT Stop Retrying if BindMode is RX */
			if (totalRetried > ReConnector.KillTimeout && this.config.getBindMode() != BindType.BIND_RX.ordinal()) {
				Logger.sysLog(LogValues.fatal, this.getClass().getName(),
						" Unable to Re-establish SMSC Connection  |  Circle=" + this.circle
								+ "  | Retry Timeout  |  Retrying Stopped ");
				reconnect = false;
				this.status = false;
				break;
			}

		} // End Of Loop

		return null;

	}// End Of Method

	/**
	 * if Reconnection already running or established, Then return TRUE else Start
	 * Reconnection and Return
	 * 
	 * It DOES NOT WAIT for reconnection to finish
	 */
	public static boolean reconnect(String circle) {

		ReConnector connector = null;

		if (circle == null) {
			return false;
		} else if (ReConnector.Trying.containsKey(circle)) {
			return true;
		} else if (CoreUtils.isSMSCActive(circle)) {
			return true;
		} else {

			try {
				connector = new ReConnector(circle);
				connector.safeStart();
				return true;
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, ReConnector.class.getName(), e.getMessage()
						+ " :: Unable to Re-connect to SMSC |  Circle=" + circle + "  |  Retrying Stopped ");
				return false;
			} // End Of Try Catch

		} // End Of If ELSE

	}// End Of Method

	/**
	 * if Reconnection already running, Then returns the re-connector thread else
	 * start Reconnection an
	 * 
	 * d then the re-connector thread
	 */
	public static ReConnector getReconnector(String circle) {

		ReConnector connector = null;

		if (circle == null)
			return null;
		else if (ReConnector.Trying.containsKey(circle)) {
			connector = ReConnector.Trying.get(circle);
		} else if (QueueManager.containsSMSC(circle)) {
			return null;
		} else if (connector == null) {
			try {
				connector = new ReConnector(circle);
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, ReConnector.class.getName(), e.getMessage());
				connector = null;
			} // End Of Try Catch
		} // End Of IF Else

		return connector;

	}// End Of Method

	public static void killAll() {

		Set<String> circles = ReConnector.Trying.keySet();

		if (circles != null) {

			Iterator<String> iter = circles.iterator();
			ReConnector conn = null;
			String circle;

			while (iter.hasNext()) {

				circle = iter.next();
				conn = ReConnector.Trying.get(circle);

				conn.interrupt();
				circle = null;
				conn = null;

			} // End Of While Loop

		} // End Of Circle Check

		ReConnector.Trying.clear();

	}// End Of Method

}// End Of Class
