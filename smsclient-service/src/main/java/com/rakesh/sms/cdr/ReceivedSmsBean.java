package com.rakesh.sms.cdr;

import java.io.Serializable;

import org.jsmpp.bean.OptionalParameter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.util.CoreUtils;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/***
 * ---> XmlElement annotation is used to specify the field name in XML CDRs --->
 * XmlTransient annotation is used to exclude the field from XML CDRs
 * 
 * ---> SerializedName annotation is used to specify the field name in JSON CDRs
 * ---> Expose annotation is used to include the field in JSON CDRs ~~~ JSON
 * CDRs are being used for atHome only
 */

@XmlRootElement(name = "smscdr")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReceivedSmsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Expose
	@XmlElement(name = "sender")
	private String sender;

	@Expose
	@SerializedName("receiver")
	@XmlElement(name = "receiver")
	private String receiverMsisdn;

	@Expose
	@XmlElement(name = "messageId")
	private String messageId;

	@Expose
	@XmlElement(name = "content")
	private String content;

	@Expose
	@SerializedName("receivetime")
	@XmlElement(name = "receivetime")
	private String time;

	@Expose
	@XmlElement(name = "status")
	private String status;

	@Expose
	@XmlElement(name = "serviceType")
	private String serviceType;

	@Expose
	@XmlElement(name = "msgType")
	private String msgType;

	@XmlTransient
	private boolean deliveryReport;

	/** Do Not Add Expose --- it will be added to JSON CDRs */
	@XmlTransient
	private String circle;

	@Expose
	@XmlTransient
	private String reason;

	@XmlTransient
	private OptionalParameter[] parameters;
	
	@Expose
	@XmlElement(name = "mode")
	private String mode;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

//	for mexico
	private String msgid;
	
	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiverMsisdn() {
		return receiverMsisdn;
	}

	public void setReceiverMsisdn(String receiverMsisdn) {
		this.receiverMsisdn = receiverMsisdn;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public OptionalParameter[] getParameters() {
		return parameters;
	}

	public void setParameters(OptionalParameter[] parameters) {
		this.parameters = parameters;
	}

	public boolean isDeliveryReport() {
		return deliveryReport;
	}

	public void setDeliveryReport(boolean deliveryReport) {

		this.deliveryReport = deliveryReport;

		if (this.deliveryReport) {
			this.setMsgType("DR");
		} else {
			this.setMsgType("MO");
		}

	}// End Of Method

	public void setFailedStatus() {

		if (this.messageId == null || this.messageId.length() == 0) {
			this.messageId = SMSController.DefaultMessageID;
		}

		if (this.status == null || this.status.length() == 0) {
			this.status = "UNKNOWN";
		}

		this.time = CoreUtils.getCurrentTimeStamp();

	}// End Of Method

//	@Override
//	public String toString() {
//		return CdrCreator.jsonReader.toJson(this);
//	}// End Of Method

}
