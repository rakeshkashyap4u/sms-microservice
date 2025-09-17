package com.rakesh.sms;

import jakarta.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rakesh.sms.queue.SimpleActiveMQConnection;
import com.rakesh.sms.queue.SmsQueue;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

import jakarta.annotation.PostConstruct;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmsQueueInitializer {

    @Autowired
    private SimpleActiveMQConnection simpleActiveMQConnection;

    public void initQueue() {
        System.out.println("SmsQueueInitializer initQueue running...");
        try {
            Session session = (Session) simpleActiveMQConnection.getSession();
            SmsQueue.init(session);
            System.out.println("SmsQueue initialized successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

