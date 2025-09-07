package com.rakesh.sms.beans;

import jakarta.xml.bind.annotation.XmlElement;



public class MTInfoResponse {

	
	private String tid;
	private String status_id;
	private String messgae;
	private String sdc;
	
	@XmlElement(name = "tid")
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	
	@XmlElement(name = "status-id")
	public String getStatus_id() {
		return status_id;
	}
	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}
	
	@XmlElement(name = "message")
	public String getMessgae() {
		return messgae;
	}
	public void setMessgae(String messgae) {
		this.messgae = messgae;
	}
	
	@XmlElement(name = "sdc")
	public String getSdc() {
		return sdc;
	}
	public void setSdc(String sdc) {
		this.sdc = sdc;
	}

}
