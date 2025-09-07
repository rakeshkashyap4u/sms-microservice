package com.rakesh.sms.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class JConsole extends Thread {

	private String result;

	public static String execute() {

		JConsole jc = new JConsole();
		jc.start();

		try {
			jc.join();
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, JConsole.class.getName(), Logger.getStack(e));
		}

		return jc.result;

	}// End Of Method

	@Override
	public void run() {

		try {

			StringBuffer output = new StringBuffer();
			String line = "";

			Logger.sysLog(LogValues.info, this.getClass().getName(), " Starting JConsole for MBeans RMI ");
			Process p = Runtime.getRuntime().exec("jconsole");
			p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = reader.readLine()) != null)
				output.append(line + "\n");

			this.result = output.toString();
			Logger.sysLog(LogValues.debug, this.getClass().getName(), this.result);

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

	}// End Of Thread

}
