package com.rakesh.sms.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rakesh.sms.beans.ConsentDetails;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.bo.UtilityBo;
import com.rakesh.sms.main.JConsole;
import com.rakesh.sms.main.SMPPMessageListener;
import com.bng.sms.queue.SmsQueue;
import com.rakesh.sms.entity.SMSBlacklist;
import com.rakesh.sms.entity.SMSWhitelist;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Controller
public class Utility {

	private UtilityBo myBo;
	private String version, revision;

	public void setVersion(String version) {
		this.version = version;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public UtilityBo getMyBo() {
		return myBo;
	}

	public void setMyBo(UtilityBo myBo) {
		this.myBo = myBo;
	}

	@RequestMapping(value = "/jconsole", method = RequestMethod.GET)
	public @ResponseBody String getContent(HttpServletRequest request, HttpServletResponse response) {

		String result = "Unable To Start";

		try {

			result = JConsole.execute();

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		return result;

	}// End Of Mapping

	@RequestMapping(value = "/reloadProp", method = RequestMethod.GET)
	public @ResponseBody String reloadProperties(HttpServletRequest request, HttpServletResponse response) {

		String result = "Reloading Properties Unsuccessful";

		try {

			this.myBo.loadProperties();
			CoreUtils.reloadBlackoutHours();
			CoreUtils.loadNumberSeries();
			result = "Reloading Properties Successful";

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		return result;

	}// End Of Mapping

	@RequestMapping(value = "/changeLogLevel/{level}")
	public void changeLogLevel(@PathVariable("level") int level, HttpServletRequest request,
			HttpServletResponse response) {
		try {

			Logger.sysLog(LogValues.info, this.getClass().getName(), "Changing Log Level to " + level);
			Logger.setLogLevel(level);

			Logger.sysLog(LogValues.fatal, this.getClass().getName(), " Testing LogLevel Fatal");
			Logger.sysLog(LogValues.error, this.getClass().getName(), " Testing LogLevel Error");
			Logger.sysLog(LogValues.warn, this.getClass().getName(), " Testing LogLevel Warn");
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Testing LogLevel Info");
			Logger.sysLog(LogValues.debug, this.getClass().getName(), " Testing LogLevel Debug");
			Logger.sysLog(LogValues.trace, this.getClass().getName(), " Testing LogLevel Trace");

			response.sendRedirect(request.getContextPath());

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		} // End Of Try Catch

	}// End Of Mapping

	@RequestMapping(value = "/{argument}", method = RequestMethod.GET)
	public @ResponseBody String checkHit(@PathVariable("argument") String argument, HttpServletRequest request,
			HttpServletResponse response) {

		Logger.sysLog(LogValues.info, this.getClass().getName(), " Check HTTP Request::  " + argument);

		if (CoreUtils.withinBlackoutHours(new Date()))
			return " You like peeping in Blackout Hours.... Nice huh!! ;)";
		else
			return " You like peeping...huh!! ;)";

	}// End Of Mapping

	@RequestMapping(value = "/peek", method = RequestMethod.GET)
	public @ResponseBody String receiveSms(@RequestParam(value = "queue", required = false) Integer queue,
			HttpServletRequest request, HttpServletResponse response) {

		Logger.sysLog(LogValues.info, this.getClass().getName(), " Queue Peek Request: smsqueue" + queue);
		Message msg = null;

		if (queue != null && queue.toString().equalsIgnoreCase("null") == false)
			msg = SmsQueue.peek(queue);
		else
			msg = SmsQueue.peek(0);

		if (msg != null)
			return msg.toString();
		else
			return "{}";

	}// End Of Mapping

	@RequestMapping(value = "/getMOUnicode", method = RequestMethod.GET)
	public @ResponseBody String getMOUnicode(@RequestParam(value = "mo") String argument, HttpServletRequest request,
			HttpServletResponse response) {

		try {

			if (argument != null && argument.length() > 0) {

				argument = argument.replaceAll("\n", "").trim();
				argument = URLEncoder.encode(argument, "UTF-16BE").replaceAll("%00", "");
				argument = argument.replaceAll("%", "\\u");

			} else
				argument = "ERROR";

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, SMPPMessageListener.class.getName(),
					" Invalid Unicode String :: " + e.getMessage());
			argument = "ERROR";
		}

		return argument;

	}// End Of Mapping

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public @ResponseBody String printVersion() {
		return this.version + "." + this.revision;
	}// End Of Mapping

	@RequestMapping(value = "/addBlacklistUser", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	public @ResponseBody String addBlacklistUser(@RequestParam("series") Boolean series,
			@RequestParam("ntype") Boolean numberType, @RequestParam("baseType") String baseType,
			@RequestParam(value = "inputMsisdn", required = false) String inputMsisdn,
			@RequestParam(value = "baseFile[]", required = false) MultipartFile file, HttpServletRequest request,
			HttpServletResponse resp) {

		int msisdnLength = Integer.parseInt(CoreUtils.getProperty("msisdnLength"));
		List<String> baseList = new ArrayList<String>();
		boolean isInternational = false;
		boolean response = false;
		String temp = null;
		String reason = "";

		if (numberType != null && numberType)
			isInternational = true;

		int isSeries = 0;

		if (series != null && series)
			isSeries = 1;

		try {

			if (file != null && file.isEmpty() == false && baseType.equalsIgnoreCase("upload")) {
				/* Msisdn Base uploaded using a file */
				Logger.sysLog(LogValues.info, this.getClass().getName(), " Upload File Details::  Name="
						+ file.getOriginalFilename() + " | Format=" + file.getContentType());

				try {
					InputStreamReader reader = new InputStreamReader(file.getInputStream());
					BufferedReader buffer = new BufferedReader(reader);

					String msisdn = null;

					do {

						msisdn = buffer.readLine();

						if (msisdn != null && msisdn.length() > 0) {
							temp = msisdn.replaceAll(",", "");
							if (temp != null && temp.length() > 0) {
								if (isInternational) {
									if (temp.startsWith("+") || temp.startsWith("00"))
										baseList.add(temp);
									else
										baseList.add("+" + temp);
								} else {
									if (isSeries == 1)
										baseList.add(temp);
									else {
										temp = CoreUtils.stripCodes(temp);
										if (temp != null && temp.length() == msisdnLength)
											baseList.add(temp);
										else
											Logger.sysLog(LogValues.warn, this.getClass().getName(), " Msisdn " + msisdn
													+ " Ignored from Promotion base |  Reason= ValidationFailure ");
									}
								}
							} else {
								Logger.sysLog(LogValues.warn, this.getClass().getName(), " Msisdn " + msisdn
										+ " Ignored from Promotion base |  Reason= ValidationFailure ");
							}
						} // End Of Validation

					} while (msisdn != null);

					buffer.close();
					reader.close();

				} catch (Exception ie) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							" ERROR Unable to read the uploaded Base File \n" + Logger.getStack(ie));
					reason = "FileUploadError";
				} // End Of Try Catch

			} else if (inputMsisdn != null && inputMsisdn.length() > 0 && baseType.equalsIgnoreCase("category")) {
				/* Comma Separated Msisdn Input in Textbox */
				String[] numbers = inputMsisdn.split(",");

				for (int i = 0; i < numbers.length; i++) {

					if (numbers[i] != null) {
						if (isInternational) {
							if (numbers[i].startsWith("+") || numbers[i].startsWith("00"))
								baseList.add(numbers[i]);
							else
								baseList.add("+" + numbers[i]);
						} else {
							if (isSeries == 1)
								baseList.add(numbers[i]);
							else {
								temp = CoreUtils.stripCodes(numbers[i]);
								if (temp != null && temp.length() == msisdnLength)
									baseList.add(temp);
								else
									Logger.sysLog(LogValues.warn, this.getClass().getName(), " Msisdn " + numbers[i]
											+ " Ignored from Promotion base |  Reason= ValidationFailure ");
							}
						}
					} else {
						Logger.sysLog(LogValues.warn, this.getClass().getName(),
								" Msisdn " + numbers[i] + " Ignored from Promotion base |  Reason= ValidationFailure ");
					} // End Of Validation
				} // End Of Loop
			} else {
				reason = "BaseNotFound";
			} // End Of Base Type Check
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		if (baseList.size() > 0 && reason.length() == 0) {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					"Adding blacklist user(s) with list size:" + baseList.size());
			int status = myBo.addBlacklistUsers(baseList, isSeries);
			if (status > 0)
				response = true;

		} else {
			reason = "ZeroBaseSize";
		} // End Of Base

		resp.setHeader("Refresh", "3;url=more/smsblacklist.html");
		if (response == true) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), "Added blacklist user(s)");
			return "Success";
		} else {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					"Unable to add blacklist user(s). Reason: " + reason);
			return "Failure " + reason;
		}

	}// End Of Mapping

	@RequestMapping(value = "/addWhitelistUser", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	public @ResponseBody String addWhitelistUser(@RequestParam("shortcode") String shortcode,
			@RequestParam("baseType") String baseType,
			@RequestParam(value = "inputMsisdn", required = false) String inputMsisdn,
			@RequestParam(value = "baseFile[]", required = false) MultipartFile file, HttpServletRequest request,
			HttpServletResponse resp) {

		int msisdnLength = Integer.parseInt(CoreUtils.getProperty("msisdnLength"));
		List<String> baseList = new ArrayList<String>();
		boolean response = false;
		String temp = null;
		String reason = "";

		try {

			if (file != null && file.isEmpty() == false && baseType.equalsIgnoreCase("upload")) {
				/* Msisdn Base uploaded using a file */
				Logger.sysLog(LogValues.info, this.getClass().getName(), " Upload File Details::  Name="
						+ file.getOriginalFilename() + " | Format=" + file.getContentType());

				try {
					InputStreamReader reader = new InputStreamReader(file.getInputStream());
					BufferedReader buffer = new BufferedReader(reader);

					String msisdn = null;

					do {

						msisdn = buffer.readLine();

						if (msisdn != null && msisdn.length() > 0) {
							temp = msisdn.replaceAll(",", "");
							temp = CoreUtils.stripCodes(temp);
							if (temp != null && temp.length() == msisdnLength)
								baseList.add(temp);
							else
								Logger.sysLog(LogValues.warn, this.getClass().getName(), " Msisdn " + msisdn
										+ " Ignored from Promotion base |  Reason= ValidationFailure ");
						} else {
							Logger.sysLog(LogValues.warn, this.getClass().getName(),
									" Msisdn " + msisdn + " Ignored from Promotion base |  Reason= ValidationFailure ");
						} // End Of Validation

					} while (msisdn != null);

					buffer.close();
					reader.close();

				} catch (Exception ie) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							" ERROR Unable to read the uploaded Base File \n" + Logger.getStack(ie));
					reason = "FileUploadError";
				} // End Of Try Catch

			} else if (inputMsisdn != null && inputMsisdn.length() > 0 && baseType.equalsIgnoreCase("category")) {
				/* Comma Separated Msisdn Input in Textbox */
				String[] numbers = inputMsisdn.split(",");

				for (int i = 0; i < numbers.length; i++) {

					if (numbers[i] != null) {
						temp = CoreUtils.stripCodes(numbers[i]);
						if (temp != null && temp.length() == msisdnLength)
							baseList.add(temp);
						else
							Logger.sysLog(LogValues.warn, this.getClass().getName(), " Msisdn " + numbers[i]
									+ " Ignored from Promotion base |  Reason= ValidationFailure ");
					} else {
						Logger.sysLog(LogValues.warn, this.getClass().getName(),
								" Msisdn " + numbers[i] + " Ignored from Promotion base |  Reason= ValidationFailure ");
					} // End Of Validation
				} // End Of Loop
			} else {
				reason = "BaseNotFound";
			} // End Of Base Type Check
		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
		}

		if (baseList.size() > 0 && reason.length() == 0) {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					"Adding whitelist user(s) with list size:" + baseList.size());
			int status = myBo.addWhitelistUsers(baseList, shortcode);
			if (status > 0)
				response = true;

		} else {
			reason = "ZeroBaseSize";
		} // End Of Base

		resp.setHeader("Refresh", "3;url=more/smswhitelist.html");
		if (response == true) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), "Added whitelist user(s)");
			return "Success";
		} else {
			Logger.sysLog(LogValues.info, this.getClass().getName(),
					"Unable to add whitelist user(s). Reason: " + reason);
			return "Failure " + reason;
		}

	}// End Of Mapping

	@RequestMapping(value = "/isBlacklisted/{msisdn}", method = RequestMethod.GET)
	public @ResponseBody String isBlacklistedUser(@PathVariable("msisdn") String msisdn) {
		return String.valueOf(CoreUtils.isBlacklisted(msisdn));
	}// End Of Mapping

	@RequestMapping(value = "/getBlacklistedNumbersAsJson", method = RequestMethod.GET)
	public @ResponseBody String getBlacklistedNumbersAsJson() {
		List<SMSBlacklist> list = this.myBo.getBlacklisted();
		String json = "{\"blacklist\": " + CoreUtils.GSON.toJson(list) + "}";
		return json;
	}// End Of Mapping

	@RequestMapping(value = "/getWhitelistedNumbersAsJson", method = RequestMethod.GET)
	public @ResponseBody String getWhitelistedNumbersAsJson() {
		List<SMSWhitelist> list = this.myBo.getWhitelisted();
		String json = "{\"whitelist\": " + CoreUtils.GSON.toJson(list) + "}";
		return json;
	}// End Of Mapping

	@RequestMapping(value = "/getRevenueInfo", method = RequestMethod.GET)
	public @ResponseBody String getRevenueInfo(@RequestParam(value = "keyword") String keyword,
			@RequestParam(value = "shortcode") String shortcode) {
		return this.myBo.getRevenueInfo(keyword, shortcode);
	}// End Of Mapping

	/********************************************************************************************************/
	/*******************************************
	 * Test Controllers
	 *******************************************/
	/********************************************************************************************************/

	@RequestMapping(value = "/testController", method = RequestMethod.GET)
	public @ResponseBody String testController() {

		this.isBlacklistedUser("9971314566");
		this.isBlacklistedUser("919799922957");
		this.isBlacklistedUser("9799922958");
		this.isBlacklistedUser("09928820163");
		this.isBlacklistedUser("08971314566");
		this.isBlacklistedUser("8771314566");
		this.isBlacklistedUser("919421314566");
		this.isBlacklistedUser("919431314566");

		this.isBlacklistedUser("+25471836520");
		this.isBlacklistedUser("+25471837810");
		this.isBlacklistedUser("+25472836520");
		this.isBlacklistedUser("919456123780");
		this.isBlacklistedUser("09426123780");

		return "BlackList Check...";

	}// End Of Mapping

	@RequestMapping(value = "/getEndDate", method = RequestMethod.GET)
	public @ResponseBody String getSubscriptionEndDate() {
		return "28/03/2016 19:37 , renewal";
	}// End Of Mapping

	@RequestMapping(value = "/serviceList", method = RequestMethod.GET)
	public @ResponseBody String getServiceList(@RequestParam(value = "services", required = false) String[] services) {

		StringBuilder list = new StringBuilder("");

		if (services == null || services.length == 0) {
			services = new String[] { "Christianity=christianity", "Islamic=islamic", "MagicVoice=magicvoice" };
		}

		for (int i = 0; i < services.length; i++) {
			list.append(services[i]);
			list.append(',');
		} // End of Loop

		if (list.length() > 0) {
			list.deleteCharAt(list.length() - 1);
		}
		return list.toString();
	}// End Of Mapping

	@RequestMapping(value = "/getUSSDJson", method = RequestMethod.GET)
	public @ResponseBody String getUSSDJson() {
		ConsentDetails consent = new ConsentDetails();
		consent.setUrl("http://---");
		consent.setResponseType("ServiceList");
		consent.setTimeoutInSec(600L);
		consent.setSmsResponseUrl("http://---");
		consent.setSuccessActionUrl("http://---");
		consent.setFailureActionUrl("http://---");

		return consent.toString();
	}// End Of Mapping

}// End Of Controller Class
