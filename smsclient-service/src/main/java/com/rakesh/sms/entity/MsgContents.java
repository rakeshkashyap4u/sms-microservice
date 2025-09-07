package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "MsgContents") // itika: earlier it was msgcontents
public class MsgContents {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@Column(name = "service")
	private String service;
	
	@Column(name = "event")
	private String event;

	@Column(name = "content")
	private String content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "msgcontents [id=" + id + ", service=" + service + ", event=" + event + ", content=" + content + "]";
	}



}
