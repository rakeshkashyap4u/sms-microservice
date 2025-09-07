package com.rakesh.sms.scheduler;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.rakesh.sms.util.CoreEnums.ExpiryUnit;

public class NamazScheduler implements JobScheduler {

	private String serviceid, subserviceid, circle, callerID, language;
	private CoreEnums.Protocol protocol;
	private boolean inRunningState;
	private AlertsBo alertBoImpl;
	private int priority;
	private int jobAlertExpiryTime;

	protected NamazScheduler(ActiveAlerts jobAlert) {

		this.alertBoImpl = (AlertsBo) AppContext.getBean("alertsBo");

		this.protocol = CoreEnums.Protocol.values[jobAlert.getProtocol()];
		this.serviceid = jobAlert.getServiceid();
		this.priority = jobAlert.getPriority();
		
		System.out.println("you are in NamazScheduler ");

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

		this.jobAlertExpiryTime = jobAlert.getExpiryTime();

	}// End Of Constructor

	public void run() {

		String subLog = "[" + this.serviceid + "][" + this.subserviceid + "][" + this.language + "]";
		Calendar cal = Calendar.getInstance();
		Date sendingTime = cal.getTime();
		this.inRunningState = true;
		int today, yest;
		String Todate;

		yest = cal.get(Calendar.DAY_OF_MONTH);
		
		System.out.println("now here");

		Logger.sysLog(LogValues.info, this.getClass().getName(), subLog + " Scheduler started to push Alerts ");

		while (this.inRunningState) {

			cal = Calendar.getInstance();
			today = cal.get(Calendar.DAY_OF_MONTH);
			Todate = String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
					+ String.valueOf(today) + " ";
			sendingTime = cal.getTime();
			
			System.out.println("sendingTime "+sendingTime);

			if (yest != today) 
			{
				yest = today;
			}
			
			

			AlertsContent content = this.alertBoImpl.getNamazMessage(this.serviceid, this.subserviceid, sendingTime,
					this.language);
			
			System.out.println("content"+content);

			if (content != null && sendingTime != null) 
			{

				try {

					sendingTime = CoreUtils.getDate(Todate + content.getSendingTime().toString() + ".000", subLog);
					long timeDiffInMillis = sendingTime.getTime() - cal.getTimeInMillis();

					if (timeDiffInMillis > 60000) 
					{
						timeDiffInMillis -= 60000L; // 1 Minute Margin
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								subLog + " Waiting for " + timeDiffInMillis / 1000 + " seconds to push Alerts ");
						Thread.sleep(timeDiffInMillis);
					}

				} catch (InterruptedException ie) {
					this.inRunningState = false;
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							subLog + " Alerts Exception occured: " + Logger.getStack(ie));
					break;
				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							subLog + " Error while sending Namaz Message " + Logger.getStack(e));

					try {
						/** DO NOT REMOVE --- 1min wait before picking next Alert */
						Thread.sleep(60000);
					} catch (InterruptedException ie) {
					}

					cal = Calendar.getInstance();
					sendingTime = cal.getTime();
					continue;
				} // End Of Try Catch

				List<SmsSubscription> msisdns = this.alertBoImpl.getNamazUsers(this.serviceid, this.subserviceid,
						this.language);
				List<Message> alerts = new ArrayList<Message>();

				LanguageSpecification spec = CoreUtils.getLanguageSpecifications(this.language);

				Date expiryTime = CoreUtils.getExpiryTime(30, ExpiryUnit.MINS);

				if (CoreUtils.getExtraParam("namazExpiryTime") != null
						&& !CoreUtils.getExtraParam("namazExpiryTime").equals(""))
					expiryTime = CoreUtils.getExpiryTime(Integer.parseInt(CoreUtils.getExtraParam("namazExpiryTime")),
							ExpiryUnit.MINS);

				if (jobAlertExpiryTime > 0)
					expiryTime = CoreUtils.getExpiryTime(jobAlertExpiryTime, ExpiryUnit.MINS);

				for (int i = 0; i < msisdns.size(); i++) {

					String msisdn = msisdns.get(i).getMsisdn();
					String text = content.getMsgText();

					try {
						text = URLDecoder.decode(text, "UTF-8");
					} catch (Exception e) {
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"Error in decoding alerts content to UTF-8!");
					}

					Message msg = new Message(this.callerID, msisdn, this.priority, text, this.protocol.ordinal(),
							CoreEnums.Type.MT.ordinal());
					msg.setServiceid(msisdns.get(i).getParam1());
					msg.setExpiryTime(expiryTime);
					msg.setExtraDetail(msisdns.get(i).getParam2());
					msg.setCircle(this.circle);

					if (msg.setLanguageSpecifications(spec) == false) {
						msg.setEncoding("true");
					}

					alerts.add(msg);
				} // End Of Loop

				CoreUtils.pushAlertsToSmsQueue(alerts);

				if (msisdns.size() > 0) {
					this.alertBoImpl.updateLastUpdated(msisdns);
					msisdns.clear();
				} else
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							subLog + " No Active USER to Send Namaz Alert ");

				try {
					/** DO NOT REMOVE --- 1min wait before picking next Alert */
					Thread.sleep(60000);
				} catch (InterruptedException e) {
				}

			} else {

				Logger.sysLog(LogValues.info, this.getClass().getName(), subLog + " Wating for next Namaz Alert... ");

				try {
					content = null;
					// Thread.sleep(1000*30);
					Thread.sleep(1000 * 60 * 59); // For 59mins
				} catch (InterruptedException ie) {
					this.inRunningState = false;
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							subLog + " Alerts1 Exception occured " + Logger.getStack(ie));
					break;
				} catch (Exception e) {
					Logger.sysLog(LogValues.warn, this.getClass().getName(),
							subLog + " Wating ended for next Namaz Alert... " + Logger.getStack(e));
				} // End Of Try Catch

			} // End Of IF Else

		} // End Of While Loop

	}// End Of Thread

	public void end() {
		this.inRunningState = false;
		Logger.sysLog(LogValues.info, this.getClass().getName(), " Scheduler stopped to push Alerts ");
	}// End Of Method

}// End Of Scheduler Class
