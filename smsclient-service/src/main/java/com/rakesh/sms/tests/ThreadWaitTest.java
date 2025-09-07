package com.rakesh.sms.tests;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Random;

public class ThreadWaitTest implements Runnable {

	private Thread mythread;
	private Object lock;

	public static void main(String args[]) {

		ThreadWaitTest twt = new ThreadWaitTest();
		twt.init();

	}// End Of Main

	private void init() {

		this.lock = new Object();

		try {
			long threadID = 0L;

			for (int i = 0; i < 10; i++) {

				this.mythread = new Thread(this);
				this.mythread.start();
				threadID = this.mythread.getId();

				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println(ThreadWaitTest.getThreadDump());
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

				System.out.println("\n --------------" + i + "--------------- ");
				System.out.println(" Thread[" + threadID + "] Started...");

				synchronized (this.lock) {
					System.out.println(" Thread[" + threadID + "] Waiting...");
					this.lock.wait(5000);
				}

				synchronized (this.lock) {
					this.lock.notify();
				}

				System.out.println(" [" + threadID + "] End Of Iteration... ");

			} // End Of Loop

		} catch (Exception e) {
			e.printStackTrace();
		} // End Of Try Catch

	}// End Of Method

	public void run() {

		long threadID = this.mythread.getId();

		try {
			Random rand = new Random();
			int sleepTime = 500 + rand.nextInt(6500);
			System.out.println(" Thread [" + threadID + "] Sleeping for " + sleepTime + "ms...");
			Thread.sleep(sleepTime);
			System.out.println(" Notifying Thread [" + threadID + "] ...");
			synchronized (this.lock) {
				this.lock.notify();
			}
			System.out.println(" Thread [" + threadID + "] Notified...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // End Of Try Catch

	}// End Of Thread

	public static String getThreadDump() {

		StringBuilder dump = new StringBuilder("");
		final ThreadMXBean mxbean = ManagementFactory.getThreadMXBean();
		final ThreadInfo[] threadInfos = mxbean.getThreadInfo(mxbean.getAllThreadIds(), 100);

		for (ThreadInfo thread : threadInfos) {

			dump.append('\n').append('"').append(thread.getThreadName()).append('"');

			final Thread.State state = thread.getThreadState();
			dump.append('\n').append(" java.lang.Thread.State: ").append(state).append('\n');

			final StackTraceElement[] stackElements = thread.getStackTrace();
			for (StackTraceElement stack : stackElements) {
				dump.append("  at ").append(stack).append('\n');
			} // End Of Loop

			dump.append('\n');
		} // End Of Loop

		return dump.toString().trim();
	}// End Of Method

}
