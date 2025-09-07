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
@Table(name = "LanguageSpecification")
public class LanguageSpecification {

	@Id
	@GeneratedValue
	@Column(name = "lid", unique = true, nullable = false)
	private Integer lid;

	@Column(name = "language", unique = true, nullable = false)
	private String language;

	@Column(name = "dataCoding", unique = false, nullable = false)
	private Integer dataCoding;

	@Column(name = "serviceType", unique = false, nullable = true)
	private String serviceType;

	@Column(name = "encoding", unique = false, nullable = false)
	private String encoding;

	@Column(name = "script", unique = false, nullable = false)
	private Integer script;

	public Integer getLid() {
		return lid;
	}

	public void setLid(Integer lid) {
		this.lid = lid;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getDataCoding() {
		return dataCoding;
	}

	public byte getDataCodingValue() {
		return dataCoding.byteValue();
	}

	public void setDataCoding(Integer dataCoding) {
		this.dataCoding = dataCoding;
	}

	public void setDataCoding(Byte dataCoding) {
		this.dataCoding = new Integer(dataCoding);
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Integer getScript() {
		return script;
	}

	public void setScript(Integer script) {
		this.script = script;
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
