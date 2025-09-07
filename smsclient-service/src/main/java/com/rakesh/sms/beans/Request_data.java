package com.rakesh.sms.beans;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "request_data")
public class Request_data {
	
	private int integrator_id;
//	private String access_token;
	private String identifier;
	private int message_id;
	private int product_id;
	private String shortcode;
	private int result_code;
	private String message;
	
	@XmlElement(name = "message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@XmlElement(name = "integrator_id")
	public int getIntegrator_id() {
		return integrator_id;
	}
	
	public void setIntegrator_id(int integrator_id) {
		this.integrator_id = integrator_id;
	}
	
	@XmlElement(name = "identifier")
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@XmlElement(name = "message_id")
	public int getMessage_id() {
		return message_id;
	}
	
	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}
	
	@XmlElement(name = "product_id")
	public int getProduct_id() {
		return product_id;
	}
	
	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}
	
	@XmlElement(name = "shortcode")
	public String getShortcode() {
		return shortcode;
	}
	
	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}
	
	@XmlElement(name = "result_code")
	public int getResult_code() {
		return result_code;
	}
	
	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}

	@Override
	public String toString() {
		return "Request_data [integrator_id=" + integrator_id + ", identifier=" + identifier + ", message_id="
				+ message_id + ", product_id=" + product_id + ", shortcode=" + shortcode + ", message=" + message
				+ ", result_code=" + result_code + "]";
	}	
	
}
