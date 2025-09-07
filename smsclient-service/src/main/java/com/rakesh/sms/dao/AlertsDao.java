package com.rakesh.sms.dao;

import java.util.Date;
import java.util.List;

import com.rakesh.sms.beans.AlertServiceDetails;
import com.rakesh.sms.beans.SmsPromotion;
import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.entity.AlertsContent;
import com.rakesh.sms.entity.SmsSubscription;

public interface AlertsDao {

	public SmsSubscription saveOrUpdate(SmsSubscription request);

	public void updateLastUpdated(List<SmsSubscription> msisdns);

	public void delete(SmsSubscription request);

	public boolean isSubscribedForAlerts(String msisdn, String serviceid, String subserviceid);

	public void updateFlag(String msisdn, String serviceid, String subserviceid, String flag);

	public List<ActiveAlerts> getAllActiveAlerts();

	public ActiveAlerts getActiveAlert(String serviceid, String subserviceid);

	public List<SmsSubscription> getActiveUsers(String serviceid, String subserviceid, String language);

	public List<String> getServiceAlertUsers(String serviceid, String subserviceid, String language, int days);

	public AlertsContent getComboMessage(SmsSubscription request);

	public AlertsContent getNamazMessage(String serviceid, String subserviceid, Date time, String language);

	public AlertsContent getServiceAlertMessage(String serviceid, String subserviceid, Date time, String language);

	public boolean namazMigration(List<SmsSubscription> users);

	public boolean uploadAlertContent(List<AlertsContent> contents);

	public List<AlertServiceDetails> getServicesWithSMSContent();

	public void deleteAlertContentOf(List<AlertServiceDetails> services);

	public int addPromotionLog(SmsPromotion details);

}
