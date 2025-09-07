package com.rakesh.sms.webService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@WebService(name = "SmsService", targetNamespace = "http://sms.bng.com")
public class SmsSubscriptionInterface {

	SmsSubscription smsSubscription;

	@WebMethod(action = "subscribe_unsubscribe", operationName = "ClientSubscribe")
	public @WebResult(name = "boolean") boolean subscribe(@WebParam(name = "parameters") Parameters[] parameters) {

		try {
			Logger.sysLog(LogValues.info, this.getClass().getName(), "Subscribe user: Parameters received : ");
			for (int i = 0; i < parameters.length; i++) {
				Logger.sysLog(LogValues.info, this.getClass().getName(), parameters[i].toString());
			}
			smsSubscription = new SmsSubscription();
			return smsSubscription.subscribe(parameters);
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			return false;
		}
	}

	@WebMethod(action = "unsubscribe", operationName = "ClientUnsubscribe")
	public @WebResult(name = "boolean") boolean unsubscribe(@WebParam(name = "parameters") Parameters[] parameters) {

		try {
			Logger.sysLog(LogValues.info, this.getClass().getName(), "Unubscribe user: Parameters received : ");
			for (int i = 0; i < parameters.length; i++) {
				Logger.sysLog(LogValues.info, this.getClass().getName(), parameters[i].toString());
			}
			smsSubscription = new SmsSubscription();
			return smsSubscription.unsubscribe(parameters);
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			return false;
		}
	}

	@WebMethod(action = "fetch_alerts", operationName = "ClientAlerts")
	public @WebResult(name = "alerts") ServiceInfo[] getServices(
			@WebParam(name = "parameters") Parameters[] parameters) {

		try {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					"User Alerts: Parameters received : " + parameters.toString());
			for (int i = 0; i < parameters.length; i++) {
				Logger.sysLog(LogValues.info, this.getClass().getName(), parameters[i].toString());
			}
			smsSubscription = new SmsSubscription();
			return smsSubscription.getServices(parameters);
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
			return null;
		}
	}

}
