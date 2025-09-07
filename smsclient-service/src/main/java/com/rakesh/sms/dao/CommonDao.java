package com.rakesh.sms.dao;

import java.util.Date;
import java.util.List;

import com.rakesh.sms.entity.SmsLogs;
import com.rakesh.sms.entity.Subscription;

public interface CommonDao {

	public List<Subscription> getServiceActiveUser(String serviceName, String userStatus, Date msgFromDate, Date msgToDate);
	
	public boolean updateSubscribedUserStatus1(String promoName, List<String> msisdn, String status, int count);

	public List<String> getMostActiveUser(int limit);
}
