package com.rakesh.sms.beans;

import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class ConsentDetails {

	/**
	 * -----DETAILS----- timeoutInSec: USSD Session Time. IF Response Received after
	 * this will be Rejected url: On Receiving USSD Request... Sync Request URL
	 * required to process the request internally responseType: Based on the above
	 * URL and its response. Define response Type to process the response
	 * smsResponseUrl: On getting the response, SMS URL to Reply User [Might contain
	 * response of 'url'] and WAIT for User DTMF successActionUrl: On receiving
	 * correct DTMF, request this URL failureActionUrl: On receiving incorrect DTMF,
	 * request this URL specialActionUrl: On receiving DTMF, make a
	 * special/different request than Success URL specialActionOn: Defining the
	 * details, when to take the above special action (First/Last DTMF Option)
	 * onDemandAction: To manually define $action$ parameter in Action URLs
	 * 
	 **/

	private String url;
	private Long timeoutInSec;
	private String responseType;
	private String smsResponseUrl;
	private String successActionUrl;
	private String failureActionUrl;
	private String specialActionUrl;
	private String specialActionOn;
	private String action;

	public String getUrl() {
		return this.url != null ? this.url : "";
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getTimeoutInSec() {
		return timeoutInSec;
	}

	public void setTimeoutInSec(Long timeoutInSec) {
		this.timeoutInSec = timeoutInSec;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getSmsResponseUrl() {
		return smsResponseUrl;
	}

	public void setSmsResponseUrl(String smsResponseUrl) {
		this.smsResponseUrl = smsResponseUrl;
	}

	public String getSuccessActionUrl() {
		return successActionUrl;
	}

	public void setSuccessActionUrl(String successActionUrl) {
		this.successActionUrl = successActionUrl;
	}

	public String getFailureActionUrl() {
		return failureActionUrl;
	}

	public void setFailureActionUrl(String failureActionUrl) {
		this.failureActionUrl = failureActionUrl;
	}

	public String getSpecialActionUrl() {
		return specialActionUrl;
	}

	public void setSpecialActionUrl(String specialActionUrl) {
		this.specialActionUrl = specialActionUrl;
	}

	public String getSpecialActionOn() {
		return specialActionOn;
	}

	public void setSpecialActionOn(String specialActionOn) {
		this.specialActionOn = specialActionOn;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String replaceAction(String input) {
		if (this.action != null && this.action.length() > 0) {
			return input.replaceAll("\\$action\\$", this.action);
		}
		return input;
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
