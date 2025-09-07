package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
;

@Entity
@Table(name = "smswhitelist")
public class SMSWhitelist {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "shortcode")
	private String shortcode;

	@Column(name = "msisdn")
	private String msisdn;

	@Column(name = "isSeries")
	private Byte isseries;

	@Column(name = "msgType")
	private String msgType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public boolean isSeries() {
		return isseries.byteValue() == 0 ? false : true;
	}

	public void setIsseries(Byte isseries) {
		this.isseries = isseries;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

}// End Of Class
