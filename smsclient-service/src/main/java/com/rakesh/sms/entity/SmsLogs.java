package com.rakesh.sms.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "SmsLogs")
public class SmsLogs {

	@Override
	public String toString() {
		return "SmsLogs [messageId=" + messageId + ", sender=" + sender + ", receiver=" + receiver + ", circle="
				+ circle + ", transId=" + transId + ", price=" + price + ", callback=" + callback + ", autotimestamp="
				+ autotimestamp + "]";
	}

	@Id
	@Column(name = "messageId", unique = true, nullable = false)
	private String messageId;

	@Column(name = "sender", unique = false, nullable = false)
	private String sender;

	@Column(name = "receiver", unique = false, nullable = false)
	private String receiver;

	@Column(name = "circle", unique = false, nullable = false)
	private String circle;

	@Column(name = "transId", unique = true, nullable = false)
	private String transId;

	@Column(name = "price", unique = false, nullable = false)
	private Double price;

	@Column(name = "callback", unique = false, nullable = false)
	private int callback;

	@Column(name = "autotimestamp", unique = false, nullable = false)
	private Date autotimestamp;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public int getCallback() {
		return callback;
	}

	public void setCallback(int callback) {
		this.callback = callback;
	}

	public Date getAutotimestamp() {
		return autotimestamp;
	}

	public void setAutotimestamp(Date autotimestamp) {
		this.autotimestamp = autotimestamp;
	}

}
