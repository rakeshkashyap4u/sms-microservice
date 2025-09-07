package com.rakesh.sms.entity;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name = "SMSPROMOTION")
public class SMSPromotion1 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "promoId")
	private long id;

	@Column(name = "promotionName", unique = true, nullable = true)
	private String promotionName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startDateTime", nullable = true)
	private Date startDateTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiryDateTime", nullable = true)
	private Date expiryDateTime;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "MessageFrom", nullable = true)
	private Date messageFrom;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "MessageTo", nullable = true)
	private Date messageTo;
	
	@Column(name = "MostActive", nullable = true)
	private int mostActive;
	
	@Column(name = "userStatus")
	private String userStatus;

	@Column(name = "msgText", unique = false, nullable = true)
	private String msgText;

	@Column(name = "language", unique = false, nullable = true)
	private String language;
	
	@Column(name = "status", unique = false)
	private String status;
	
	@Column(name = "service", unique = false)
	private String service;
	
	@Column(name = "callerId", unique = false)
	private String callerId;
	
	@Column(name = "circle", unique = false)
	private String circle;

	@OneToMany(mappedBy="smspromo1")
	private Set<PromotionMsisdn1> promomsisdn;

	public Set<PromotionMsisdn1> getPromomsisdn() {
		return promomsisdn;
	}

	public void setPromomsisdn(Set<PromotionMsisdn1> promomsisdn) {
		this.promomsisdn = promomsisdn;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime1) {
		this.expiryDateTime = expiryDateTime1;
	}
	
	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}
	
	

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public Date getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(Date messageFrom) {
		messageFrom = messageFrom;
	}

	public Date getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(Date messageTo) {
		messageTo = messageTo;
	}

	public int getMostActive() {
		return mostActive;
	}

	public void setMostActive(int mostActive) {
		mostActive = mostActive;
	}
	
	

	/*@Override
	public String toString() {
		
		try {
			String json = CoreUtils.GSON.toJson(this);
			return json;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}
		return "{}";
		
	}*/
}
