package com.rakesh.sms.dao;

import java.util.Date;
import java.util.List;

import com.rakesh.sms.entity.PromotionMsisdn1;
import com.rakesh.sms.entity.SMSPromotion1;

public interface SmsPromoDao {

public List<SMSPromotion1> getSmsPromotion(String date);
	
	public List<PromotionMsisdn1> getPromoMsisdn(String promoName);
	
	public List<String> getActiveUser(String serviceName, String userStatus, Date msgFromDate, Date msgToDate);
	
	public boolean updateStatus(String promoName,String status);
	
	public boolean updatePromoMsisdnStatus(String promoName, List<String> msisdn, String status, int count);
	
	public SMSPromotion1 getPromotion(String promoName);
	
	public boolean updateSubscribedUserStatus(String promoName, List<String> msisdn, String status, int count);
	
	public boolean insertServiceMsisdns(SMSPromotion1 sp,String promoName,long promoId, List<String> msisdns);
	
	public List<String> getMostActiveUser(int limit);
}
