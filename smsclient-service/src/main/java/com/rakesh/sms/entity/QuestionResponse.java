package com.rakesh.sms.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name = "question_response")
public class QuestionResponse {
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "user_id")
	private String user_id;
	
	@Column(name = "level_id")
	private String level_id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "asked_time")
	private Date asked_time;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "answered_time")
	private Date answered_time;
	
	@Column(name = "response")
	private String response;
	
	@Column(name = "correct_reponse")
	private int correct_reponse;
	
	@Column(name = "format")
	private String format;
	
	@Column(name = "question_id")
	private String question_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getLevel_id() {
		return level_id;
	}

	public void setLevel_id(String level_id) {
		this.level_id = level_id;
	}

	public Date getAsked_time() {
		return asked_time;
	}

	public void setAsked_time(Date asked_time) {
		this.asked_time = asked_time;
	}

	public Date getAnswered_time() {
		return answered_time;
	}

	public void setAnswered_time(Date answered_time) {
		this.answered_time = answered_time;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getCorrect_reponse() {
		return correct_reponse;
	}

	public void setCorrect_reponse(int correct_reponse) {
		this.correct_reponse = correct_reponse;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}

	
	public QuestionResponse() {
		super();
	}

	public QuestionResponse(Integer id, String user_id, String level_id, Date asked_time, Date answered_time,
			String response, int correct_reponse, String format, String question_id) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.level_id = level_id;
		this.asked_time = asked_time;
		this.answered_time = answered_time;
		this.response = response;
		this.correct_reponse = correct_reponse;
		this.format = format;
		this.question_id = question_id;
	}

	@Override
	public String toString() {
		return "QuestionResponse [id=" + id + ", user_id=" + user_id + ", level_id=" + level_id + ", asked_time="
				+ asked_time + ", answered_time=" + answered_time + ", response=" + response + ", correct_reponse="
				+ correct_reponse + ", format=" + format + ", question_id=" + question_id + "]";
	}


	

}
