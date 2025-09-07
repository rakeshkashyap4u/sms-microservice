package com.rakesh.sms.entity;

import java.util.Date;

import com.rakesh.sms.util.CoreUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "SmsSubscription")
public class SmsSubscription {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name = "msisdn", unique = false, nullable = false)
	private String msisdn;

	@Column(name = "serviceid", unique = false, nullable = false)
	private String serviceid;

	@Column(name = "subserviceid", unique = false, nullable = false)
	private String subserviceid;

	@Column(name = "status", unique = false, nullable = false)
	private String status;

	@Column(name = "startdate", unique = false, nullable = false)
	private Date startdate;

	@Column(name = "language", unique = false, nullable = false)
	private String language;

	@Column(name = "shortcode", unique = false, nullable = false)
	private String shortcode;

	@Column(name = "operator", unique = false, nullable = false)
	private String operator;

	@Column(name = "country", unique = false, nullable = false)
	private String country;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastprocessed", unique = false, nullable = false)
	private Date lastprocessed;

	@Column(name = "msgflag", unique = false, nullable = true)
	private String msgflag;

	@Column(name = "param1", unique = false)
	private String param1; // adding this for operator service id in iran cell

	@Column(name = "param2", unique = false)
	private String param2;// adding this for timestamp field in iran cell
	
	@Temporal(TemporalType.DATE)
	@Column(name = "enddate", unique = false)
	private Date enddate;// adding this for timestamp field in asia cell
	
	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getSubserviceid() {
		return subserviceid;
	}

	public void setSubserviceid(String subserviceid) {
		this.subserviceid = subserviceid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getLastprocessed() {
		return lastprocessed;
	}

	public void setLastprocessed(Date lastprocessed) {
		this.lastprocessed = lastprocessed;
	}

	public String getMsgflag() {
		return msgflag;
	}

	public void setMsgflag(String msgflag) {
		this.msgflag = msgflag;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String toString() {
		return CoreUtils.GSON.toJson(this).trim();
	}// End Of Method

}
