package com.rakesh.sms.beans;

import com.rakesh.sms.entity.CallbackDetails;
import com.google.gson.annotations.Expose;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class Callback {

	@Expose
	private String shortcode;
	@Expose
	private String messageId;
	@Expose
	private String channel;
	@Expose
	private String serviceid;
	@Expose
	private String subServiceid;
	@Expose
	private String action;
	@Expose
	private String transactionId;
	@Expose
	private String additionals;
	@Expose
	private String price;
	@Expose
	private String callbackStatus;
	@Expose
	private String failureReason;

	public Callback() {
		this.shortcode = this.messageId = this.serviceid = this.subServiceid = this.action = this.transactionId = this.additionals = this.price = this.callbackStatus = this.failureReason = new String(
				"");
	}// End Of Constructor

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getSubServiceid() {
		return subServiceid;
	}

	public void setSubServiceid(String subServiceid) {
		this.subServiceid = subServiceid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAdditionals() {
		return additionals;
	}

	public void setAdditionals(String additionals) {
		this.additionals = additionals;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCallbackStatus() {
		return callbackStatus;
	}

	public void setCallbackStatus(String callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public void set(CallbackDetails details) {

		this.action = details.getAction();
		this.serviceid = details.getServiceid();
		this.subServiceid = details.getSubServiceid();
		this.additionals = details.getAdditionals();

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
