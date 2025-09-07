package com.rakesh.sms.beans;

import com.rakesh.sms.util.CoreUtils;

public class AlertServiceDetails {

	private String serviceid;
	private String subserviceid;

	public AlertServiceDetails() {
		this(null, null);
	}// End Of Constructor

	public AlertServiceDetails(String serviceid, String subserviceid) {
		this.serviceid = serviceid;
		this.subserviceid = subserviceid;
	}// End Of Constructor

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getSubserviceid() {
		return subserviceid;
	}

	public void setSubserviceid(String subserviceid) {
		this.subserviceid = subserviceid == null ? "" : subserviceid.trim();
	}

	public String getServiceString() {
		return this.serviceid + ";" + this.subserviceid;
	}// End Of Method

	public String toString() {
		try {
			String json = CoreUtils.GSON.toJson(this);
			return json;
		} catch (Exception e) {
			//Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}
		return "{}";
	}// End Of Method

}
