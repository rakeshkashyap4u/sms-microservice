package com.rakesh.sms.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.rakesh.sms.beans.AlertServiceDetails;
import com.rakesh.sms.beans.Message;
import com.rakesh.sms.bo.AlertsBo;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.entity.ActiveAlerts;
import com.rakesh.sms.entity.AlertsContent;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.SmsSubscription;
import com.rakesh.sms.scheduler.Promotions;
import com.rakesh.sms.scheduler.StartScheduler;
import com.rakesh.sms.util.CoreEnums;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Controller
public class AlertsController {

	@Autowired
	private AlertsBo subsBo;

	public void setSubsBo(AlertsBo subsBo) {
		this.subsBo = subsBo;
	}

	/**
	 * Used by SubsEngine to inform SMSClient, that the user has subscribed for
	 * specific Alert, like Namaz
	 */
	@RequestMapping(value = "/smsSubscription", method = RequestMethod.GET)
	public @ResponseBody String setSmsSubscriptionDetails(@RequestParam("msisdn") String msisdn,
			@RequestParam("status") String status, @RequestParam("serviceid") String serviceid,
			@RequestParam("language") String language,
			@RequestParam(value = "subserviceid", required = false) String subserviceid,
			@RequestParam(value = "shortcode", required = false) String shortcode,
			@RequestParam(value = "operator", required = false) String operator,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "strip", required = false) Boolean strip,
			@RequestParam(value = "addCC", required = false) Boolean addCC,
			@RequestParam(value = "flag", required = false) String flag,
			@RequestParam(value = "param1", required = false) String param1,
			@RequestParam(value = "param2", required = false) String param2,
			@RequestParam(value = "startingdate", required = false) String startingdate,
			@RequestParam(value = "enddate", required = false) String enddate) {

		/**
		 * http://127.0.0.1:8080/SMSClient/smsSubscription?msisdn=$msisdn$&serviceid=Namaz&subserviceid=$subserviceid$&language=$language$&shortcode=1234&addCC=true
		 */

		if (msisdn != null && msisdn.length() > 0 && status != null && status.length() > 0 && serviceid != null
				&& serviceid.length() > 0) {

			try {

				String codes[] = CoreUtils.getProperty("countryCodes").split(",");
				SmsSubscription user = new SmsSubscription();
				String subStatus = "";
				String cli;

				for (int i = 0; i < CoreEnums.SubscriptionStatus.values.length; i++) {
					subStatus = CoreEnums.SubscriptionStatus.values[i].toString();
					status = status.trim().toUpperCase();
					if (subStatus.equalsIgnoreCase(status))
						user.setStatus(subStatus);
				} // End Of Loop

				if (status != null && status.equalsIgnoreCase("SUB")) {
					user.setStatus("ACTIVE");
				}

				if (strip != null) {

					if (strip.booleanValue() == true)
						msisdn = CoreUtils.stripCodes(msisdn);

				} // End Of Strip IF

				if (addCC != null) {

					if (strip == null || strip.booleanValue() == false) {
						msisdn = CoreUtils.stripCodes(msisdn);
					}

					if (addCC.booleanValue() == true) {
						msisdn = codes[0].concat(msisdn);
					}

				} // End Of AddCountryCode IF

				user.setMsisdn(msisdn.trim());
				user.setServiceid(serviceid.trim());
				user.setLanguage(language.trim());
				if (param1 != null)
					user.setParam1(param1.trim());
				if (param2 != null)
					user.setParam2(param2.trim());

				if (shortcode != null && shortcode.length() > 0)
					user.setShortcode(shortcode.trim());
				else
					user.setShortcode(CoreUtils.getProperty("callerID").trim());

				if (operator != null && operator.length() > 0)
					user.setOperator(operator.trim());
				else
					user.setOperator(CoreUtils.getProperty("operator").trim());

				if (country != null && country.length() > 0)
					user.setCountry(country.trim());
				else
					user.setCountry(CoreUtils.getProperty("country").trim());

				if (subserviceid != null && subserviceid.length() > 0)
					user.setSubserviceid(subserviceid.trim());
				else
					user.setSubserviceid(new String(""));
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				
				if (enddate != null && enddate.length() > 0)
					user.setEnddate(df.parse(enddate.trim()));
				else {
					if(CoreUtils.getProperty("operator").equalsIgnoreCase("ASL") && CoreUtils.getProperty("country").equalsIgnoreCase("IRQ")) {
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Calendar c = Calendar.getInstance();
						
						int days = 0;
						String daysAdd = (CoreUtils.getProperty("enddateDaysAdd"));
						if(daysAdd != null && daysAdd.length() > 0)
						days = Integer.parseInt(daysAdd);
						
						c.add(Calendar.DAY_OF_MONTH, days);
						String newDate = sdf.format(c.getTime());
						Date d = sdf.parse(newDate);
						user.setEnddate(d);
					}
				}
				
				if (flag != null && flag.length() > 0)
					user.setMsgflag(flag.trim());

				if (user.getStatus().equalsIgnoreCase("UNSUB")) 
				{
					this.subsBo.delete(user);
				}
				else 
				{

					SmsSubscription updatedUser = this.subsBo.saveOrUpdate(user);
					
					
					

					if (updatedUser != null)
					{
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								" User Successfully Subscribed :)  " + updatedUser.toString());
						
						
					} 
					else 
					{
						Logger.sysLog(LogValues.info, this.getClass().getName(),
								" User Subscription UNSuccessfully :(  " + updatedUser);
					}

					if (updatedUser != null && updatedUser.getStatus().equalsIgnoreCase("ACTIVE")) 
					{

						ActiveAlerts alert = this.subsBo.getActiveAlert(serviceid, subserviceid);

						if (alert != null) {

							Logger.sysLog(LogValues.info, this.getClass().getName(),
									" Alerts Found for serviceId=" + serviceid);

							cli = alert.getCli();
							if (cli == null || cli.length() == 0)
								cli = CoreUtils.getProperty("callerID");

							String circle = alert.getCircle();
							if (circle == null || circle.length() == 0)
								circle = Pusher.getDefaultCircle();

							if (alert.getType() == CoreEnums.AlertType.COMBO.ordinal()) {

								AlertsContent smsDetails = this.subsBo.getComboMessage(updatedUser);

								if (smsDetails != null) {
									Message msg = new Message(cli, user.getMsisdn(), alert.getPriority(),
											smsDetails.getMsgText(), alert.getProtocol(), CoreEnums.Type.MT.ordinal());
									msg.setCircle(circle);
									msg.setServiceid(smsDetails.getServiceId());

									LanguageSpecification spec = CoreUtils
											.getLanguageSpecifications(alert.getLanguage());
									if (msg.setLanguageSpecifications(spec) == false) {
										msg.setEncoding("true");
									} // End Of Language Check

									Logger.sysLog(LogValues.info, this.getClass().getName(),
											" Alert Type COMBO | SMS Successfully pushed for new Active User :: "
													+ msg.toString());
									CoreUtils.pushAlertToSmsQueue(msg);
								} else {
									Logger.sysLog(LogValues.warn, this.getClass().getName(),
											" Alert Type COMBO | No Alert Content found for User ");
								}

							} // End Of Alert Type Check

						} // End Of Alert Found Check

					} // End Of Active User Check

				} // End Of IF Else

			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
				return "Failure";
			} // End Of Try Catch

		} else 
		{
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Unable to update Subscription for msisdn= " + msisdn + " | status= " + status + " | serviceid= "
							+ serviceid + " | subserviceid= " + subserviceid + " | shortcode= " + shortcode);
			return "Failure";
		}

		return "Success";

	}// End Of Request Mapping

	@RequestMapping(value = "/isSubscribed", method = RequestMethod.GET)
	public @ResponseBody String isSubscribedForAlerts(@RequestParam("msisdn") String msisdn,
			@RequestParam("serviceid") String serviceid,
			@RequestParam(value = "subserviceid", required = false) String subserviceid) {

		if (msisdn != null && serviceid != null && msisdn.length() > 0 && serviceid.length() > 0) {

			boolean subscribed = this.subsBo.isSubscribedForAlerts(msisdn, serviceid, subserviceid);

			if (subscribed) {
				return "Success";
			}

		} else {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " NULL msisdn OR serviceid ");
		}

		return "Failure";

	}// End Of Request Mapping

	@RequestMapping(value = "/updateFlag", method = RequestMethod.GET)
	public @ResponseBody String updateFlag(@RequestParam("msisdn") String msisdn,
			@RequestParam("serviceid") String serviceid, @RequestParam("flag") Integer flag,
			@RequestParam(value = "subserviceid", required = false) String subserviceid) {

		if (msisdn != null && serviceid != null && msisdn.length() > 0 && serviceid.length() > 0) {
			this.subsBo.updateFlag(msisdn, serviceid, subserviceid, String.valueOf(flag));
			return "Success";
		} else {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " updateComboFlag :: NULL Parameters ");
		}

		return "Failure";

	}// End Of Request Mapping

	@RequestMapping(value = "/showPromotions", method = RequestMethod.GET)
	public @ResponseBody String showRunningPromotions() {

		List<Promotions> promos = StartScheduler.getLivePromotions();
		StringBuilder buffer = new StringBuilder("{\"promotions\": [");

		for (int i = 0; i < promos.size(); i++) {
			Promotions promo = promos.get(i);
			if (promo.getId() != -1) {
				String starttime = promo.getStartTime();
				starttime = starttime.substring(0, starttime.length() - 4);
				buffer.append("{\"name\":\"" + promo.getName() + "\", \"pid\":\"" + promo.getId() + "\", \"count\":\""
						+ promo.getPushCount() + "\", \"start\":\"" + starttime + "\", \"paused\":\"" + promo.isPaused()
						+ "\"},");
			}
		} // End Of Loop

		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append("]}");
		promos.clear();
		return buffer.toString();
	}// End Of Request Mapping

	@RequestMapping(value = "/stopPromotion", method = RequestMethod.GET)
	public @ResponseBody String stopThisPromotion(@RequestParam("pid") Integer pid, HttpServletResponse resp) {

		List<Promotions> promos = StartScheduler.getLivePromotions();
		String response = new String(" Promotion not found... Please RELOAD the page and Try again...");

		for (int i = 0; i < promos.size(); i++) {
			Promotions promo = promos.get(i);
			if (promo.getId() == pid.intValue()) {
				response = new String("Promotion STOP Request Initiated...");
				StartScheduler.stopJob(promo);
				break;
			}
		} // End Of Loop

		resp.setHeader("Refresh", "5;url=/SMSClient/");
		promos.clear();
		return response;
	}// End Of Request Mapping

	@RequestMapping(value = "/togglePromotion", method = RequestMethod.GET)
	public @ResponseBody String pauseOrResumePromotion(@RequestParam("pid") Integer pid, HttpServletResponse resp) {

		/**
		 * Toggles Promotion:-- ~~~If Paused then Resumes ~~~If Running then Pauses
		 * Promotion
		 **/

		List<Promotions> promos = StartScheduler.getLivePromotions();
		String response = new String(" Promotion not found... Please RELOAD the page and Try again...");

		for (int i = 0; i < promos.size(); i++) {
			Promotions promo = promos.get(i);
			if (promo.getId() == pid.intValue()) {

				if (promo.isPaused()) {
					response = new String("Promotion RESUMED...");
					promo.resume();
				} else {
					response = new String("Promotion PAUSED...");
					promo.pause();
				}
				break;
			}
		} // End Of Loop

		resp.setHeader("Refresh", "5;url=/SMSClient/");
		promos.clear();
		return response;
	}// End Of Request Mapping

	@RequestMapping(value = "/uploadNamazSubscription", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	public @ResponseBody String transferNamazSubscription(HttpServletResponse response,
			@RequestParam("namazBase[]") MultipartFile file) {

		String status = "Failure";

		try {

			InputStreamReader reader = new InputStreamReader(file.getInputStream());
			BufferedReader buffer = new BufferedReader(reader);

			List<SmsSubscription> base = new ArrayList<SmsSubscription>(1000);

			final String shortcode = CoreUtils.getProperty("callerID");
			final String operator = CoreUtils.getProperty("operator");
			final String country = CoreUtils.getProperty("country");
			final String quote = String.valueOf('"');
			final String comma = String.valueOf(',');
			final String tab = String.valueOf('\t');

			String msisdn, namazId, detail = "";
			String temp[];

			while (detail != null) {

				detail = buffer.readLine();

				if (detail == null || detail.length() == 0) {
					break;
				}
				if (detail.contains(comma) == true) {
					detail = detail.replaceAll(comma, tab).replaceAll(quote, "");
				}

				temp = detail.split(tab);

				if (temp.length > 1 && temp[0] != null && temp[1] != null) {

					msisdn = temp[0].trim();
					namazId = temp[1].trim();

					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" Namaz Base:: [" + msisdn + "] [" + namazId + "]");

					SmsSubscription namazUser = new SmsSubscription();
					namazUser.setSubserviceid(namazId);
					namazUser.setShortcode(shortcode);
					namazUser.setOperator(operator);
					namazUser.setServiceid("Namaz");
					namazUser.setCountry(country);
					namazUser.setStatus("ACTIVE");
					namazUser.setLanguage("_E");
					namazUser.setMsisdn(msisdn);
					base.add(namazUser);
				} else {
					Logger.sysLog(LogValues.warn, this.getClass().getName(),
							" Invalid Namaz Base Entry Ignored :: " + detail);
				} // End Of If ELSE

				status = "Migration Started!! In Progress...";

			} // End Of Loop

			buffer.close();
			reader.close();

			this.subsBo.namazMigration(base);

		} catch (Exception ie) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Namaz Migration --- ERROR Unable to read the uploaded File \n" + Logger.getStack(ie));
		} // End Of Try Catch

		response.setHeader("Refresh", "10;url=/SMSClient/");
		return status;

	}// End Of Request Mapping

	@RequestMapping(value = "/uploadAlertContent", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	public @ResponseBody String uploadAlertContent(HttpServletResponse response,
			@RequestParam("alertcontent[]") MultipartFile file) {
		
		System.out.println("got hit .........................");

		String status = "Failure";

		try {

			InputStreamReader reader = new InputStreamReader(file.getInputStream(), "UTF-8");
			BufferedReader buffer = new BufferedReader(reader);

			List<AlertsContent> contents = new ArrayList<AlertsContent>(1200);

			/* To Remove All ByteObjectMark from the UTF-8 encoded file */
			final String BOM = URLDecoder.decode("%EF%BB%BF", "UTF-8");
			final String tab = String.valueOf('\t');

			String[] cache, temp;
			String detail = "";
			int count = 0;

			temp = new String[15];

			while (detail != null) {

				detail = buffer.readLine();

				if (detail == null || detail.length() == 0) {
					break;
				}

				cache = detail.replaceAll(BOM, "").split(tab);
				count = 0;

				for (int j = 0; j < cache.length; j++) {
					if (cache[j] != null && cache.length > 0) {
						temp[count++] = cache[j].trim();
					}
				} // End Of Loop

				try {
					AlertsContent content = new AlertsContent();

					Integer msgMonth = Integer.parseInt(temp[0]);
					Integer msgDay = Integer.parseInt(temp[1]);
					content.setMsgMonth(msgMonth);
					content.setMsgDay(msgDay);
					content.setServiceId(temp[2]);
					content.setSubServiceId(temp[3]);
					content.setSendingTime(CoreUtils.getTime(temp[4]));
					content.setMsgText(temp[5]);
					content.setLanguage(temp[6]);

					try {
						content.setMsgText(URLEncoder.encode(content.getMsgText(), "UTF-8"));
					} catch (Exception e) {
						Logger.sysLog(LogValues.error, this.getClass().getName(),
								"Unable to encode message in UTF-8: " + Logger.getStack(e));
					}

					if (temp.length > 7) {
						content.setMsgFlag(temp[7]);
					}

					contents.add(content);

				} catch (Exception e) {
					Logger.sysLog(LogValues.error, this.getClass().getName(),
							"\nERROR Uploading Content :: { " + temp[0] + ", " + temp[1] + ", " + temp[2] + ", "
									+ temp[3] + ", " + temp[4] + " } :: Ignored " + Logger.getStack(e));
				} // End Of Try Catch

				status = "Uploading Started!! In Progress...";

			} // End Of Loop

			buffer.close();
			reader.close();

			this.subsBo.uploadAlertContent(contents);

		} catch (Exception ie) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Upload Alert Content --- ERROR Unable to read the uploaded File \n" + Logger.getStack(ie));
		} // End Of Try Catch

		response.setHeader("Refresh", "10;url=/SMSClient/");
		return status;
	}// End Of Request Mapping

	/**
	 * Controller to get all UNIQUE (serviceid, subServiceid) combination for which
	 * the AlertContent has been uploaded
	 **/
	@RequestMapping(value = "/getAlertsWithContents", method = RequestMethod.GET)
	public @ResponseBody String getAlertsWithContents() {

		List<AlertServiceDetails> services = this.subsBo.getServicesWithSMSContent();
		String json = CoreUtils.GSON.toJson(services);

		json = "{\"alerts\":" + json + "}";
		services.clear();
		return json;
	}// End Of Request Mapping

	@RequestMapping(value = "/deleteAlertContent", method = RequestMethod.POST)
	public @ResponseBody String deleteAlertContentWith(
			@RequestParam(value = "service", required = false) String[] services, HttpServletResponse response) {

		List<AlertServiceDetails> list = new ArrayList<AlertServiceDetails>(10);
		final String semiColon = String.valueOf(';');
		String result = "";

		for (int i = 0; services != null && i < services.length; i++) {

			String serviceDetail = services[i];

			if (serviceDetail != null) {
				String[] values = serviceDetail.split(semiColon);

				if (values.length == 1) {
					AlertServiceDetails service = new AlertServiceDetails();
					service.setServiceid(values[0].trim());
					service.setSubserviceid("");
					list.add(service);
				} else if (values.length > 1) {
					AlertServiceDetails service = new AlertServiceDetails();
					service.setServiceid(values[0].trim());
					service.setSubserviceid(values[1]);
					list.add(service);
				}

			} // End Of NULL Check
		} // End Of Loop

		if (list.size() > 0) {
			response.setHeader("Refresh", "3;url=/SMSClient/more/uploadContent.html");
			this.subsBo.deleteAlertContentOf(list);
			result = " Deleting Content... <br/> Please wait, We Will take you back to the UploadContent Page...!!! ";
		} else {
			response.setHeader("Refresh", "1;url=/SMSClient/more/uploadContent.html");
			result = " NO Content to delete...!!! ";
		}

		return result;
	}// End Of Request Mapping

}// End Of Controller
