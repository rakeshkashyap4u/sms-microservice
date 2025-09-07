package com.rakesh.sms.scheduler;

public interface JobScheduler extends Runnable {

	public abstract void run();

	public abstract void end();

}// End Of Interface
