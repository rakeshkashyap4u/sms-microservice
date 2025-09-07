package com.rakesh.sms.webService;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "alert")
public class ServiceInfo {

	String shortcode;
	String serviceName;
	String unsubKeyword;
	String activationDate;

	public ServiceInfo() {

	}

	@XmlAttribute(name = "shortcode")
	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	@XmlAttribute(name = "subscription")
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@XmlAttribute(name = "unsubscribeKeyword")
	public String getUnsubKeyword() {
		return unsubKeyword;
	}

	public void setUnsubKeyword(String unsubKeyword) {
		this.unsubKeyword = unsubKeyword;
	}

	@XmlAttribute(name = "activationDate")
	public String getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}

}
