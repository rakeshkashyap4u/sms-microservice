package com.rakesh.sms.beans;

public class Header {

	private String property;
	private String value;

	public Header() {
		this.property = "";
		this.value = "";
	}// End Of Constructor

	public Header(String property, String value) {
		this.property = property;
		this.value = value;
	}// End Of Constructor

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Property: " + this.property + " Value: " + this.value;
	}
}
