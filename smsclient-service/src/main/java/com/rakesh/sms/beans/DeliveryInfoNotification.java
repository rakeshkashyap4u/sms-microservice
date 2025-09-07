package com.rakesh.sms.beans;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;



public class DeliveryInfoNotification {

	private String callbackData;
	private List<DeliveryInfo> deliveryInfo;

	@XmlElement(name = "callbackData")
	public String getCallbackData() {
		return callbackData;
	}

	public void setCallbackData(String callbackData) {
		this.callbackData = callbackData;
	}

	@XmlElement(name = "deliveryInfo")
	public List<DeliveryInfo> getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(List<DeliveryInfo> deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

}
