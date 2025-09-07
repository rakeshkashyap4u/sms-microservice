package com.rakesh.sms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rakesh.sms.beans.HelloAck;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Controller
public class MonitoringController {

	@RequestMapping(value = "/heartBeat", method = RequestMethod.GET, produces = "text/json")
	public @ResponseBody String heartBeat(@RequestParam("id") String id, @RequestParam("type") String type,
			@RequestParam("event") String event, @RequestParam("subEvent") String subevent,
			@RequestParam("hbTime") String hbTime, @RequestParam("module") String module,
			@RequestParam("queueName") String queueName, @RequestParam("className") String className) {

		HelloAck ack = null;

		if (id.equals("10") && event.equals("9") && subevent.equals("9")) {
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Received Hello Packet [HeartBeat] at " + hbTime);
			ack = new HelloAck(HelloAck.SUCCESS);
			return ack.toString();
		} else {
			Logger.sysLog(LogValues.info, this.getClass().getName(), " Unknown Hello Packet [HeartBeat] Received ");
			ack = new HelloAck(HelloAck.ERROR);
			return ack.toString();
		} // End Of If ELSE

	}// End Of Request Mapper

}// End Of Controller
