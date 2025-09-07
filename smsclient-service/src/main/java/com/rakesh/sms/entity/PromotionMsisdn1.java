package com.rakesh.sms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROMOTIONMSISDN")
public class PromotionMsisdn1 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "promotionName", unique = true, nullable = false)
	private String promotionName;

	@ManyToOne
	@JoinColumn(name="promoId", nullable=false)
	private SMSPromotion1 smspromo1;

	@Column(name = "msisdn", nullable = false)
	private String msisdn;
	
	@Column(name = "status", nullable = false)
	private String status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public PromotionMsisdn1( SMSPromotion1 smspromo1) {
		super();
		
		this.smspromo1 = smspromo1;
	}
	
	public PromotionMsisdn1( ) {
		
	}

	public SMSPromotion1 getSmspromo1() {
		return smspromo1;
	}

	public void setSmspromo1(SMSPromotion1 smspromo1) {
		this.smspromo1 = smspromo1;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PromotionMsisdn1( long id,String promotionName, String msisdn, String status) {
		super();
		this.id = id;
		this.promotionName = promotionName;
		this.msisdn = msisdn;
		this.status = status;
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
