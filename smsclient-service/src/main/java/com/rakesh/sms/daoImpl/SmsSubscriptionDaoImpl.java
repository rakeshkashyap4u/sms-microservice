package com.rakesh.sms.daoImpl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.SmsSubscriptionDao;
import com.rakesh.sms.entity.SmsSubscription;
import com.rakesh.sms.jpas.SmsSubscriptionRepository;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class SmsSubscriptionDaoImpl  implements SmsSubscriptionDao{
	
	
	private final SmsSubscriptionRepository smsSubscriptionRepo;

    public SmsSubscriptionDaoImpl(SmsSubscriptionRepository countryRepository) {
        this.smsSubscriptionRepo = countryRepository;
    }

	@Transactional
	public List<SmsSubscription> getSubUser(String subserviceid) {
	    try {
	        return smsSubscriptionRepo.findBySubserviceid(subserviceid);
	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	        return new ArrayList<>();
	    }
	}


}
