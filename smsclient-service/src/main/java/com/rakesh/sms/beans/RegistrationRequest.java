package com.rakesh.sms.beans;

import java.io.Serializable;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "registrationRequest")
public class RegistrationRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String requestFormat;
	private List<Header> headers;
	private String registrationURL;
	private String deregistrationURL;
	private String registerEachService;
	private String deregisterRequestFormat;

	@XmlElement(name = "registrationURL")
	public String getRegistrationURL() {
		return registrationURL;
	}

	public void setRegistrationURL(String registrationURL) {
		this.registrationURL = registrationURL;
	}

	@XmlElement(name = "deregistrationURL")
	public String getDeregistrationURL() {
		return deregistrationURL;
	}

	public void setDeregistrationURL(String deregistrationURL) {
		this.deregistrationURL = deregistrationURL;
	}

	@XmlElement(name = "header", type = Header.class)
	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}

	@XmlElement(name = "requestFormat")
	public String getRequestFormat() {
		return requestFormat;
	}

	public void setRequestFormat(String requestFormat) {
		this.requestFormat = requestFormat;
	}

	@XmlElement(name = "registerEachService")
	public String getRegisterEachService() {
		return registerEachService;
	}

	public void setRegisterEachService(String registerEachService) {
		this.registerEachService = registerEachService;
	}

	@XmlElement(name = "deregisterRequestFormat")
	public String getDeregisterRequestFormat() {
		return deregisterRequestFormat;
	}

	public void setDeregisterRequestFormat(String deregisterRequestFormat) {
		this.deregisterRequestFormat = deregisterRequestFormat;
	}
}
