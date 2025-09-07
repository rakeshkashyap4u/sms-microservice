package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "GamingUser")
public class GamingUser {
	
	
	@Id
	@GeneratedValue()
	@Column(name = "id", unique = true, nullable = false)
    private String id;
	
	@Column(name = "userid", unique = true, nullable = false)
	private String userid;

	@Column(name = "questionid", unique = false, nullable = false)
	private String questionid;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	@Override
	public String toString() {
		return "GamingUser [id=" + id + ", userid=" + userid + ", questionid=" + questionid + "]";
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getQuestionid() {
		return questionid;
	}

	public void setQuestionid(String questionid) {
		this.questionid = questionid;
	}

	

}
