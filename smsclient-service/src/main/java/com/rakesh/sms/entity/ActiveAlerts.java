package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ActiveAlerts")
public class ActiveAlerts {

	@Id
	@GeneratedValue
	@Column(name = "slNo", unique = true, nullable = false)
	private Integer slNo;

	@Column(name = "serviceid", unique = true, nullable = false)
	private String serviceid;

	@Column(name = "subserviceid", unique = false, nullable = true)
	private String subserviceid;

	@Column(name = "type", unique = false, nullable = false)
	private Integer type;

	@Column(name = "protocol", unique = false, nullable = false)
	private Integer protocol;

	@Column(name = "priority", unique = false, nullable = false)
	private Integer priority;

	@Column(name = "circle", unique = false, nullable = false)
	private String circle;

	@Column(name = "cli", unique = false, nullable = true)
	private String cli;

	@Column(name = "language", unique = false, nullable = false)
	private String language;

	@Column(name = "expiryTime", unique = false, nullable = false)
	private Integer expiryTime;

	public Integer getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Integer expiryTime) {
		this.expiryTime = expiryTime;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getProtocol() {
		return protocol;
	}

	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getCli() {
		return cli;
	}

	public void setCli(String cli) {
		this.cli = cli;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
