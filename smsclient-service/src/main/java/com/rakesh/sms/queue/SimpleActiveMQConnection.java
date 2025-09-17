package com.rakesh.sms.queue;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.stereotype.Component;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;

@Component
public class SimpleActiveMQConnection {

    private Connection connection;
    private Session session;
    private String brokerUrl = "tcp://localhost:61616";
    private boolean usePooling = true;
    private JmsPoolConnectionFactory pooledFactory;

    public SimpleActiveMQConnection() {
        init();
    }

    public void init() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);

            if (usePooling) {
                pooledFactory = new JmsPoolConnectionFactory();
                pooledFactory.setConnectionFactory(factory);
                pooledFactory.setMaxConnections(10);
                factory = pooledFactory;
            }

            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            System.out.println("JMS Connection and Session established successfully. Pooling: " + usePooling);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Queue createQueue(String queueName) throws JMSException {
        return session.createQueue(queueName);
    }

    public MessageProducer createProducer(Queue queue) throws JMSException {
        return session.createProducer(queue);
    }

    public MessageConsumer createConsumer(Queue queue) throws JMSException {
        return session.createConsumer(queue);
    }

    public void destroy() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
            if (pooledFactory != null) pooledFactory.stop();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }
}
