package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import com.rakesh.sms.main.SmsValidation;

@Entity
@Table(name = "MessageFormats")
public class MessageFormats {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;

	@Column(name = "serviceCode")
	private String serviceCode;

	@Column(name = "keyword")
	private String keyword;
	
	@Column(name = "countryCode")
	private String countryCode;

	@Column(name = "subkey")
	private String subkey;

	@Column(name = "argument1")
	private String argument1;

	@Column(name = "argument2")
	private String argument2;

	@Column(name = "argument3")
	private String argument3;

	@Column(name = "serviceid")
	private String serviceid;

	public MessageFormats() {
		this.id = 0;
	}// End Of Constructor

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSubkey() {
		return subkey;
	}

	public void setSubkey(String subkey) {
		this.subkey = subkey;
	}

	public String getArgument1() {
		return argument1;
	}

	public void setArgument1(String argument1) {
		if (argument1 == null || argument1.equals("") || argument1.toLowerCase().contains("null"))
			this.argument1 = SmsValidation.NULL;
		else
			this.argument1 = argument1;
	}

	public String getArgument2() {
		return argument2;
	}

	public void setArgument2(String argument2) {
		if (argument2 == null || argument2.equals("") || argument2.toLowerCase().contains("null"))
			this.argument2 = SmsValidation.NULL;
		else
			this.argument2 = argument2;
	}

	public String getArgument3() {
		return argument3;
	}

	public void setArgument3(String argument3) {
		if (argument3 == null || argument3.equals("") || argument3.toLowerCase().contains("null"))
			this.argument3 = SmsValidation.NULL;
		else
			this.argument3 = argument3;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String toString() {
		return " ID= " + this.id + "  |  serviceCode= " + this.serviceCode + "  |  Keyword= " + this.keyword
				+ "  |  subKey= " + this.subkey + "  |  argument1= " + this.argument1 + "  |  argument2= "
				+ this.argument2 + "  |  argument3= " + this.argument3;
	}// End Of Method

}
