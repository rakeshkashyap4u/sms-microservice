package com.rakesh.sms.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContext implements ApplicationContextAware {

	@Autowired
	private static ApplicationContext context;

	public ApplicationContext getApplicationContext() {
		return AppContext.context;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AppContext.context = applicationContext;
	}

	synchronized public static Object getBean(String beanName) {

		try {

			if (AppContext.context != null)
				return AppContext.context.getBean(beanName);
			else
				Logger.sysLog(LogValues.error, AppContext.class.getName(),
						" ApplicationContext Initialization Failed | getBean() Failed ");

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, AppContext.class.getName(),
					" Error getting Bean " + beanName + " \n " + Logger.getStack(e));
		}

		return null;

	}// End Of Method

}// End Of Class
