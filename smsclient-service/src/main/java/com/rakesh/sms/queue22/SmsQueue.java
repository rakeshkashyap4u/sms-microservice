package com.rakesh.sms.queue22;

import java.sql.Date;

import com.rakesh.sms.beans.Message;

//Old (pre-Boot 3)


//New (Boot 3 / Jakarta EE 9+)
import jakarta.jms.Session;

public class SmsQueue {

//	private static Long[] newMessageCount = new Long[3];
	private static Long[] newMessageCount = new Long[4];  //Airtel Malawi
	public static final String Queue = "smsqueue";
	private static Session session;
	
	Date date = new Date(0);

	protected static void init(Session session) {

		try {

			SmsQueue.session = session;
//			newMessageCount[0] = newMessageCount[1] = newMessageCount[2] = 0L;
			newMessageCount[0] = newMessageCount[1] = newMessageCount[2] = newMessageCount[3] = 0L; //Airtel Malawi

		} catch (Exception e) {
		//	Logger.sysLog(LogValues.error, SmsQueue.class.getName(), Logger.getStack(e));
		} // End Of TryCatch

	}
	
	public void push(Message msg)
	{
		
	}
}
