package com.rakesh.sms.queue;



import java.io.IOException;

import jakarta.jms.Connection;
import jakarta.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.rakesh.sms.controller.PromotionScheduler;
import com.rakesh.sms.main.ConnectionChecker;
import com.rakesh.sms.scheduler.BlackoutHourMonitor;
import com.rakesh.sms.scheduler.StartScheduler;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

/**
 * Handles pooled JMS connections with Artemis + Jakarta JMS.
 */
public class ActiveMQConnection implements jakarta.jms.ExceptionListener {

    @Autowired
    private QueueManager manager;

    private static JmsPoolConnectionFactory pooledConnectionFactory;
    private ConnectionChecker rxConnectionChecker;
    private BlackoutHourMonitor bhThread;
    private StartScheduler scheduler;
    private Connection connection;
    private boolean connected;
    private Session session;
    private PromotionScheduler promoScheduler;

    public void setPooledConnectionFactory(JmsPoolConnectionFactory factory) {
        ActiveMQConnection.pooledConnectionFactory = factory;
    }

    public JmsPoolConnectionFactory getPooledConnectionFactory() {
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
            this.connection = ActiveMQConnection.pooledConnectionFactory.createConnection();
            this.connection.setExceptionListener(this);
            this.connection.start();

            this.session = this.connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
            Logger.sysLog(LogValues.info, this.getClass().getName(), "All Artemis JMS Connections Established");

           // CdrCreator.init(this.session);
           // SmsQueue.init(this.session);
            this.manager.start();

            this.scheduler = new StartScheduler();
            this.scheduler.start();

            this.bhThread = new BlackoutHourMonitor();
            this.bhThread.start();

            this.rxConnectionChecker = new ConnectionChecker();
            this.rxConnectionChecker.startChecking();

            this.promoScheduler = new PromotionScheduler();
            this.promoScheduler.start();

            this.connected = true;
            Logger.sysLog(LogValues.info, this.getClass().getName(), "SMSClient successfully started");

        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
        }
    }

    public boolean isConnectionAlive() {
        return this.connected;
    }

    @Override
    public void onException(jakarta.jms.JMSException exception) {
        this.connected = false;
        Logger.sysLog(LogValues.error, this.getClass().getName(), "JMS Exception: " + exception.getMessage());
    }

    public void destroy() {
        this.manager.abort();
        StartScheduler.endJobs();
       // CdrCreator.closeWriter();
        if (this.bhThread != null) this.bhThread.interrupt();
        if (this.rxConnectionChecker != null) this.rxConnectionChecker.stopChecking();

        try {
            if (this.session != null) this.session.close();
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
        }

        try {
            if (this.connection != null) this.connection.close();
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
        }

        if (ActiveMQConnection.pooledConnectionFactory != null) {
            ActiveMQConnection.pooledConnectionFactory.stop();
        }

        Logger.sysLog(LogValues.info, this.getClass().getName(), "All Artemis JMS Connections Destroyed/Closed");
    }

    public static Connection getNewConnection() {
        try {
            return ActiveMQConnection.pooledConnectionFactory.createConnection();
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, ActiveMQConnection.class.getName(), Logger.getStack(e));
        }
        return null;
    }
}
