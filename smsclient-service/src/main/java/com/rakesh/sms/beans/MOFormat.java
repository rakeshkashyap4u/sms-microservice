package com.rakesh.sms.beans;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "inboundSMSMessageNotification")
public class MOFormat {

	private String callbackData;
	private List<InboundMessage> inboundMessage;

	@XmlElement(name = "callbackData")
	public String getCallbackData() {
		return callbackData;
	}

	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}

	@XmlElement(name = "inboundSMSMessage", type = InboundMessage.class)
	public List<InboundMessage> getInboundMessage() {
		return inboundMessage;
	}

	public void setInboundMessage(List<InboundMessage> inboundMessage) {
		this.inboundMessage = inboundMessage;
	}

	@Override
	public String toString() {
		return this.callbackData;
	}

}
