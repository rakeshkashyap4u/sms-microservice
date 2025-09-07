package com.rakesh.sms.beans;


import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "soapenv:Envelope")
public class OrangeEgyptResponse {

	@XmlElement(name = "status")
	private String status;

	@XmlElement(name = "errorCode")
	private String errorCode;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
