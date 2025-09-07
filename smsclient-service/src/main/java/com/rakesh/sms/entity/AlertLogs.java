package com.rakesh.sms.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "AlertLogs")
public class AlertLogs {

	@Id
	@GeneratedValue
	@Column(name = "pid", unique = true, nullable = false)
	private Integer pid;

	@Column(name = "name", unique = false, nullable = false)
	private String name;

	@Column(name = "cli", unique = false, nullable = false)
	private String cli;

	@Column(name = "circle", unique = false, nullable = false)
	private String circle;

	@Column(name = "baseSize", unique = false, nullable = false)
	private Integer baseSize;

	@Column(name = "status", unique = false, nullable = false)
	private String status;

	@Column(name = "timestamp", unique = false, nullable = false)
	private Date timestamp;

	@Column(name = "expiresAt", unique = false, nullable = false)
	private Date expiresAt;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCli() {
		return cli;
	}

	public void setCli(String cli) {
		this.cli = cli;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public Integer getBaseSize() {
		return baseSize;
	}

	public void setBaseSize(Integer baseSize) {
		this.baseSize = baseSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

}
