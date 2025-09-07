package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import com.rakesh.sms.util.CoreUtils;

@Entity
@Table(name = "SMSBlacklist")
public class SMSBlacklist {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer slno;

	@Column(name = "msisdn")
	private String msisdn;

	@Column(name = "isSeries")
	private Byte isseries;

	public Integer getSlno() {
		return slno;
	}

	public void setSlno(Integer slno) {
		this.slno = slno;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn != null ? msisdn.trim() : null;
	}

	public boolean isSeries() {
		return isseries.byteValue() == 0 ? false : true;
	}

	public void setIsseries(Byte isseries) {
		this.isseries = isseries;
	}

	public void setIsseries(Integer isseries) {
		this.isseries = isseries.byteValue();
	}

	@Override
	public String toString() {
		return CoreUtils.GSON.toJson(this);
	}

}
