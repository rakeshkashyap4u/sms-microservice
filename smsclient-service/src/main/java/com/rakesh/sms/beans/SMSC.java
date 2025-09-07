package com.rakesh.sms.beans;

import com.rakesh.sms.main.ESME;
import com.rakesh.sms.main.Gateway;
import com.rakesh.sms.main.HttpGateway;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class SMSC {

	private ESME smppGateway;
	private SMSCConfigs config;
	private SMSCFormats format;
	private HttpGateway httpGateway;
	private RegistrationRequest requestMO, requestMT;

	public ESME getSmppGateway() {
		return smppGateway;
	}

	public void setSmppGateway(ESME smppGateway) {
		this.smppGateway = smppGateway;
	}

	public SMSCConfigs getConfig() {
		return config;
	}

	public void setConfig(SMSCConfigs config) {
		this.config = config;
	}

	public SMSCFormats getFormat() {
		return format;
	}

	public void setFormat(SMSCFormats format) {
		this.format = format;
	}

	public HttpGateway getHttpGateway() {
		return httpGateway;
	}

	public void setHttpGateway(HttpGateway httpGateway) {
		this.httpGateway = httpGateway;
	}

	public RegistrationRequest getRequestMO() {
		return requestMO;
	}

	public void setRequestMO(RegistrationRequest requestMO) {
		this.requestMO = requestMO;
	}

	public RegistrationRequest getRequestMT() {
		return requestMT;
	}

	public void setRequestMT(RegistrationRequest requestMT) {
		this.requestMT = requestMT;
	}

	public String getCircle() {
		if (this.config != null)
			return this.config.getCircle();
		else {
			Logger.sysLog(LogValues.info, this.getClass().getName(), " SMSC Configuration NOT FOUND ");
			return "";
		}
	}// End Of Method

	public Gateway getGateway() {

		if (CoreUtils.getProtocol() == CoreEnums.Protocol.SMPP) {
			return this.smppGateway;
		} else {
			return this.httpGateway;
		}

	}// End Of Method

	public void shutdown() {
		try {
			if (CoreUtils.getProtocol() == CoreEnums.Protocol.SMPP) {
				this.smppGateway.close();
				this.httpGateway.close();
			} else {
				if (this.getFormat().getRegister().equals("1"))
					//this.getHttpGateway().deregister(requestMO);
			//	this.getHttpGateway().deregister(requestMT);
				this.httpGateway.close();
			}
		} catch (Exception e) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Error Shutting SMSC " + e.getMessage());
		} // End Of Try Catch
	}// End Of Method

}
