package com.rakesh.sms.boImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rakesh.sms.bo.SmsPromoBo;
import com.rakesh.sms.dao.SmsPromoDao;
import com.rakesh.sms.entity.PromotionMsisdn1;
import com.rakesh.sms.entity.SMSPromotion1;

public class SmsPromoBoImpl implements SmsPromoBo{

	@Autowired
	private SmsPromoDao smsdaoImpl;
	
	

	public SmsPromoDao getSmsdaoImpl() {
		return smsdaoImpl;
	}

	public void setSmsdaoImpl(SmsPromoDao smsdaoImpl) {
		smsdaoImpl = smsdaoImpl;
	}

	@Override
	public List<SMSPromotion1> getSmsPromotion(String date){
		return this.smsdaoImpl.getSmsPromotion(date);
	}
	
	@Override
	public List<PromotionMsisdn1> getPromoMsisdn(String promoName){
		return this.smsdaoImpl.getPromoMsisdn(promoName);
	}
	
	public List<String> getActiveUser(String serviceName, String userStatus, Date msgFromDate, Date msgToDate){
		return this.smsdaoImpl.getActiveUser(serviceName,userStatus , msgFromDate, msgToDate);
	}
	
	public boolean updateStatus(String promoName,String status) {
		return this.smsdaoImpl.updateStatus(promoName,status);
	}
	
	public boolean updatePromoMsisdnStatus(String promoName, List<String> msisdn, String status, int count) {
		return this.smsdaoImpl.updatePromoMsisdnStatus( promoName, msisdn,  status,count);
	}
	
	public SMSPromotion1 getPromotion(String promoName) {
		return this.smsdaoImpl.getPromotion(promoName);
	}
	
	public boolean updateSubscribedUserStatus(String promoName, List<String> msisdn, String status, int count) {
		return this.smsdaoImpl.updateSubscribedUserStatus(promoName,msisdn,status,count);
	}
	
	public boolean insertServiceMsisdns(SMSPromotion1 sp,String promoName,long promoId, List<String> msisdns) {
		return this.smsdaoImpl.insertServiceMsisdns(sp,promoName,promoId, msisdns);
	}
	
	public List<String> getMostActiveUser(int limit){
		return this.smsdaoImpl.getMostActiveUser(limit);
	}
}
