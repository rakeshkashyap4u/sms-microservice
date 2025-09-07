package com.rakesh.sms.entity;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Entity
@Table(name = "AlertsContent")
public class AlertsContent {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "msgMonth", unique = false, nullable = false)
	private Integer msgMonth;

	@Column(name = "msgDay", unique = false, nullable = false)
	private Integer msgDay;

	@Column(name = "serviceId", unique = false, nullable = false)
	private String serviceId;

	@Column(name = "subServiceId", unique = false, nullable = false)
	private String subServiceId;

	@Column(name = "sendingTime", unique = false, nullable = false)
	private Time sendingTime;

	@Column(name = "msgText", unique = false, nullable = false)
	private String msgText;

	@Column(name = "msgFlag", unique = false, nullable = false)
	private String msgFlag;

	@Column(name = "language", unique = false)
	private String language;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMsgMonth() {
		return msgMonth;
	}

	public void setMsgMonth(Integer msgMonth) {
		this.msgMonth = msgMonth;
	}

	public Integer getMsgDay() {
		return msgDay;
	}

	public void setMsgDay(Integer msgDay) {
		this.msgDay = msgDay;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSubServiceId() {
		return subServiceId;
	}

	public void setSubServiceId(String subServiceId) {
		this.subServiceId = subServiceId;
	}

	public Time getSendingTime() {
		return sendingTime;
	}

	public void setSendingTime(Time sendingTime) {
		this.sendingTime = sendingTime;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public String getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String toString() {

		try {
			String json = CoreUtils.GSON.toJson(this);
			return json;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		return "{}";

	}// End Of Method

}
