package com.rakesh.sms.beans;

import java.io.Serializable;

import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class HelloAck implements Serializable {

	private static final long serialVersionUID = -1186169521435519316L;

	public static final String SUCCESS = new String("hbResponse");
	public static final String STOPPING = new String("shutDown");
	public static final String STARTED = new String("startup");
	public static final String ERROR = new String("hbError");

	private String id;
	private String module;
	private String hbReplyTime;
	private String type;

	public HelloAck() {
	}

	public HelloAck(String type) {
		super();
		this.id = new String("10");
		this.module = new String("SMSClient");
		this.hbReplyTime = CoreUtils.getCurrentTimeStamp();
		this.type = type;
	}// End Of Constructor

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getHbReplyTime() {
		return hbReplyTime;
	}

	public void setHbReplyTime(String hbReplyTime) {
		this.hbReplyTime = hbReplyTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
