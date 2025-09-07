package com.rakesh.sms.beans;

import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class Response {

	/** SMS ID */
	private String id;
	/** --- */
	private int sub;
	/** Delivered status */
	private int dlvrd;
	/** Submit Date */
	private long submitDate;
	/** Done Date */
	private long doneDate;
	/** --- */
	private String stat;
	/** Error Code */
	private int err;
	/** Text */
	private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSub() {
		return sub;
	}

	public void setSub(int sub) {
		this.sub = sub;
	}

	public int getDlvrd() {
		return dlvrd;
	}

	public void setDlvrd(int dlvrd) {
		this.dlvrd = dlvrd;
	}

	public long getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(long submitDate) {
		this.submitDate = submitDate;
	}

	public long getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(long doneDate) {
		this.doneDate = doneDate;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

}// End Of Bean
