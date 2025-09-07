package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Entity
@Table(name = "mtresponse")
public class MtResponse {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "msisdn")
	private String msisdn;

	@Column(name = "cli")
	private String cli;
	
	@Column(name = "serviceid")
	private String serviceid;
	
	@Column(name = "subserviceid")
	private String subserviceid;
	
	@Column(name = "tid")
	private String tid;
	
	@Column(name = "status_id")
	private String status_id;
	
	@Column(name = "message")
	private String message;
	
	@Column(name = "sdc")
	private String sdc;

	public MtResponse() {
		this.id =0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getCli() {
		return cli;
	}

	public void setCli(String cli) {
		this.cli = cli;
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

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getStatus_id() {
		return status_id;
	}

	public void setStatus_id(String status_id) {
		this.status_id = status_id;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSdc() {
		return sdc;
	}

	public void setSdc(String sdc) {
		this.sdc = sdc;
	}

	public String toString() {

		try {
			String json = CoreUtils.GSON.toJson(this);
			return json;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		return "{}";

	}// End Of Method
	
}
