package com.rakesh.sms.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.bo.AlertsBo;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.entity.AlertsContent;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.SmsSubscription;
import com.rakesh.sms.util.AppContext;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class ComboScheduler implements JobScheduler {

	private String serviceid, subserviceid, circle, callerID, language;
	private CoreEnums.Protocol protocol;
	private boolean inRunningState;
	private AlertsBo alertBoImpl;
	private int priority;

	protected ComboScheduler(ActiveAlerts jobAlert) {

		this.alertBoImpl = (AlertsBo) AppContext.getBean("alertsBo");

		this.protocol = CoreEnums.Protocol.values[jobAlert.getProtocol()];
		this.serviceid = jobAlert.getServiceid();
		this.priority = jobAlert.getPriority();

		String subservice = jobAlert.getSubserviceid();
		if (subservice != null && subservice.length() > 0)
			this.subserviceid = subservice;
		else
			this.subserviceid = new String("");

		String callerID = jobAlert.getCli();
		if (callerID != null && callerID.length() > 0)
			this.callerID = callerID;
		else
			this.callerID = CoreUtils.getProperty("callerID");

		String circle = jobAlert.getCircle();
		if (circle != null && circle.length() > 0)
			this.circle = circle;
		else
			this.circle = Pusher.getDefaultCircle();

		this.language = jobAlert.getLanguage();

	}// End Of Constructor

	public void run() {

		String subLog = "[" + this.serviceid + "][" + this.subserviceid + "]";
		this.inRunningState = true;

		while (this.inRunningState) {

			List<SmsSubscription> msisdns = this.alertBoImpl.getComboUsers(this.serviceid, this.subserviceid);
			List<Message> alerts = new ArrayList<Message>();

			LanguageSpecification spec = CoreUtils.getLanguageSpecifications(this.language);
			for (int j = 0; j < msisdns.size(); j++) {

				try {
					SmsSubscription msisdn = msisdns.get(j);
					AlertsContent smsDetails = this.alertBoImpl.getComboMessage(msisdn);

					try {

						Message msg = new Message(this.callerID, msisdn.getMsisdn(), this.priority,
								smsDetails.getMsgText(), this.protocol.ordinal(), CoreEnums.Type.MT.ordinal());
						msg.setServiceid(this.serviceid);
						msg.setExtraDetail("Combo");
						msg.setCircle(this.circle);

						if (msg.setLanguageSpecifications(spec) == false) {
							msg.setEncoding("true");
						}

						alerts.add(msg);

					} catch (Exception e) {
						Logger.sysLog(LogValues.error, this.getClass().getName(),
								subLog + " Unable to push Combo Alert :: " + smsDetails + " :: " + e.getMessage());
					} // End Of Try Catch

				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							subLog + " Unable to push Combo Alert for the MSISDN --- \n" + Logger.getStack(e));
				} // End Of Try Catch
			} // End Of Loop

			CoreUtils.pushAlertsToSmsQueue(alerts);

			if (msisdns.size() > 0) {
				this.alertBoImpl.updateLastUpdated(msisdns);
				msisdns.clear();
			} else
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						subLog + " No Active USER to Send Combo Alert ");

			try {
				Thread.sleep(1000 * 60 * 59); // For 60mins - 1mins(Margin)
			} catch (InterruptedException ie) {
				this.inRunningState = false;
				break;
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), subLog + "  " + e.getMessage());
			} // End Of Try Catch

		} // End Of While Loop

	}// End Of Thread

	public void end() {
		this.inRunningState = false;
	}// End Of Method

}// End Of Scheduler Class
