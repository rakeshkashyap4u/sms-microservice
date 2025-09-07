package com.rakesh.sms.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "DoubleConsent", uniqueConstraints = @UniqueConstraint(columnNames = { "msisdn",
		"moId" }))
public class DoubleConsent {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "msisdn", unique = false, nullable = false)
	private String msisdn;

	@Column(name = "shortcode", unique = false, nullable = false)
	private String shortcode;

	@Column(name = "moId", unique = false, nullable = false)
	private Integer moId;

	@Column(name = "message", unique = false, nullable = false)
	private String message;

	@Column(name = "expiresAt", unique = false, nullable = false)
	private Date expiresAt;

	@Column(name = "timestamp", unique = false, nullable = false)
	private Date timestamp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public Integer getMoId() {
		return moId;
	}

	public void setMoId(Integer moId) {
		this.moId = moId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String toString() {
		return " Msisdn= " + this.msisdn + "  |  serviceCode= " + this.shortcode + "  |  Message= " + this.message
				+ "  |  expiry= " + this.expiresAt;
	}// End Of Method
}
