package com.rakesh.sms.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.rakesh.sms.bo.AlertsBo;
import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.util.AppContext;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class StartScheduler extends Thread {

	private static Map<JobScheduler, Thread> Jobs;
	private static AlertsBo alertBo;

	static {
		StartScheduler.Jobs = new HashMap<JobScheduler, Thread>();
	}

	public StartScheduler() {
		super();
		StartScheduler.alertBo = (AlertsBo) AppContext.getBean("alertsBo");
	}// End Of Constructor

	public void run() {
		
		System.out.println("this is running now");

		List<ActiveAlerts> alerts = StartScheduler.alertBo.getAllActiveAlerts();

		try {
			Thread.sleep(1000 * 30); // Sleep for 30 Seconds then Start
		} catch (InterruptedException e) {
			Logger.sysLog(LogValues.info, StartScheduler.class.getName(), " Alert Scheduler Interrupted [1] ");
			return;
		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, StartScheduler.class.getName(),
					e.getMessage() + "::  Alert Scheduler Stopped Unexpectedly ");
			return;
		}

		Logger.sysLog(LogValues.info, StartScheduler.class.getName(), " Alerts-Scheduler Started... " + alerts.size());

		try {

			for (int i = 0; i < alerts.size(); i++) {

				ActiveAlerts alert = alerts.get(i);
				JobScheduler job = null;
				

				Logger.sysLog(LogValues.info, StartScheduler.class.getName(), " Alerts TYPE " + alert.getType());

				if (alert.getType() == CoreEnums.AlertType.NAMAZ.ordinal())
					job = new NamazScheduler(alert);
				else if (alert.getType() == CoreEnums.AlertType.COMBO.ordinal())
					job = new ComboScheduler(alert);
				else if (alert.getType() == CoreEnums.AlertType.SERVICE.ordinal())
					job = new ServiceScheduler(alert);
				else if (alert.getType() == CoreEnums.AlertType.FEATURE.ordinal())
					job = new FeatureScheduler(alert);
				else
					Logger.sysLog(LogValues.error, StartScheduler.class.getName(), " Alert Type unknown for serviceid="
							+ alert.getServiceid() + " and subserviceid=" + alert.getSubserviceid());

				if (job != null) {
					Thread jobThread = new Thread(job);
					StartScheduler.Jobs.put(job, jobThread);
					jobThread.start();
				} // End of IF

			} // End Of Loop

			alerts.clear();

		} catch (IllegalStateException ise) {
			Logger.sysLog(LogValues.info, StartScheduler.class.getName(), " Alert Scheduler Interrupted [2] ");
		} catch (Exception e) {
			Logger.sysLog(LogValues.warn, StartScheduler.class.getName(),
					e.getMessage() + "::  Alert Scheduler Stopped Unexpectedly ");
		} // End Of Try Catch

	}// End Of Thread

	/**
	 **** 'static synchronized' Methods (IMPORTANT) Helps in synchronization of Active
	 * Job List. Since multiple threads are accessing the same list through these
	 * static methods, 'synchronized' keyword will allow only one thread to access
	 * only one synchronized method of this class at a time.
	 */

	public static synchronized void addJob(JobScheduler scheduler, Thread thread) {
		StartScheduler.Jobs.put(scheduler, thread);
	}// End Of Method

	public static synchronized void stopJob(JobScheduler job) {
		if (Jobs.containsKey(job)) {

			Thread jobThread = StartScheduler.Jobs.get(job);
			job.end();

			try {
				if (jobThread.isAlive() || jobThread.isDaemon()) {
					jobThread.interrupt();
				} // End Of IF
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, StartScheduler.class.getName(),
						" Error Stopping Promotion [" + ((Promotions) job).getName() + "] \n" + Logger.getStack(e));
			} // End Of Try Catch

			StartScheduler.Jobs.remove(job);

		} // End Of IF
	}// End Of Method

	public static synchronized void finishJob(JobScheduler job) {

		// System.out.println("Promotion finished:"+CoreUtils.getTimeStamp(new Date()));
		StartScheduler.Jobs.remove(job);
	}// End Of Method

	public static synchronized List<Promotions> getLivePromotions() {

		List<Promotions> promos = new ArrayList<Promotions>(5);

		Iterator<JobScheduler> jobs = StartScheduler.Jobs.keySet().iterator();

		while (jobs.hasNext()) {
			JobScheduler job = jobs.next();

			if (job instanceof Promotions) {
				promos.add((Promotions) job);
			}
		} // End Of Loop

		return promos;

	}// End Of Method

	/** Stop all the Threads Running for Alerts and Promotions */
	public static synchronized void endJobs() {

		try {

			Logger.sysLog(LogValues.info, StartScheduler.class.getName(), " Stopping Alerts-Scheduler... ");
			Iterator<JobScheduler> jobs = StartScheduler.Jobs.keySet().iterator();

			while (jobs.hasNext()) {

				JobScheduler job = jobs.next();
				Thread jobThread = StartScheduler.Jobs.get(job);
				job.end();

				if (jobThread.isAlive() || jobThread.isDaemon()) {
					jobThread.interrupt();
					jobThread.join();
				} // End Of IF

			} // End Of Loop

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, StartScheduler.class.getName(),
					" Error Ending Scheduler Job for SMS Alerts \n" + Logger.getStack(e));
		} // End Of Try Catch

		StartScheduler.Jobs.clear();

	}// End Of Method

}
