package com.rakesh.sms.util;

import com.rakesh.sms.main.HttpGateway;

public class HttpRequestManager {

	private int connectionTimeout, readTimeout;
	private HttpGateway gateway;

	public HttpRequestManager(HttpGateway gateway, int connTimeout, int readtimeout) {
		this.connectionTimeout = connTimeout;
		this.readTimeout = readtimeout;
		this.gateway = gateway;
	}// End Of Constructor

	int getConnectionTimeout() {
		return this.connectionTimeout;
	}

	int getReadTimeout() {
		return this.readTimeout;
	}

	String getUserAgent() {
		return this.gateway.getUserAgent();
	}

	void finish(int queue) {
		this.gateway.decreaseThreadsCount();
		this.gateway.notifyTPS();
	}// End Of Method

}// End Of Class
