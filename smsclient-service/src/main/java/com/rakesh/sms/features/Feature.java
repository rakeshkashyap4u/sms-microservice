package com.rakesh.sms.features;

import javax.jms.MessageListener;

import com.rakesh.sms.beans.Message;

public interface Feature extends MessageListener {

	public void onMessage(javax.jms.Message msg);

	public void sendReply(Message mo);

}
