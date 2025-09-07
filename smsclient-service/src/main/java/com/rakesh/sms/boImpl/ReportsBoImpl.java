package com.rakesh.sms.boImpl;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.bo.ReportsBo;
import com.rakesh.sms.dao.ReportsDao;
import com.rakesh.sms.entity.CallbackDetails;
import com.rakesh.sms.entity.SmsLogs;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class ReportsBoImpl implements ReportsBo {

	private ReportsDao reportsDaoImpl;

	public void setReportsDaoImpl(ReportsDao reportsDaoImpl) {
		this.reportsDaoImpl = reportsDaoImpl;
	}

	public Integer addCallbackDetails(CallbackDetails callback) {
		return this.reportsDaoImpl.addCallbackDetails(callback);
	}

	public CallbackDetails getCallbackDetails(String serviceid, String subServiceid) {
		return this.reportsDaoImpl.getCallbackDetails(null, serviceid, subServiceid);
	}

	public CallbackDetails getCallbackDetails(String action, String serviceid, String subServiceid) {
		return this.reportsDaoImpl.getCallbackDetails(action, serviceid, subServiceid);
	}

	public void addLog(SmsLogs log) {
		this.reportsDaoImpl.addLog(log);
	}

	public Message getMessageLog(String messageId) {

		Message msg = null;

		try {

			SmsLogs log = this.reportsDaoImpl.getLog(messageId);

			if (log != null) {
				CallbackDetails callback = this.reportsDaoImpl.getCallbackDetails(log.getCallback());

				msg = new Message(log.getSender(), log.getReceiver());
				msg.setCircle(log.getCircle());
				msg.setCallback(true);
				msg.getCallbackDetails().set(callback);
				msg.getCallbackDetails().setShortcode(log.getSender());
				msg.getCallbackDetails().setPrice(log.getPrice().toString());
				msg.getCallbackDetails().setTransactionId(log.getTransId());
			} else
				Logger.sysLog(LogValues.debug, this.getClass().getName(), " No Callback Log Found for " + messageId);

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		return msg;

	}// End Of Method

}
