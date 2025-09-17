package com.rakesh.sms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rakesh.sms.bo.GatewayBo;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.main.ReConnector;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

import jakarta.annotation.PostConstruct;

@Component
public class ReConnectorStarter {

    @Autowired
    private GatewayBo gatewayBo; // or your DAO/service to fetch SMSCConfigs

    @PostConstruct
    public void init() {
        // fetch all SMSC configurations from DB
        List<SMSCConfigs> configs = gatewayBo.getAllCircleDetails();
        
        for (SMSCConfigs config : configs) {
            try {
                ReConnector connector = new ReConnector(config); // create thread dynamically
                connector.safeStart(); // start the thread
            } catch (Exception e) {
                Logger.sysLog(LogValues.error, this.getClass().getName(),
                        "Failed to start ReConnector for circle " + config.getCircle() + " | " + e.getMessage());
            }
        }
    }
}

