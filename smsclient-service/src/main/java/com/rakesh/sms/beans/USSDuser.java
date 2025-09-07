package com.rakesh.sms.beans;

import java.net.URLEncoder;
import java.util.HashMap;

import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class USSDuser {

	class Option {

		private String dtmf;
		private String name;
		private String serviceid;

		String getDtmf() {
			return dtmf;
		}

		void setDtmf(String dtmf) {
			this.dtmf = dtmf;
		}

		String getName() {
			return name;
		}

		void setName(String name) {
			this.name = name;
		}

		String getServiceid() {
			return serviceid;
		}

		void setServiceid(String serviceid) {
			this.serviceid = serviceid;
		}

	}// End Of Class

	private HashMap<Integer, Option> optionList;
	private String successUrl, failureUrl;
	private String specialUrl, specialOn;
	private String action, shortcode;
	private String extraInfo;
	private int sessionId;
	private int serviceOp;
	private String message;
	private String msisdn;
	private String type;
	private long expiry;

	public USSDuser() {
		this.optionList = new HashMap<Integer, Option>(10);
		this.expiry = System.currentTimeMillis();
	}// End Of Constructor

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getExpiry() {
		return expiry;
	}

	public void addSecondsToExpiry(long offset) {
		this.expiry += (offset * 1000L);
	}

	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	public String getSpecialUrl() {
		return specialUrl;
	}

	public void setSpecialUrl(String specialUrl) {
		this.specialUrl = specialUrl;
	}

	public String getSpecialOn() {
		return specialOn;
	}

	public void setSpecialOn(String specialOn) {
		this.specialOn = specialOn;
	}

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public int getServiceOp() {
		return serviceOp;
	}

	public void setServiceOp(int serviceOp) {
		this.serviceOp = serviceOp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public void addOption(String dtmf, String serviceName, String serviceId) {

		Option opt = new Option();

		try {

			int dtmfVal = Integer.parseInt(dtmf);
			opt.setDtmf(dtmf);

			if (serviceName != null && serviceName.length() > 0) {
				opt.setName(serviceName.trim());
			} else {
				throw new Exception(" NULL Service Name ");
			}

			if (serviceId != null && serviceId.length() > 0) {
				opt.setServiceid(serviceId.trim());
			} else {
				throw new Exception(" NULL Service ID ");
			}

			this.optionList.put(Integer.valueOf(dtmfVal), opt);

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Unable to add USSD option :: " + e.getMessage());
		} // End of Try Catch

	}// End Of Method

	public int isValidOption(String dtmf) {

		try {

			Logger.sysLog(LogValues.debug, this.getClass().getName(),
					" USSD Option: " + URLEncoder.encode(dtmf, "UTF-8"));
			Integer value = Integer.parseInt(dtmf);

			if (this.optionList.containsKey(value) == true)
				return value.intValue();
			else
				return -1;

		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, this.getClass().getName(), " Invalid USSD Option :: " + dtmf);
		} // End Of Try Catch
		return -1;

	}// End Of Method

	public String replaceAction(String input) {
		if (this.action != null && this.action.length() > 0) {
			return input.replaceAll("\\$action\\$", this.action);
		}
		return input;
	}// End Of Method

	public int getTotalOptions() {
		return this.optionList.size();
	}// End Of Method

	public String getServiceName(int itemNumber) {
		return this.optionList.get(Integer.valueOf(itemNumber)).getName();
	}// End Of Method

	public String getServiceId(int itemNumber) {
		return this.optionList.get(Integer.valueOf(itemNumber)).getServiceid();
	}// End Of Method

	public String getServiceId() {
		Integer key = (Integer) this.optionList.keySet().iterator().next();
		return this.optionList.get(key).getServiceid();
	}// End Of Method

}
