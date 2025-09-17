package com.rakesh.sms.controller;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jsmpp.session.SMPPSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rakesh.sms.beans.SMSC;
import com.rakesh.sms.bo.GatewayBo;
import com.rakesh.sms.main.ESME;
import com.rakesh.sms.main.Pusher;
import com.rakesh.sms.main.ReConnector;
import com.rakesh.sms.queue.QueueManager;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Controller
public class SmscController {

	private static final Long START_TIMEOUT = 1200L, WAIT_TIMEOUT = 15000L;
	private static List<SMSCConfigs> CircleConfigs;
	private static GatewayBo smscGatewayBo;
	private static List<String> Circles;
	

	public SmscController() {
		//SmscController.smscGatewayBo = smscGatewayBo.getGatewayBoImpl();
		SmscController.CircleConfigs = null;
		SmscController.Circles = null;
	}// End Of Constructor

	@RequestMapping(value = "/isAlive", method = RequestMethod.GET)
	public @ResponseBody String isSmscConnectionAlive(@RequestParam(value = "circle", required = false) String circle) {

		ConcurrentHashMap<String, SMSC> smscList = QueueManager.smscList;
		String result = "Failure";

		if (circle == null || circle.length() == 0)
			circle = Pusher.getDefaultCircle();
		else
			circle = circle.trim().toUpperCase();

		SMSC smsc = smscList.get(circle);

		if (smsc != null) {

			ESME esme = smsc.getSmppGateway();

			if (esme != null) {
				SMPPSession esmeSession = esme.getSmppSession();

				if (esmeSession.getSessionState().isBound())
					result = "Success";
				
				
			} else
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" [" + circle + "] SMSC Session isAlive Request | No SMSC Session found [1] ");

		} else
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" [" + circle + "] SMSC Session isAlive Request | No SMSC Session found [2] ");

		return result;
	}// End Of Request Mapping

	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public @ResponseBody String connectToSmsc(@RequestParam(value = "circle", required = false) String circle) {

		ConcurrentHashMap<String, SMSC> smscList = QueueManager.smscList;
		StringBuffer response = new StringBuffer("");
		List<String> circles;

		if (circle != null && circle.length() > 0) {

			circle = circle.toUpperCase().trim();

			Logger.sysLog(LogValues.info, this.getClass().getName(), " SMSC Circle: " + circle + "!");

			if (smscList.containsKey(circle) == false) {
				try {
					ReConnector connection = new ReConnector(circle);
					connection.safeStart();
					try {
						response.append(" Connection Initiated...<br/>");
						connection.join(SmscController.START_TIMEOUT);
					} catch (Exception e) {
						response.append(" Connection Failed!!!<br/>");
					} // End Of Try Catch

					if (connection.isAlive() == false) {
						try {
							connection.join(SmscController.WAIT_TIMEOUT);

							if (!connection.isAlive()) {
								response.append(" Connection Successfull!!!<br/>");
							} else {
								connection.interrupt();
								connection.join();
							}

						} catch (Exception e) {
							response.append(" Connection Timeout!!!<br/>");
						} // End Of Try Catch
					} else
						response.append(" Connection Successfull!!!<br/>");
				} catch (Exception e) {
					response.append(e.getMessage() + "<br/>");
				} // End Of Try Catch
			} else {

				String result = this.isSmscConnectionAlive(circle);

				if (result.equalsIgnoreCase("Success")) {
					response.append(" Already Connected!!!<br/>");
				} else {
					smscList.remove(circle);  
					return this.connectToSmsc(circle);
				}
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" SMSC Session for this Circle " + circle + " already connected  ||  Check isAlive ");

			} // End Of IF ELSE

		} else {

			circles = SmscController.smscGatewayBo.getAllCircleNames();

			for (int i = 0; i < circles.size(); i++) {

				circle = circles.get(i);

				if (smscList.containsKey(circle) == false) {
					try {
						ReConnector connection = new ReConnector(circle);
						connection.safeStart();
						try {
							response.append(" Circle " + circle + " Connection Initiated...<br/>");
							connection.join(SmscController.START_TIMEOUT);
						} catch (Exception e) {
							response.append(" Circle " + circle + " Connection Failed!!!<br/>");
						} // End Of Try Catch

						if (connection.isAlive() == false) {
							try {
								connection.join(SmscController.WAIT_TIMEOUT);

								if (!connection.isAlive()) {
									response.append(" Circle " + circle + " Connection Successful!!!<br/>");
								} else {
									connection.interrupt();
									connection.join();
								}

							} catch (Exception e) {
								response.append(" Circle " + circle + " Connection Timed Out!!!<br/>");
							} // End Of Try Catch
						} else
							response.append(" Circle " + circle + " Connection Successful!!!<br/>");
					} catch (Exception e) {
						response.append(e.getMessage() + "<br/>");
					} // End Of Try Catch
				} else {

					String result = this.isSmscConnectionAlive(circle);

					if (result.equalsIgnoreCase("Success")) {
						response.append(" Circle " + circle + " Already Connected <br/>");
					} else {
						smscList.remove(circle);	
						response.append(this.connectToSmsc(circle));
					}
					Logger.sysLog(LogValues.info, this.getClass().getName(),
							" SMSC Session for Circle " + circle + " already connected  ||  Ignored ");
				} // End Of IF ELSE

			} // End Of Loop

		} // End Of IF Else

		return response.toString();

	}// End Of Request Mapping

	@RequestMapping(value = "/disconnect", method = RequestMethod.GET)
	public @ResponseBody String unbindSmscConnection(@RequestParam(value = "circle", required = false) String circle,
			@RequestParam(value = "reason", required = false) String reason) {

		ConcurrentHashMap<String, SMSC> smscList = QueueManager.smscList;
		String result = "Failure";

		if (circle == null || circle.length() == 0)
			circle = Pusher.getDefaultCircle();
		else
			circle = circle.trim().toUpperCase();

		SMSC smsc = smscList.get(circle);

		if (smsc != null) {

			smsc.shutdown();
			smscList.remove(circle);
			result = "Success";

			if (reason != null && reason.length() > 0) {
				Logger.sysLog(LogValues.info, this.getClass().getName(),
						" SMSC Unbind Session request Successfull for Circle: " + circle + " | Reason: " + reason);
			} else
				Logger.sysLog(LogValues.warn, this.getClass().getName(),
						" SMSC Unbind Session request Successfull for Circle: " + circle + " WITHOUT ANY Reason ");

		} else
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" SMSC Unbind Session Request | No SMSC Session found [2] ");

		return result;

	}// End Of Request Mapping

	@RequestMapping(value = "/pause", method = RequestMethod.GET)
	public @ResponseBody String pauseClient(@RequestParam(value = "reason", required = false) String reason) {

		if (reason != null && reason.length() > 0) {
			Logger.sysLog(LogValues.warn, this.getClass().getName(), " SMS Client PAUSE request | Reason= " + reason);
		} else {
			Logger.sysLog(LogValues.error, this.getClass().getName(), " SMS Client PAUSE request WITHOUT REASON ");
		}

		ReConnector.killAll();
		QueueManager.pauseManager();
		Pusher.closeAllActiveSMSCConnections();

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}

		return "Success";

	}// End Of Request Mapping

	@RequestMapping(value = "/resume", method = RequestMethod.GET)
	public @ResponseBody String resumeClient() {

		QueueManager.resumeManager();

		try {
			Thread.sleep(500);
		} catch (Exception e) {
		}

		return "Success";

	}// End Of Request Mapping

	@RequestMapping(value = "/fetchCirclesAsJson", method = RequestMethod.GET)
	public @ResponseBody String fetchCirclesAsJson() {

		if (SmscController.Circles == null) {
			SmscController.Circles = SmscController.smscGatewayBo.getTXCircleNames();
		}

		StringBuffer buffer = new StringBuffer("{\"circles\": [");

		for (int i = 0; i < Circles.size(); i++) {
			buffer.append("{\"name\":\"" + Circles.get(i) + "\"},");
		}

		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append("]}");
		return buffer.toString();

	}// End Of Request Mapping

	@RequestMapping(value = "/getConnectionsAsJson", method = RequestMethod.GET)
	public @ResponseBody String getConnectionsAsJson() {

		if (SmscController.CircleConfigs == null) 
		{
			SmscController.CircleConfigs = SmscController.smscGatewayBo.getAllCircleDetails();
		}
		
	
		
		

		StringBuffer buffer = new StringBuffer("{\"circles\": [");

		for (int i = 0; i < CircleConfigs.size(); i++) {
			SMSCConfigs config = CircleConfigs.get(i);
			
			if(config.getServiceUri()== null || config.getServiceUri().equals("") )
			{
				buffer.append("{\"name\":\"" + config.getCircle() + "\", \"detail\":\"" + config.getServerIp() + ":"
						+ config.getServerPort() + "\", \"status\":\"" + "Success" + "\"},");
			}
			else
			{
			String status = this.isSmscConnectionAlive(config.getCircle());
			buffer.append("{\"name\":\"" + config.getCircle() + "\", \"detail\":\"" + config.getServerIp() + ":"
					+ config.getServerPort() + "\", \"status\":\"" + status + "\"},");
			
			}
		} // End Of Loop

		buffer.deleteCharAt(buffer.length() - 1);
		buffer.append("]}");
		return buffer.toString();

	}// End Of Request Mapping

}// End Of Controller
