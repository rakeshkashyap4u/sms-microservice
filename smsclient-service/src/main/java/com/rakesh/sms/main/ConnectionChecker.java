package com.rakesh.sms.main;

import java.util.List;

import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class ConnectionChecker implements Runnable {

	private List<String> trxCircles;
	private List<String> rxCircles;
	private Thread runningThread;
	private boolean running;

	public ConnectionChecker() {
		this.trxCircles = Pusher.getGatewayBoImpl().getTRXCircleNames(); // added because ussd sent through smpp gateway
																			// as well
		this.rxCircles = Pusher.getGatewayBoImpl().getRXCircleNames();
		this.runningThread = null;
		this.running = false;
	}// End Of Constructor

	public Thread startChecking() {
		this.runningThread = new Thread(this);
		this.runningThread.start();
		return this.runningThread;
	}// End Of Method

	public void stopChecking() {
		if (this.running) {
			this.running = false;
			this.runningThread.interrupt();
		}
	}// End Of Method

	public void run() {

		final int thirtySeconds = 30 * 1000;
		this.running = true;

		try {

			/**
			 * Delay added... To remove conflict of starting TWO Connections on Application
			 * startup...
			 */
			Thread.sleep(30 * 1000);

			while (this.running) {

				if (this.rxCircles.size() == 0 && this.trxCircles.size() == 0) {
					this.running = false;
					break;
				}

				for (int i = 0; i < this.rxCircles.size(); i++) {

					String circle = this.rxCircles.get(i);
					boolean status = ReConnector.reconnect(circle);

					if (status == false) {
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								" Connection for RX Circle[" + circle + "] Initiated...");
					}

				} // End Of For Loop

				for (int i = 0; i < this.trxCircles.size(); i++) {

					String circle = this.trxCircles.get(i);
					boolean status = ReConnector.reconnect(circle);

					if (status == false) {
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								" Connection for TRX Circle[" + circle + "] Initiated...");
					}

				} // End Of For Loop

				Thread.sleep(thirtySeconds);

			} // End Of Loop

		} catch (InterruptedException ie) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), " RX Connection Checker stopped gracefully... ");
		} // End Of Try Catch

	}// End Of Thread

}
