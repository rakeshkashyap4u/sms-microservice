package com.rakesh.sms.daoImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.hibernate.Session;

import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.ReportsDao;
import com.rakesh.sms.entity.CallbackDetails;
import com.rakesh.sms.entity.SmsLogs;
import com.rakesh.sms.jpas.CallbackDetailsRepository;
import com.rakesh.sms.jpas.SmsLogsRepository;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class ReportsDaoImpl implements ReportsDao {

	 private final CallbackDetailsRepository callbackRepo;
	    private final SmsLogsRepository smsLogsRepo;

	    public ReportsDaoImpl(CallbackDetailsRepository callbackRepo, SmsLogsRepository smsLogsRepo) {
	        this.callbackRepo = callbackRepo;
	        this.smsLogsRepo = smsLogsRepo;
	    }

	    public Integer addCallbackDetails(CallbackDetails callback) {
	        try {
	            return callbackRepo.save(callback).getId(); // assuming getId() returns Integer
	        } catch (Exception e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	            return 0;
	        }
	    }

	    public CallbackDetails getCallbackDetails(String action, String serviceid, String subServiceid) {
	        try {
	            return callbackRepo.findFirstByServiceidAndSubServiceidAndAction(serviceid, subServiceid, action)
	                               .orElse(null);
	        } catch (Exception e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	            return null;
	        }
	    }

	    public CallbackDetails getCallbackDetails(int cid) {
	        try {
	            return callbackRepo.findById(cid).orElse(null);
	        } catch (Exception e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	            return null;
	        }
	    }

	    public void addLog(SmsLogs log) {
	        try {
	            smsLogsRepo.save(log);
	        } catch (Exception e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	        }
	    }

	    public SmsLogs getLog(String messageId) {
	        try {
	            Calendar cal = Calendar.getInstance();
	            cal.add(Calendar.DAY_OF_MONTH, -3);
	            Date threeDaysAgo = cal.getTime();
	            return smsLogsRepo.findFirstByMessageIdAndAutotimestampAfter(messageId, threeDaysAgo)
	                              .orElse(null);
	        } catch (Exception e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	            return null;
	        }
	    }
	}