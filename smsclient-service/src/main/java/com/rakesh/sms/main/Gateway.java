package com.rakesh.sms.main;

public interface Gateway {
	public boolean isConnected();

	public void close();
}
