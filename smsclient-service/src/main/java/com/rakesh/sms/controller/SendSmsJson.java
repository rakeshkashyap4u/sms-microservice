package com.rakesh.sms.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Controller
public class SendSmsJson {
	
	 private final SMSController smsController;

	    public SendSmsJson(SMSController smsController) {
	        this.smsController = smsController;
	    }

	@RequestMapping(value = "/sendSmsJ", method = RequestMethod.GET)
	public @ResponseBody String addSmsToQueueJson(@RequestParam(value = "cli", required = false) String cli,
			@RequestParam("msisdn") String msisdn, @RequestParam(value = "priority", required = false) Integer msgType,
			@RequestParam("content") String content,
			@RequestParam(value = "validate", required = false) Boolean validate,
			@RequestParam(value = "unicode", required = false) Boolean unicode,
			@RequestParam(value = "type", required = false) Integer smsType,
			@RequestParam(value = "callback", required = false) Boolean callback,
			@RequestParam(value = "circle", required = false) String circle,
			@RequestParam(value = "reschedule", required = false) Boolean reschedule,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "dcs", required = false) Integer dataCoding,
			@RequestParam(value = "script", required = false) Integer script,
			@RequestParam(value = "flag", required = false) Integer flag,
			@RequestParam(value = "expiry", required = false) Integer expiry,
			@RequestParam(value = "sync", required = false) Boolean sync,
			@RequestParam(value = "detail", required = false) String extraDetail,
			@RequestParam(value = "serviceid", required = false) String serviceid,
			@RequestParam(value = "multiple", required = false) Boolean multiple,
			@RequestParam(value = "session", required = false) Boolean session,
			@RequestParam(value = "sessionEnd", required = false) Boolean sessionEnd, HttpServletRequest request) throws IOException {

		String response = smsController.addSmsToQueue(cli, msisdn, msgType, content, validate, unicode, smsType,
				callback, circle, reschedule, serviceType, dataCoding, script, flag, expiry, sync, extraDetail,
				serviceid, multiple, session,null, sessionEnd, 0, false,null, null,null,0,request);
		//change due to changes in dream travel service of Mexico 
		String jsonResp = "{\"status\":\"" + (response.equalsIgnoreCase("success") ? "successful" : "failure")
				+ "\",\"reason\":\"\"}";

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Pushing Sms for msisdn : " + msisdn);
		return jsonResp;
	}

}
