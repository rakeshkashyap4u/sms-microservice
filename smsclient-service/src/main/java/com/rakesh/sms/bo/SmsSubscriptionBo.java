package com.rakesh.sms.bo;


import java.util.List;

import com.rakesh.sms.entity.SmsSubscription;

public interface SmsSubscriptionBo {

	public List<SmsSubscription> getSubUser(String subserviceid);

}
