package com.rakesh.sms.util;

import java.util.Properties;

import com.rakesh.sms.beans.Message;
import com.rakesh.sms.controller.SMSController;
import com.rakesh.sms.entity.LanguageSpecification;

/**
 * Language Dependent Settings.
 */
public class OptionalParameters {

	public static boolean SET(Message sms, String language) {

		try {

			if (language != null && language.length() > 0) {
				
				if(CoreUtils.getProperty("country").equals("AFG") && CoreUtils.getProperty("operator").equals("MTN")
						&& CoreUtils.getProperty("protocol").equals("SMPP")) {
					if(sms.getMsgid() != null && sms.getMsgid().length() > 0)
					language = sms.getMsgid();
				}

				LanguageSpecification specs = CoreUtils.getLanguageSpecifications(language.toUpperCase());

				if (specs != null) {
					Logger.sysLog(LogValues.info, OptionalParameters.class.getName(),
							" Found optional settings for Language :: " + specs.toString());
					sms.setLanguageSpecifications(specs);

					int script = specs.getScript();
					String message = SMSController.convertTexttoUnicode(sms.getMessage(), script);
					sms.setMessage(message);

					return true;
				} else {
					Logger.sysLog(LogValues.info, OptionalParameters.class.getName(),
							" No Optional Configuration found for Language ");
				}

			} // End Of Language Check

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, OptionalParameters.class.getName(), Logger.getStack(e));
		} // End Of Try Catch

		return false;
	}// End Of Method

	public static boolean SET(Message sms, Properties props) {
		// TODO
		return false;
	}// End Of Method

}// End Of Class
