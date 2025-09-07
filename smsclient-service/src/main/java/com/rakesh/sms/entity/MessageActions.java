package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Entity
@Table(name = "MessageActions")  //itika: for table structure
public class MessageActions {

	@Id
	@GeneratedValue
	@Column(name = "aid")
	private int aid;

	@Column(name = "moId")
	private int moId;

	@Column(name = "type")
	private String type;

	@Column(name = "details")
	private String details;

	public MessageActions() {
		this.aid = 0;
	}// End Of Constructor

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public int getMoId() {
		return moId;
	}

	public void setMoId(int moId) {
		this.moId = moId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
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
