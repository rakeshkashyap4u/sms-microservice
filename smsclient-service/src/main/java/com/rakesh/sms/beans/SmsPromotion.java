package com.rakesh.sms.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.rakesh.sms.scheduler.Promotions;
import com.rakesh.sms.scheduler.StartScheduler;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.util.CoreEnums.Protocol;
import com.rakesh.sms.util.CoreEnums.SMSFlag;

public class SmsPromotion {

	private SMSFlag flag;
	private String message;
	private String jobName;
	private String circle;
	private String status;
	private String callerId;
	private String language;
	private String serviceid;
	private Protocol protocol;
	private List<String> base;
	private Date timestamp, starttime, expiry;
	private Thread promotion;

	public SmsPromotion(String jobName, String message, String callerId, String circle) {
		this.callerId = callerId;
		this.status = new String("Failure");
		this.jobName = jobName.replaceAll(" ", "_");
		this.protocol = Protocol.SMPP;
		this.flag = SMSFlag.UNKNOWN;
		this.message = message;
		this.serviceid = null;
		this.language = null;
		this.circle = circle;
		this.base = null;
	}// End Of Constructor

	public SmsPromotion(String message, String callerId, String circle) {
		this.jobName = new String("Untitled");
		this.status = new String("Failure");
		this.protocol = Protocol.SMPP;
		this.flag = SMSFlag.UNKNOWN;
		this.callerId = callerId;
		this.message = message;
		this.serviceid = null;
		this.language = null;
		this.circle = circle;
		this.base = null;
	}// End Of Constructor

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName.replaceAll(" ", "_");
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public List<String> getBase() {
		return base;
	}

	public void setBase(List<String> base) {
		this.base = base;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setCurrentTimestamp() {
		this.timestamp = Calendar.getInstance().getTime();
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public SMSFlag getFlag() {
		return flag;
	}

	public void setFlag(SMSFlag flag) {
		this.flag = flag;
	}

	public void setFlag(int flag) {
		this.flag = SMSFlag.values[flag];
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = Protocol.values[protocol];
	}

	public int baseSize() {
		if (this.base != null) {
			return this.base.size();
		} else
			return 0;
	}// End Of Method

	public boolean isSuccessful() {

		if (this.status == null || this.status.equalsIgnoreCase("Success"))
			return true;
		else
			return false;

	}// End Of Method

	public void startPromotions() {
		Promotions promo = new Promotions(this);
		this.promotion = new Thread(promo);
		StartScheduler.addJob(promo, this.promotion);
		this.promotion.start();
	}// End Of Method

	
	
	
	public String toString() {

		try {
			String json = CoreUtils.GSON.toJson(this);
			return json;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		return "{}";

	}// End Of Method

}
