package com.bng.sms.queue;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.transport.TransportListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.rakesh.sms.cdr.CdrCreator;
import com.rakesh.sms.controller.PromotionScheduler;
import com.rakesh.sms.main.ConnectionChecker;
import com.rakesh.sms.scheduler.BlackoutHourMonitor;
import com.rakesh.sms.scheduler.StartScheduler;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class ActiveMQConnection implements TransportListener {

	@Autowired
	private QueueManager manager;

	private static PooledConnectionFactory pooledConnectionFactory;
	private ConnectionChecker rxConnectionChecker;
	private BlackoutHourMonitor bhThread;
	private StartScheduler scheduler;
	private Connection connection;
	private boolean connected;
	private Session session;
	private PromotionScheduler PromoScheduler;
	
	public void setPooledConnectionFactory(PooledConnectionFactory pooledConnectionFactory) {
		ActiveMQConnection.pooledConnectionFactory = pooledConnectionFactory;
	}

	public PooledConnectionFactory getPooledConnectionFactory() {
		return ActiveMQConnection.pooledConnectionFactory;
	}

	public void setManager(QueueManager manager) {
		this.manager = manager;
	}

	public QueueManager getManager() {
		return manager;
	}

	public void init() {

		try {

			this.connection = (Connection) ActiveMQConnection.pooledConnectionFactory.createConnection();
			this.connection.start();

			this.session = this.connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
			Logger.sysLog(LogValues.info, this.getClass().getName(), " All ActiveMQ Connections Established ");

			CdrCreator.init(this.session);

			SmsQueue.init(this.session);
			this.manager.start();

			this.scheduler = new StartScheduler();
			this.scheduler.start();

			this.bhThread = new BlackoutHourMonitor();
			this.bhThread.start();

			this.rxConnectionChecker = new ConnectionChecker();
			this.rxConnectionChecker.startChecking();
			
			this.PromoScheduler = new PromotionScheduler();
			this.PromoScheduler.start();

			Logger.sysLog(LogValues.info, this.getClass().getName(), " SMSClient successfully started ");

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

	}// End of init-method

	public boolean isConnectionAlive() {
		return this.connected;
	}// End Of Method

	public void onCommand(Object arg0) {
	}

	public void onException(IOException arg0) {
		this.connected = false;
	}

	public void transportInterupted() {
		this.connected = false;
	}

	public void transportResumed() {
		this.connected = true;
	}

	public void destroy() {

		this.manager.abort();
		StartScheduler.endJobs();
		CdrCreator.closeWriter();
		this.bhThread.interrupt();
		this.rxConnectionChecker.stopChecking();

		try {
			this.session.close();
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		try {
			this.connection.close();
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		ActiveMQConnection.pooledConnectionFactory.clear();

		Logger.sysLog(LogValues.info, this.getClass().getName(), " All ActiveMQ Connections Destroyed/Closed ");

	}// End of destroy-method

	public static Connection getNewConnection() {

		try {

			return (Connection) ActiveMQConnection.pooledConnectionFactory.createConnection();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, ActiveMQConnection.class.getName(), Logger.getStack(e));
		}

		return null;

	}// End Of Method

}
