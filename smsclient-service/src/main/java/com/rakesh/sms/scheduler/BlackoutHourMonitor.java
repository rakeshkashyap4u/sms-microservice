package com.rakesh.sms.scheduler;

import java.util.Date;

import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class BlackoutHourMonitor extends Thread {

	private static Boolean blackout;

	public static boolean isBlackout() {
		return blackout.booleanValue();
	}// End Of Method

	@Override
	public void run() {

		BlackoutHourMonitor.blackout = false;

		try {

			while (true) {

				if (CoreUtils.isBlackoutHours()) {
					blackout = true;
					Date end = CoreUtils.getEndOfCurrentBlackoutHour();
					long diff = end.getTime() - System.currentTimeMillis();
					diff += 500;
					Logger.sysLog(LogValues.info, this.getClass().getName(), " ---In Blackout Hours--- ");
					Thread.sleep(diff);
				} else {
					blackout = false;
					Thread.sleep(300000L); // 5mins
				} // End Of IF ELSE

			} // End Of Loop

		} catch (InterruptedException ie) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Stopping Blackout Hour Monitor...!!! ");
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		} // End Of Try Catch

	}// End Of Thread

}// End Of Class
