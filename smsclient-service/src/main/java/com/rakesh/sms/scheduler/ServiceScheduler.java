package com.rakesh.sms.scheduler;

import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.bo.AlertsBo;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.entity.AlertsContent;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.util.AppContext;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;
import com.rakesh.sms.util.CoreEnums.ExpiryUnit;

public class ServiceScheduler implements JobScheduler {

	private String serviceid, subserviceid, circle, callerID, language;
	private CoreEnums.Protocol protocol;
	private boolean inRunningState;
	private AlertsBo alertBoImpl;
	private int priority;

	protected ServiceScheduler(ActiveAlerts jobAlert) {

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

		String subLog = "[" + this.serviceid + "][" + this.subserviceid + "][" + this.language + "]";
		Calendar cal = Calendar.getInstance();
		Date sendingTime = cal.getTime();
		this.inRunningState = true;
		int today, yest;
		String Todate;

		yest = cal.get(Calendar.DAY_OF_MONTH);

		while (this.inRunningState) {

			cal = Calendar.getInstance();
			today = cal.get(Calendar.DAY_OF_MONTH);
			Todate = String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
					+ String.valueOf(today) + " ";
			sendingTime = cal.getTime();

			if (yest != today) {
				yest = today;
			}

			AlertsContent content = this.alertBoImpl.getServiceAlertMessage(this.serviceid, this.subserviceid,
					sendingTime, this.language);

			if (content != null && sendingTime != null) {

				try {

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							subLog + " Content fetched for service alert : " + content.getMsgText());

					sendingTime = CoreUtils.getDate(Todate + content.getSendingTime().toString() + ".000", subLog);
					long timeDiffInMillis = sendingTime.getTime() - cal.getTimeInMillis();

					if (timeDiffInMillis > 60000) {
						timeDiffInMillis -= 60000L; // 1 Minute Margin
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								subLog + " Waiting for " + timeDiffInMillis / 1000 + " seconds to push Alerts ");
						Thread.sleep(timeDiffInMillis);
					}

				} catch (InterruptedException ie) {
					this.inRunningState = false;
					break;
				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							subLog + " Error while sending Namaz Message ");

					try {
						/** DO NOT REMOVE --- 1min wait before picking next Alert */
						Thread.sleep(60000);
					} catch (InterruptedException ie) {
					}

					cal = Calendar.getInstance();
					sendingTime = cal.getTime();
					continue;
				} // End Of Try Catch

				List<String> users = this.alertBoImpl.getServiceAlertUsers(this.serviceid, this.subserviceid,
						this.language, new Integer(CoreUtils.getExtraParam("days")));

				if (users.size() > 0)
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							subLog + " Users fetched from the database : " + users.toString());

				List<Message> alerts = new ArrayList<Message>();

				LanguageSpecification spec = CoreUtils.getLanguageSpecifications(this.language);

				Date expiryTime = CoreUtils.getExpiryTime(180, ExpiryUnit.MINS);

				for (int i = 0; i < users.size(); i++) {

					String msisdn = users.get(i);
					String text = content.getMsgText();
					
					if(text.contains("$uid$"))
					{
						
						String phraseToEncrypt = CoreUtils.getProperty("uid");
						
						String finalmsisdn =  CoreUtils.stripCodes(msisdn);
						
						
						try {
							text=	CoreUtils.getUid(finalmsisdn);
						} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
								| IllegalBlockSizeException | BadPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					try {
						text = URLDecoder.decode(text, "UTF-8");
					} catch (Exception e) {
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								"Error in decoding alerts content to UTF-8!");
					}

					Message msg = new Message(this.callerID, msisdn, this.priority, text, this.protocol.ordinal(),
							CoreEnums.Type.MT.ordinal());
					msg.setServiceid(this.serviceid + ":" + this.subserviceid);
					msg.setExpiryTime(expiryTime);
					msg.setExtraDetail("ServiceAlert");
					msg.setCircle(this.circle);

					if (msg.setLanguageSpecifications(spec) == false) {
						msg.setEncoding("true");
					}

					alerts.add(msg);
				} // End Of Loop

				CoreUtils.pushAlertsToSmsQueue(alerts);

				if (users.size() > 0) {
					users.clear();
				} else
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							subLog + " No Active USER to Send Service Alert ");

				try {
					/** DO NOT REMOVE --- 1min wait before picking next Alert */
					Thread.sleep(60000);
				} catch (InterruptedException e) {
				}

			} else {

				Logger.sysLog(LogValues.info, this.getClass().getName(), subLog + " Wating for next Service Alert... ");

				try {
					content = null;
					// Thread.sleep(1000*30);
					Thread.sleep(1000 * 60 * 59); // For 59mins
				} catch (InterruptedException ie) {
					this.inRunningState = false;
					break;
				} catch (Exception e) {
					Logger.sysLog(LogValues.warn, this.getClass().getName(),
							subLog + " Wating ended for next Service Alert... ");
				} // End Of Try Catch

			} // End Of IF Else

		} // End Of While Loop

	}// End Of Thread

	public void end() {
		this.inRunningState = false;
	}// End Of Method

}// End Of Scheduler Class
