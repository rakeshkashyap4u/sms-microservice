package com.rakesh.sms.webService;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "parameters")
public class Parameters {

	private String key;
	private String value;

	public Parameters() {

	}

	@XmlElement(name = "key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@XmlElement(name = "value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.key + ":" + this.value;
	}

}
