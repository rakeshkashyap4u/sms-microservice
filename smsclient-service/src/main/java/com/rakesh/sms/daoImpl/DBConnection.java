package com.rakesh.sms.daoImpl;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rakesh.sms.bo.UtilityBo;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class DBConnection {

    @Autowired
    private UtilityBo properties;

    private static int DBcount;
    private static Map<String, SessionFactory> sessionfactorylist;

    public void setSessionfactorylist(Map<String, SessionFactory> sessionfactorylist) {
        Logger.sysLog(LogValues.info, DBConnection.class.getName(), "DBConnection Successfully Established");
        DBConnection.sessionfactorylist = sessionfactorylist;
    }

    public void setDBcount(int dBcount) {
        DBConnection.DBcount = dBcount;
    }

    public UtilityBo getProperties() {
        return properties;
    }

    public void setProperties(UtilityBo properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        // 1. Load properties from DB
        this.properties.loadProperties();

        // 2. Set static UtilityBo in CoreUtils
        CoreUtils.setUtilityBo(this.properties);

        Logger.sysLog(LogValues.info, DBConnection.class.getName(), "SMS Properties Loaded Successfully");
    }

    @PreDestroy
    public void destroy() {
        try {
            Logger.sysLog(LogValues.info, DBConnection.class.getName(), "Closing DB Connections & Session Factories");
            for (int i = 0; i < DBConnection.DBcount; i++) {
                SessionFactory sessionFactory = DBConnection.getSessionFactory(Integer.toString(i));
                if (sessionFactory != null) {
                    sessionFactory.close();
                }
            }
            CoreUtils.clear();
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, DBConnection.class.getName(), Logger.getStack(e));
        }
    }

    public static SessionFactory getSessionFactory(String key) {
        try {
            int dbNumber = Integer.parseInt(key);
            if (dbNumber >= 0 && dbNumber < DBConnection.DBcount) {
                return DBConnection.sessionfactorylist.get(key);
            }
        } catch (Exception e) {
            Logger.sysLog(LogValues.error, DBConnection.class.getName(),
                    "Unable to get Session Factory \n" + Logger.getStack(e));
        }
        return DBConnection.sessionfactorylist.get("0");
    }
}
