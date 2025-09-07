package com.rakesh.sms.queue22;



import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import jakarta.jms.JMSException;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.transport.TransportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rakesh.sms.scheduler.BlackoutHourMonitor;


@Component
public class ActiveMQConnection implements TransportListener {

	
    private final QueueManager manager;
	
    private final ConnectionFactory connectionFactory;

   // private ConnectionChecker rxConnectionChecker;
	
    private BlackoutHourMonitor bhThread;
   // private StartScheduler scheduler;
	
    private Connection connection;
    private boolean connected;
    private Session session;
  //  private PromotionScheduler promoScheduler;

    // Constructor injection for Spring Boot
    @Autowired
    public ActiveMQConnection(QueueManager manager, ConnectionFactory connectionFactory) {
        this.manager = manager;
        this.connectionFactory = connectionFactory;
    }

    @PostConstruct
    public void init() {
        try {
            // JMS connection & session
            this.connection = connectionFactory.createConnection();
            this.connection.start();
            this.session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);

           // Logger.sysLog(LogValues.info, getClass().getName(), "All ActiveMQ Connections Established");

            // Initialize app-specific components
            //CdrCreator.init(this.session);
            SmsQueue.init(this.session);
            manager.start();

            // Start schedulers and monitors
           // scheduler = new StartScheduler();
           // scheduler.start();

            bhThread = new BlackoutHourMonitor();
            bhThread.start();

           // rxConnectionChecker = new ConnectionChecker();
           // rxConnectionChecker.startChecking();

           // promoScheduler = new PromotionScheduler();
           // promoScheduler.start();

            //Logger.sysLog(LogValues.info, getClass().getName(), "SMSClient successfully started");

        } catch (JMSException e) {
            //Logger.sysLog(LogValues.error, getClass().getName(), Logger.getStack(e));
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (Exception ignore) {}
        manager.abort();
    }


    // --- TransportListener methods ---
    @Override
    public void onCommand(Object arg0) { }

    @Override
    public void onException(java.io.IOException arg0) {
        this.connected = false;
    }

    @Override
    public void transportInterupted() {
        this.connected = false;
    }

    @Override
    public void transportResumed() {
        this.connected = true;
    }

    // --- Utilities ---
    public boolean isConnectionAlive() {
        return this.connected;
    }

    public Connection getNewConnection() {
        try {
            return connectionFactory.createConnection();
        } catch (JMSException e) {
           // Logger.sysLog(LogValues.error, getClass().getName(), Logger.getStack(e));
            return null;
        }
    }
}
