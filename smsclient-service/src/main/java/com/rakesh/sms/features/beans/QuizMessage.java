package com.rakesh.sms.features.beans;

import java.util.Date;

import com.rakesh.sms.util.CoreUtils;

public class QuizMessage {

	private String msisdn, quizname, shortcode, message, timestamp;

	public QuizMessage() {
		this.msisdn = this.quizname = this.shortcode = this.message = null;
		this.timestamp = CoreUtils.getTimeStamp(new Date());
	}// End Of Constructor

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getQuizname() {
		return quizname;
	}

	public void setQuizname(String quizname) {
		this.quizname = quizname;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = CoreUtils.getTimeStamp(timestamp);
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return CoreUtils.GSON.toJson(this);
	}

	public static QuizMessage parse(String json) {
		if (json != null && json.length() > 0)
			return CoreUtils.GSON.fromJson(json, QuizMessage.class);
		else
			return null;
	}// End Of Method

}
