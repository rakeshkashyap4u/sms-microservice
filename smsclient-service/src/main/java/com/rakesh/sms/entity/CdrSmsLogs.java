package com.rakesh.sms.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;



@Entity
@Table(name = "SmsLogs", catalog = "cdrlog")
public class CdrSmsLogs {
     
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "master_id")
	private String master_id;
	
	@Column(name = "sender")
	private String sender;
	
	@Column(name = "receiver")
	private String receiver;

	@Column(name = "msg_id")
	private String msg_id;

	@Column(name = "mode")
	private String mode;

	@Column(name = "content")
	private String content;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "msg_type")
	private String msg_type;
	
	@Column(name = "circle")
	private String circle;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submit_time")
	private Date submit_time;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "response_time")
	private Date response_time;
	
	@Column(name = "service_type")
	private String service_type;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "receive_time")
	private Date receive_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMaster_id() {
		return master_id;
	}

	public void setMaster_id(String master_id) {
		this.master_id = master_id;
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

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public Date getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(Date submit_time) {
		this.submit_time = submit_time;
	}

	public Date getResponse_time() {
		return response_time;
	}

	public void setResponse_time(Date response_time) {
		this.response_time = response_time;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public Date getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(Date receive_time) {
		this.receive_time = receive_time;
	}

	
}
