package com.rakesh.sms.bo;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.entity.CallbackDetails;
import com.rakesh.sms.entity.SmsLogs;

public interface ReportsBo {

	public Integer addCallbackDetails(CallbackDetails callback);

	public CallbackDetails getCallbackDetails(String serviceid, String subServiceid);

	public CallbackDetails getCallbackDetails(String action, String serviceid, String subServiceid);

	public void addLog(SmsLogs log);

	public Message getMessageLog(String messageId);

}
