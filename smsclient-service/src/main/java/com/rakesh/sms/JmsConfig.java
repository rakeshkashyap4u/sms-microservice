package com.rakesh.sms;



import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.queue.SimpleActiveMQConnection;

@Configuration
public class JmsConfig {

    @Value("${activemq.broker-url:tcp://localhost:61616}")
    private String brokerUrl;

    @Bean
    public SimpleActiveMQConnection simpleActiveMQConnection() {
        SimpleActiveMQConnection conn = new SimpleActiveMQConnection();
        conn.init();
        return conn;
    }
    
//    @Bean
//    public MessageConverter jacksonJmsMessageConverter() {
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//
//        // Map type name to class
//        Map<String, Class<?>> typeIdMappings = new HashMap<>();
//        typeIdMappings.put("Message", Message.class);
//        converter.setTypeIdMappings(typeIdMappings);
//
//        // Use "_type" property if present, otherwise fallback
//        converter.setTypeIdPropertyName("_type");
//        return converter;
//    }

    
}
