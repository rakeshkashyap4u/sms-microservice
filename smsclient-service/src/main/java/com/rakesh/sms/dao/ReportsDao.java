package com.rakesh.sms.dao;

import com.rakesh.sms.entity.CallbackDetails;
import com.rakesh.sms.entity.SmsLogs;

public interface ReportsDao {

	public Integer addCallbackDetails(CallbackDetails callback);

	public CallbackDetails getCallbackDetails(String action, String serviceid, String subServiceid);

	public CallbackDetails getCallbackDetails(int cid);

	public SmsLogs getLog(String messageId);

	public void addLog(SmsLogs log);

}
