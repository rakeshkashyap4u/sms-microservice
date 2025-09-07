package com.rakesh.sms.dao;

import java.util.List;

import com.rakesh.sms.entity.SmsSubscription;

public interface SmsSubscriptionDao {

	public List<SmsSubscription> getSubUser(String subserviceid);

}
