package com.rakesh.sms.entity;

import java.util.HashSet;
import java.util.Set;

import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SMSCConfigs")
public class SMSCConfigs {

	@Id
	@GeneratedValue
	@Column(name = "cid")
	private Integer cid;

	@Column(name = "opId")
	private String opId;

	@Column(name = "circle")
	private String circle;

	@Column(name = "serverIp")
	private String serverIp;

	@Column(name = "serverPort")
	private int serverPort;

	@Column(name = "serviceUri")
	private String serviceUri;

	@Column(name = "responseType")
	private String responseType;

	@Column(name = "contentType")
	private String contentType;

	@Column(name = "userid")
	private String userid;

	@Column(name = "password")
	private String password;

	@Column(name = "timeout")
	private int timeout;

	@Column(name = "maxConnections")
	private int maxConnections;

	@Column(name = "systemType")
	private String systemType;

	@Column(name = "bindMode")
	private int bindMode;

	public SMSCConfigs() {
		this.serviceUri = "";
	}

	public SMSCConfigs(String circle, String serverIp, int serverPort, String serviceUri, String userid,
			String password, int bindMode) {
		this.circle = circle;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serviceUri = serviceUri;
		this.userid = userid;
		this.password = password;
		this.bindMode = bindMode;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "circle")
	transient private Set<MsisdnSeries> msisdnSeries = new HashSet<MsisdnSeries>();

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getOpId() {
		return opId;
	}

	public void setOpId(String opId) {
		this.opId = opId;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServiceUri() {
		return serviceUri;
	}

	public void setServiceUri(String serviceUri) {
		this.serviceUri = serviceUri;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public int getBindMode() {
		return bindMode;
	}

	public void setBindMode(int bindMode) {
		this.bindMode = bindMode;
	}

	public Set<MsisdnSeries> getMsisdnSeries() {
		return msisdnSeries;
	}

	public void setMsisdnSeries(Set<MsisdnSeries> msisdnSeries) {
		this.msisdnSeries = msisdnSeries;
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
