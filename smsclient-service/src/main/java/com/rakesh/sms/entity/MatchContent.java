package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "MatchContent")
public class MatchContent {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@Column(name = "type")
	private String type;

	@Column(name = "content")
	private String content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MatchContent [id=" + id + ", type=" + type + ", content=" + content + "]";
	}
	
	

}
