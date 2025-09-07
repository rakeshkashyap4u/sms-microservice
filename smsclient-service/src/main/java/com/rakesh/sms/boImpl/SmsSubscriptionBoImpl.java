package com.rakesh.sms.boImpl;

import java.util.List;

import com.rakesh.sms.bo.SmsSubscriptionBo;
import com.rakesh.sms.dao.SmsSubscriptionDao;
import com.rakesh.sms.entity.SmsSubscription;

public class SmsSubscriptionBoImpl implements SmsSubscriptionBo{

	private SmsSubscriptionDao smsSubscriptionDao ;
	
	public SmsSubscriptionDao getSmsSubscriptionDao() {
		return smsSubscriptionDao;
	}

	public void setSmsSubscriptionDao(SmsSubscriptionDao smsSubscriptionDao) {
		this.smsSubscriptionDao = smsSubscriptionDao;
	}

	@Override
	public List<SmsSubscription> getSubUser(String subserviceid) {
		return this.smsSubscriptionDao.getSubUser(subserviceid);
	}

	
	
}
