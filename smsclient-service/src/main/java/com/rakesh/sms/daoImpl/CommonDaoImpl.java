package com.rakesh.sms.daoImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.CommonDao;
import com.rakesh.sms.entity.Subscription;
import com.rakesh.sms.jpas.CdrSmsLogsRepository;
import com.rakesh.sms.jpas.SmsSubscriptionRepository;
import com.rakesh.sms.jpas.SubscriptionRepository;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class CommonDaoImpl implements CommonDao{
	
	private final SmsSubscriptionRepository subscriptionRepository;
	private final CdrSmsLogsRepository cdrSmsLogsRepository;
	private final SubscriptionRepository repo;
	
	

	 public CommonDaoImpl(SmsSubscriptionRepository repo1,CdrSmsLogsRepository cdrSmsLogsRepository,SubscriptionRepository repo) {
	        this.subscriptionRepository = repo1;
	        this.cdrSmsLogsRepository=cdrSmsLogsRepository;
	        this.repo=repo;
	       
	    }


	
	  @Transactional
	    public List<Subscription> getServiceActiveUser(String serviceName, String userStatus, Date msgFromDate, Date msgToDate) {
	        try {
	            List<String> statuses = null;
	            if (userStatus != null && !userStatus.isEmpty()) {
	                statuses = Arrays.asList(userStatus.split(","));
	            }

	            return repo.findServiceActiveUsers(serviceName, statuses, msgFromDate, msgToDate);
	        } catch (Exception e) {
	            com.rakesh.sms.util.Logger.sysLog(com.rakesh.sms.util.LogValues.error,
	                    this.getClass().getName(), com.rakesh.sms.util.Logger.getStack(e));
	            return new ArrayList<>();
	        }
	    }

	@Transactional
	public List<String> getMostActiveUser(int mlimit) {
	    List<String> msisdns = new ArrayList<>();
	    try {
	        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	        List<Object[]> result = cdrSmsLogsRepository.findMostActiveUsers(currentDate, mlimit);

	        for (Object[] row : result) {
	            String msisdn = String.valueOf(row[0]);
	            long count = ((Number) row[1]).longValue();
	            msisdns.add(msisdn);
	            Logger.sysLog(LogValues.info, this.getClass().getName(),
	                    "Msisdn: " + msisdn + " with count: " + count);
	        }

	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	    }

	    return msisdns;
	}

	public boolean updateSubscribedUserStatus1(String promoName, List<String> msisdn, String status, int count) {
		boolean result = false;


		return true;
	}


}
