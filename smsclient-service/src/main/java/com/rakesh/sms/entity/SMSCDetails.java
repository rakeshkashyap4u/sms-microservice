package com.rakesh.sms.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "SMSCDetails")
public class SMSCDetails {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private String id;

	@Column(name = "operator")
	private String operator;

	@Column(name = "country")
	private String country;

	@Column(name = "protocol")
	private String protocol;

	public SMSCDetails() {

	}

	public SMSCDetails(String operator, String country, String protocol) {
		this.operator = operator;
		this.protocol = protocol;
		this.country = country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
