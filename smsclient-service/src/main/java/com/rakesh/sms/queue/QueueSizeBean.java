package com.rakesh.sms.queue;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class QueueSizeBean {

	private static MBeanServerConnection mBeanServerConnection;

	public void setMBeanServerConnection(MBeanServerConnection mBeanServerConnection) {
		Logger.sysLog(LogValues.info, this.getClass().getName(),
				" MBean Server Configuration Successfull | Session Established | " + mBeanServerConnection);
		QueueSizeBean.mBeanServerConnection = mBeanServerConnection;
	}

	protected int getSize(String queueName) {

		long queueSize = 0;

		try {

			String url = "localhost:type=Broker,brokerName=localhost,destinationType=Queue,destinationName="
					+ queueName;
			ObjectName objectNameRequest = new ObjectName(url);

			queueSize = (Long) mBeanServerConnection.getAttribute(objectNameRequest, "QueueSize");

			Logger.sysLog(LogValues.info, this.getClass().getName(), "server connection " + mBeanServerConnection);

			Logger.sysLog(LogValues.info, this.getClass().getName(),
					"RMI MBean Details :: QueueName= " + queueName + "  |  #PendingMessages: " + queueSize);

		} catch (InstanceNotFoundException infe) {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					" Queue Instance Not Found | Unable to create MBean |  QueueName= " + queueName
							+ "  |  Returning 0 " + Logger.getStack(infe));
			queueSize = 0;
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " getQueueSize--- \n" + Logger.getStack(e));
		}

		return (int) queueSize;

	}// End Of Method

}
