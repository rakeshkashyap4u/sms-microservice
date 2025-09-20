package com.rakesh.sms.boImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakesh.sms.beans.AlertServiceDetails;
import com.rakesh.sms.beans.SmsPromotion;
import com.rakesh.sms.bo.AlertsBo;
import com.rakesh.sms.dao.AlertsDao;
import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.entity.AlertsContent;
import com.rakesh.sms.entity.SmsSubscription;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;


@Service("alertsBo")  
public class AlertsBoImpl implements AlertsBo {

	@Autowired
	private AlertsDao subsDaoImpl;

	public void setSubsDaoImpl(AlertsDao subsDaoImpl) {
		this.subsDaoImpl = subsDaoImpl;
	}

	public SmsSubscription saveOrUpdate(SmsSubscription request) {
		return this.subsDaoImpl.saveOrUpdate(request);
	}

	public void updateLastUpdated(List<SmsSubscription> msisdns) {
		this.subsDaoImpl.updateLastUpdated(msisdns);
	}

	public void delete(SmsSubscription request) {
		this.subsDaoImpl.delete(request);
	}

	public List<ActiveAlerts> getAllActiveAlerts() {
		return this.subsDaoImpl.getAllActiveAlerts();
	}

	public ActiveAlerts getActiveAlert(String serviceid, String subserviceid) {
		return this.subsDaoImpl.getActiveAlert(serviceid.trim(), subserviceid);
	}

	public List<SmsSubscription> getAllActiveUsers(String serviceid, String subserviceid) {
		return this.subsDaoImpl.getActiveUsers(serviceid.trim(), subserviceid, null);
	}

	public List<SmsSubscription> getComboUsers(String serviceid, String subserviceid) {

		List<SmsSubscription> users = this.subsDaoImpl.getActiveUsers(serviceid.trim(), subserviceid, null);
		List<SmsSubscription> comboUsers = new ArrayList<SmsSubscription>();
		Calendar cal = Calendar.getInstance();

		try {

			long yesterday = cal.getTimeInMillis() - (1000 * 60 * 60 * 24);
			Date yesterdayCheck = new Date(yesterday);
			yesterday += (1000 * 60 * 60);
			Date today = new Date(yesterday);

			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" Combo Alerts | Fetching Users | serviceid=" + serviceid);

			for (int i = 0; i < users.size(); i++) {

				SmsSubscription subs = users.get(i);
				Logger.sysLog(LogValues.debug, this.getClass().getName(),
						" Combo Alerts:: Start= " + yesterdayCheck.toString() + " | End= " + today.toString());

				if ((subs.getLastprocessed().after(yesterdayCheck) || subs.getLastprocessed().equals(yesterdayCheck))
						&& subs.getLastprocessed().before(today))
					comboUsers.add(subs);

			} // End Of Loop

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		} // End Of Try Catch

		users.clear();
		return comboUsers;

	}// End Of Method

	public List<SmsSubscription> getNamazUsers(String serviceid, String subserviceid, String language) {
		return this.subsDaoImpl.getActiveUsers(serviceid.trim(), subserviceid, language);
	}// End Of Method

	public List<String> getServiceAlertUsers(String serviceid, String subserviceid, String language, int days) {
		return this.subsDaoImpl.getServiceAlertUsers(serviceid.trim(), subserviceid, language, days);
	}// End Of Method

	public AlertsContent getComboMessage(SmsSubscription request) {
		return this.subsDaoImpl.getComboMessage(request);
	}// End Of Method

	public AlertsContent getNamazMessage(String serviceid, String subserviceid, Date time, String language) {
		if (time != null) {
			return this.subsDaoImpl.getNamazMessage(serviceid.trim(), subserviceid, time, language);
		} else {
			return this.subsDaoImpl.getNamazMessage(serviceid.trim(), subserviceid, new Date(), language);
		}
	}// End Of Method

	public AlertsContent getServiceAlertMessage(String serviceid, String subserviceid, Date time, String language) {
		if (time != null) {
			return this.subsDaoImpl.getServiceAlertMessage(serviceid.trim(), subserviceid, time, language);
		} else {
			return this.subsDaoImpl.getServiceAlertMessage(serviceid.trim(), subserviceid, new Date(), language);
		}
	}// End Of Method

	public boolean isSubscribedForAlerts(String msisdn, String serviceid, String subserviceid) {
		return this.subsDaoImpl.isSubscribedForAlerts(msisdn.trim(), serviceid.trim(), subserviceid);
	}// End Of Method

	public int addPromotionLog(SmsPromotion details) {
		return this.subsDaoImpl.addPromotionLog(details);
	}// End Of Method

	public void updateFlag(String msisdn, String serviceid, String subserviceid, String flag) {
		this.subsDaoImpl.updateFlag(msisdn.trim(), serviceid.trim(), subserviceid, flag.trim());
	}// End Of Method

	public boolean namazMigration(List<SmsSubscription> users) {
		return this.subsDaoImpl.namazMigration(users);
	}// End Of Method

	public boolean uploadAlertContent(List<AlertsContent> contents) {
		return this.subsDaoImpl.uploadAlertContent(contents);
	}// End Of Method

	public List<AlertServiceDetails> getServicesWithSMSContent() {
		return this.subsDaoImpl.getServicesWithSMSContent();
	}// End Of Method

	public void deleteAlertContentOf(List<AlertServiceDetails> services) {
		this.subsDaoImpl.deleteAlertContentOf(services);
	}// End Of Method

}
