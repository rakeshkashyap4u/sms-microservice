package com.rakesh.sms.beans;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;



public class RequestFormat {

	private List<Header> header;
	private String request;

	@XmlElement(name = "header")
	public List<Header> getHeader() {
		return header;
	}

	@Override
	public String toString() {
		return "RequestFormat [header=" + header.toString() + ", request=" + request + "]";
	}

	public void setHeader(List<Header> header) {
		this.header = header;
	}

	@XmlElement(name = "request")
	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}
}
