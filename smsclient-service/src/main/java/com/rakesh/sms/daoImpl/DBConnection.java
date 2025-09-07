package com.rakesh.sms.daoImpl;

import java.util.Map;

import org.hibernate.SessionFactory;

import com.rakesh.sms.bo.UtilityBo;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class DBConnection {

	private UtilityBo properties;

	private static int DBcount;
	private static Map<String, SessionFactory> sessionfactorylist;

	public void setSessionfactorylist(Map<String, SessionFactory> sessionfactorylist) {
		Logger.sysLog(LogValues.info, DBConnection.class.getName(), " DBConnection Successfully Established ");
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

	public void init() {

		this.properties.loadProperties();
		Logger.sysLog(LogValues.info, DBConnection.class.getName(), " SMS Properties Loaded Successfully ");

	}// End Of init-method

	public void destroy() {

		try {

			Logger.sysLog(LogValues.info, DBConnection.class.getName(), " Closing DB Connections & Session Factories ");
			for(int i = 0 ; i< DBConnection.DBcount ; i++) {
				DBConnection.getSessionFactory(Integer.toString(i)).close();
			}
			CoreUtils.clear();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, DBConnection.class.getName(), Logger.getStack(e));
		}

	}// End Of destroy-method

	public static SessionFactory getSessionFactory(String key) {

		try {

			int dbNumber = Integer.parseInt(key);

			if (dbNumber >= 0 && dbNumber < DBConnection.DBcount)
				return DBConnection.sessionfactorylist.get(key);

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, DBConnection.class.getName(),
					" Unable to get Session Factory \n" + Logger.getStack(e));
		}

		return DBConnection.sessionfactorylist.get("0");

	}// End Of Method

}// End Of Class
