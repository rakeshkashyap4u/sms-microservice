package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "SMSCFormats")
public class SMSCFormats {

	@Id
	@GeneratedValue
	@Column(name = "rid")
	private String rid;

	@Column(name = "cid")
	private String cid;

	@Column(name = "requestFormat")
	private String requestFormat;

	@Column(name = "responseFormat")
	private String responseFormat;

	@Column(name = "mode")
	private String mode;

	@Column(name = "register")
	private String register;

	@Column(name = "MORegisterFormat")
	private String moRegisterFormat;

	@Column(name = "MTRegisterFormat")
	private String mtRegisterFormat;
	
	@Column(name = "options")
	private String options;
	
	@Column(name = "request_format")
	private String requestformat;

	public String getRequestformat() {
		return requestformat;
	}

	public void setRequestformat(String requestformat) {
		this.requestformat = requestformat;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public SMSCFormats() {
		this.requestFormat = this.responseFormat = "";
		this.mode = "GET";
	}// End Of Constructor

	public SMSCFormats(String cid, String requestFormats, String responseFormats, String mode) {
		this.requestFormat = requestFormats;
		this.responseFormat = responseFormats;
		this.mode = mode;
	}// End Of Constructor

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getRequestFormat() {
		return requestFormat;
	}

	public void setRequestFormat(String requestFormat) {
		this.requestFormat = requestFormat;
	}

	public String getResponseFormat() {
		return responseFormat;
	}

	public void setResponseFormat(String responseFormat) {
		this.responseFormat = responseFormat;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}

	public String getMoRegisterFormat() {
		return moRegisterFormat;
	}

	public void setMoRegisterFormat(String moRegisterFormat) {
		this.moRegisterFormat = moRegisterFormat;
	}

	public String getMtRegisterFormat() {
		return mtRegisterFormat;
	}

	public void setMtRegisterFormat(String mtRegisterFormat) {
		this.mtRegisterFormat = mtRegisterFormat;
	}

}
