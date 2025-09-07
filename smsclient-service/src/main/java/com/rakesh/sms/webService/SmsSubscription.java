package com.rakesh.sms.webService;

import java.util.List;

import javax.jws.WebService;

import com.rakesh.sms.cdr.CdrCreator;
import com.rakesh.sms.cdr.ReceivedSmsBean;
import com.rakesh.sms.main.SMPPMessageListener;
import com.rakesh.sms.main.SmsValidation;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@WebService(endpointInterface = "com.rakesh.sms.webService.SmsSubscriptionInterface", portName = "SmsServicePort", serviceName = "Sms")
public class SmsSubscription {

	public static boolean response = false;
	public static List<ServiceInfo> services = null;

	public boolean subscribe(Parameters[] parameters) throws InvalidRequestException {

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Subscribe user: Parameters received : ");
		for (int i = 0; i < parameters.length; i++) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), parameters[i].toString());
		}

		String msisdn = null;
		String keyword = null;
		String shortcode = null;

		ReceivedSmsBean cdr = new ReceivedSmsBean();

		try {

			for (int i = 0; parameters != null && i < parameters.length; i++) {
				if (parameters[i].getKey().equalsIgnoreCase("msisdn"))
					msisdn = parameters[i].getValue();

				if (parameters[i].getKey().equalsIgnoreCase("keyword"))
					keyword = parameters[i].getValue();

				if (parameters[i].getKey().equalsIgnoreCase("shortcode"))
					shortcode = parameters[i].getValue();
			}

			if (msisdn == null || msisdn.length() == 0)
				throw new InvalidRequestException("Invalid msisdn", "Required parameter msisdn is empty");

			if (keyword == null || keyword.length() == 0)
				throw new InvalidRequestException("Invalid msisdn", "Required parameter keyword is empty");

			if (shortcode == null || shortcode.length() == 0)
				throw new InvalidRequestException("Invalid shortcode", "Required parameter shortcode is empty");

			cdr.setTime(CoreUtils.getCurrentTimeStamp());
			cdr.setReceiverMsisdn(shortcode);
			cdr.setDeliveryReport(false);
			cdr.setStatus("Success");
			cdr.setContent(keyword);
			cdr.setSender(msisdn);

			String receivedMessage = SMPPMessageListener.getTrimmedMessage(keyword);
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" MO Received :: [" + msisdn + "][" + shortcode + "] ::  Message=" + keyword);

			try {
				SmsValidation validator = new SmsValidation();
				validator.parseAndValidate(receivedMessage, cdr);
				synchronized (validator) {
					validator.wait();
				}
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						"Exception in subUnsub soap call:" + Logger.getStack(e));
				throw new InvalidRequestException("Invalid request", "");
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					"Exception in subUnsub soap call:" + Logger.getStack(e));

			cdr.setTime(CoreUtils.getCurrentTimeStamp());
			cdr.setReceiverMsisdn(shortcode);
			cdr.setDeliveryReport(false);
			cdr.setFailedStatus();
			cdr.setContent(keyword);
			cdr.setSender(msisdn);

			CdrCreator.saveAsXML(cdr);
		}

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Returning " + response);
		return response;
	}

	public boolean unsubscribe(Parameters[] parameters) throws InvalidRequestException {

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Unubscribe user: Parameters received : ");
		for (int i = 0; i < parameters.length; i++) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), parameters[i].toString());
		}

		String msisdn = null;
		String keyword = null;
		String shortcode = null;

		ReceivedSmsBean cdr = new ReceivedSmsBean();

		try {

			for (int i = 0; parameters != null && i < parameters.length; i++) {
				if (parameters[i].getKey().equalsIgnoreCase("msisdn"))
					msisdn = parameters[i].getValue();

				if (parameters[i].getKey().equalsIgnoreCase("keyword"))
					keyword = parameters[i].getValue();

				if (parameters[i].getKey().equalsIgnoreCase("shortcode"))
					shortcode = parameters[i].getValue();
			}

			if (msisdn == null || msisdn.length() == 0)
				throw new InvalidRequestException("Invalid msisdn", "Required parameter msisdn is empty");

			if (keyword == null || keyword.length() == 0)
				throw new InvalidRequestException("Invalid msisdn", "Required parameter keyword is empty");

			if (shortcode == null || shortcode.length() == 0)
				throw new InvalidRequestException("Invalid shortcode", "Required parameter shortcode is empty");

			cdr.setTime(CoreUtils.getCurrentTimeStamp());
			cdr.setReceiverMsisdn(shortcode);
			cdr.setDeliveryReport(false);
			cdr.setStatus("Success");
			cdr.setContent(keyword);
			cdr.setSender(msisdn);

			String receivedMessage = SMPPMessageListener.getTrimmedMessage(keyword);
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" MO Received :: [" + msisdn + "][" + shortcode + "] ::  Message=" + keyword);

			try {
				SmsValidation validator = new SmsValidation();
				validator.parseAndValidate(receivedMessage, cdr);
				synchronized (validator) {
					validator.wait();
				}
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						"Exception in subUnsub soap call:" + Logger.getStack(e));
				throw new InvalidRequestException("Invalid request", "");
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					"Exception in subUnsub soap call:" + Logger.getStack(e));

			cdr.setTime(CoreUtils.getCurrentTimeStamp());
			cdr.setReceiverMsisdn(shortcode);
			cdr.setDeliveryReport(false);
			cdr.setFailedStatus();
			cdr.setContent(keyword);
			cdr.setSender(msisdn);

			CdrCreator.saveAsXML(cdr);
		}

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Returning " + response);
		return response;
	}

	public ServiceInfo[] getServices(Parameters[] parameters) throws InvalidRequestException {

		Logger.sysLog(LogValues.info, this.getClass().getName(),
				"User Alerts: Parameters received : " + parameters.toString());
		for (int i = 0; i < parameters.length; i++) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), parameters[i].toString());
		}

		ServiceInfo[] serviceArray = null;

		services = null;

		String msisdn = null;
		String shortcode = null;

		ReceivedSmsBean cdr = new ReceivedSmsBean();

		try {

			for (int i = 0; parameters != null && i < parameters.length; i++) {
				if (parameters[i].getKey().equalsIgnoreCase("msisdn"))
					msisdn = parameters[i].getValue();
				if (parameters[i].getKey().equalsIgnoreCase("shortcode"))
					shortcode = parameters[i].getValue();
			}

			if (msisdn == null || msisdn.length() == 0)
				throw new InvalidRequestException("Invalid msisdn", "Required parameter msisdn is empty");

			if (shortcode == null || shortcode.length() == 0)
				throw new InvalidRequestException("Invalid shortcode", "Required parameter shortcode is empty");

			cdr.setTime(CoreUtils.getCurrentTimeStamp());
			cdr.setReceiverMsisdn(shortcode);
			cdr.setDeliveryReport(false);
			cdr.setStatus("Success");
			cdr.setContent("info");
			cdr.setSender(msisdn);

			String receivedMessage = SMPPMessageListener.getTrimmedMessage("info");
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" MO Received :: [" + msisdn + "][" + shortcode + "] ::  Message=info");

			try {
				SmsValidation validator = new SmsValidation();
				validator.parseAndValidate(receivedMessage, cdr);

				synchronized (validator) {
					validator.wait();
				}

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						"Exception in subUnsub soap call:" + Logger.getStack(e));
				throw new InvalidRequestException("Invalid request", "");
			}

			if (services != null)
				serviceArray = new ServiceInfo[services.size()];

			for (int j = 0; services != null && j < services.size(); j++) {
				serviceArray[j] = services.get(j);
			}

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), "Exception : " + Logger.getStack(e));

			cdr.setTime(CoreUtils.getCurrentTimeStamp());
			cdr.setReceiverMsisdn(shortcode);
			cdr.setDeliveryReport(false);
			cdr.setFailedStatus();
			cdr.setContent("info");
			cdr.setSender(msisdn);

			CdrCreator.saveAsXML(cdr);
		}

		return serviceArray;
	}
}
