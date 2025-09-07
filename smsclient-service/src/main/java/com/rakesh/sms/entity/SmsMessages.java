package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "smsmessages")
public class SmsMessages {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private int id;
	
	@Column(name = "key")
	private String key;
	
	@Column(name = "content")
	private String content ;
	
	@Column(name = "language")
	private String language ;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "SmsMessages [id=" + id + ", key=" + key + ", content=" + content + ", language=" + language + "]";
	}

	
}

