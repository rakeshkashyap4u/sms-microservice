package com.rakesh.sms.beans;

import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Tag;

import com.google.gson.annotations.Expose;

public class SessionParameters {

	@Expose
	private int itsSessionInfo;
	@Expose
	private int ussdServiceOp;
	@Expose
	private int sessionInactive;

	public int getItsSessionInfo() {
		return itsSessionInfo;
	}

	public void setItsSessionInfo(int itsSessionInfo) {
		this.itsSessionInfo = itsSessionInfo;
	}

	public int getUssdServiceOp() {
		return ussdServiceOp;
	}

	public void setUssdServiceOp(int ussdServiceOp) {
		this.ussdServiceOp = ussdServiceOp;
	}

	public int getSessionInactive() {
		return sessionInactive;
	}

	public void setSessionInactive(int sessionInactive) {
		this.sessionInactive = sessionInactive;
	}

	public OptionalParameter getItsSessionInfoObject() {

		byte[] content = { (byte) itsSessionInfo, (byte) sessionInactive };

		return new OptionalParameter.Short(Tag.ITS_SESSION_INFO.code(), content);
	}

	public OptionalParameter getUssdServiceOpObject() {
		return new OptionalParameter.Byte(Tag.USSD_SERVICE_OP, (byte) ussdServiceOp);
	}

}// End Of Class
