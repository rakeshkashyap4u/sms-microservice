package com.rakesh.sms.cdr;

import com.rakesh.sms.util.CoreUtils;

public class CdrEvent {

	private String xml;
	private String filename;

	public CdrEvent() {
		this.xml = "";
		this.filename = "";
	}

	public CdrEvent(String xml, String filename) {
		this.xml = xml;
		this.filename = filename;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return CoreUtils.GSON.toJson(this);
	}// End Of Method

}
