package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "CallbackDetails")
public class CallbackDetails {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "action", unique = false, nullable = false)
	private String action;

	@Column(name = "serviceid", unique = false, nullable = false)
	private String serviceid;

	@Column(name = "subServiceid", unique = false, nullable = true)
	private String subServiceid;

	@Column(name = "additionals", unique = false, nullable = true)
	private String additionals;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getServiceid() {
		return serviceid;
	}

	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getSubServiceid() {
		return subServiceid;
	}

	public void setSubServiceid(String subServiceid) {
		this.subServiceid = subServiceid;
	}

	public String getAdditionals() {
		return additionals;
	}

	public void setAdditionals(String additionals) {
		this.additionals = additionals;
	}

}
