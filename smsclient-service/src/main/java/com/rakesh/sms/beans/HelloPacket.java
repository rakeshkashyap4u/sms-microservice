package com.rakesh.sms.beans;

import java.io.Serializable;

public class HelloPacket implements Serializable {

	private static final long serialVersionUID = 2303946645381287792L;

	private String id;
	private String event;
	private String subEvent;
	private String module;
	private String hbTime;
	private String type;
	private String action;
	private String queueName;
	private String className;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getSubEvent() {
		return subEvent;
	}

	public void setSubEvent(String subEvent) {
		this.subEvent = subEvent;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getHbTime() {
		return hbTime;
	}

	public void setHbTime(String hbTime) {
		this.hbTime = hbTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isValid() {
		if (this.id != null && this.id.equals("10") && this.event.equals("9") && this.subEvent.equals("9")) {
			return true;
		} else {
			return false;
		}
	}// End Of Method

	public boolean isNotification() {
		if (this.action != null && this.action.length() > 0 && this.module.length() > 0) {
			return true;
		} else {
			return false;
		}
	}// End Of Method

}
