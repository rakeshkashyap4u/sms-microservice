package com.rakesh.sms.cdr;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
public class SmsCdrBean implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Expose
	@SerializedName("sender")
	@XmlElement(name = "sender")
	private String senderCli;

	@Expose
	@SerializedName("receiver")
	@XmlElement(name = "receiver")
	private String receiverMsisdn;

	@Expose
	@XmlElement(name = "messageId")
	private String messageId;

	@Expose
	@XmlElement(name = "mode")
	private String mode;

	@Expose
	@XmlElement(name = "content")
	private String content;

	@Expose
	@XmlElement(name = "status")
	private String status;

	@Expose
	@XmlElement(name = "msgType")
	private String msgType;

	@Expose
	@XmlElement(name = "circle")
	private String circle;

	@Expose
	@XmlElement(name = "submittime")
	private String submittime;

	@Expose
	@XmlElement(name = "responsetime")
	private String responsetime;

	@Expose
	@XmlElement(name = "serviceType")
	private String serviceType;

	/** Used to specify SMS priority */
	@XmlTransient
	private String type;

	/** Used only for AtHome, to provide Failure Reason */
	@Expose
	@XmlTransient
	private String reason;

	/**
	 * For future use, to add additional Info to SMS CDR. Like Promotions, Namaz,
	 * etc SMS. Currently not in use ---> Remove XmlTransient annotation to make use
	 * in CDRs
	 **/
	@XmlTransient
	private String info;

	public String getSenderCli() {
		return senderCli;
	}

	public void setSenderCli(String senderCli) {
		this.senderCli = senderCli;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getSubmittime() {
		return submittime;
	}

	public void setSubmittime(String submittime) {
		this.submittime = submittime;
	}

	public String getResponsetime() {
		return responsetime;
	}

	public void setResponsetime(String responsetime) {
		this.responsetime = responsetime;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

//	@Override
//	public String toString() {
//		return CdrCreator.jsonReader.toJson(this);
//	}// End Of Method

}
