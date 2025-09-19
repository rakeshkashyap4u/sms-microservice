package com.rakesh.sms.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rakesh.sms.entity.Country;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCDetails;
import com.rakesh.sms.entity.SMSCFormats;
import com.rakesh.sms.entity.Operator;
import com.rakesh.sms.util.All;
import com.rakesh.sms.bo.CountryBo;
import com.rakesh.sms.bo.OperatorBo;
import com.rakesh.sms.bo.SMSCDetailsBo;
import com.rakesh.sms.bo.SMSCConfigurationBo;

@Controller
public class ConfigController {

	@Autowired
	SMSCDetailsBo detail;

	public void setDetails(SMSCDetailsBo details) {
		this.detail = details;
	}
	
	

	@Autowired
	SMSCConfigurationBo configuration;

	@Autowired
	OperatorBo service;

	@Autowired
	CountryBo country;

	@RequestMapping(value = "/getSmscDetails", method = RequestMethod.GET)
	public ModelAndView getDetails() {

		return new ModelAndView("all", "All", new All());
	}

	@RequestMapping(value = "/saveSmscConfig", method = RequestMethod.POST, headers = "Accept=application/json")
	public String saveConfigDetails(@ModelAttribute("config") SMSCConfigs config) {

		Integer cid = configuration.saveOrUpdate(config);

		return "NewFile";

	}

	@RequestMapping(value = "/getSmscConfig", method = RequestMethod.GET)
	public ModelAndView getconfigDetails() {
		ModelAndView obj = new ModelAndView("smscconfig");
		return obj;
	}

	@RequestMapping(value = "/updateSmscConfig", method = RequestMethod.POST)
	public String method(@ModelAttribute("config") SMSCConfigs config) {

		int cid = configuration.saveOrUpdate(config);

		return "NewFile";
	}

	@RequestMapping(value = "/add")
	public String getOperatorNames(Map<String, Object> operator) {
		List<Operator> operatorlist = service.getAllOperators();
		for (Operator obj : operatorlist) {
		}

		operator.put("operators", operatorlist);
		operator.put("op", new Operator());

		return "drop";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveAllDetails(@ModelAttribute("All") All all, HttpServletResponse resp)
			throws SQLIntegrityConstraintViolationException {
		String opId;
		boolean circleExists = false;
		SMSCConfigs config = new SMSCConfigs(all.getCircle(), all.getServerIp(), all.getServerPort(),
				all.getServiceUri(), all.getUserid(), all.getPassword(), all.getBindMode());
		SMSCDetails details = new SMSCDetails(all.getOperator(), all.getCountry(), all.getProtocol());
		/*
		 * SMSCFormats formats= new
		 * SMSCFormats(all.getCircle(),all.getRequestFormat(),all.getResponseFormat(),
		 * all.getMode());
		 */

		List<SMSCConfigs> configlist = configuration.getAllConfiguration();

		if (configlist.size() == 0) {
			opId = detail.saveOrUpdate(details);
			config.setOpId(opId);
			Integer cid = configuration.saveOrUpdate(config);
			/* formats.setCid(cid.toString()); */

		} else {
			for (SMSCConfigs configObj : configlist) {
				if (configObj.getCircle().equals(all.getCircle())) {
					circleExists = true;
				}
			}
			if (!(circleExists)) {
				opId = detail.saveOrUpdate(details);
				config.setOpId(opId);
				configuration.saveOrUpdate(config);
			}

		}
		resp.setHeader("Refresh", "1;url=./config");

	}

	@RequestMapping(value = "/config")
	public String displayConfiguration(ModelMap model) {
		List<SMSCConfigs> configlist = configuration.getAllConfiguration();
		List<SMSCDetails> detaillist = detail.getSMSCDetails();
		if (configlist != null && configlist.size() > 0) {
			model.addAttribute("Configuration", configlist);
		}
		if (detaillist != null && detaillist.size() > 0) {
			model.addAttribute("detaillist", detaillist);
		}
		return "configuration";
	}// End Of Mapping

	@RequestMapping(value = "/ConfigInfo", method = RequestMethod.GET)
	public String editConfiguration(HttpServletRequest request, ModelMap map) {
		List<SMSCConfigs> configlist = configuration.getAllConfiguration();
		List<SMSCDetails> detaillist = detail.getSMSCDetails();
		if (configlist != null && configlist.size() > 0) {

			map.addAttribute("Configuration", configlist);
			map.addAttribute("Details", detaillist);
		}
		map.addAttribute("circlename", request.getParameter("circle"));

		map.addAttribute("all", new All());
		return "editConfiguration";
	}

	@ModelAttribute("countryList")
	public List getCountry() {
		List<Country> countryList = country.getAllCountry();
		return countryList;
	}

	@ModelAttribute("operatorList")
	public List getOperator() {
		List<Operator> operatorList = service.getAllOperators();

		return operatorList;
	}

	@ModelAttribute("protocolList")
	public List getProtocol() {
		List protocolList = new ArrayList();
		protocolList.add("SMPP");
		protocolList.add("HTTP");
		protocolList.add("SOAP");
		return protocolList;
	}

	@ModelAttribute("BindMode")
	public List getBindMode() {
		List BindMode = new ArrayList();
		BindMode.add("0");
		BindMode.add("1");
		BindMode.add("2");
		return BindMode;
	}

	@RequestMapping(value = "/updateConfig", method = RequestMethod.POST)
	public void updateConfigDetails(@ModelAttribute("All") All result, HttpServletResponse resp) {
		int configstatus = 0;
		String opId = null;

		List<SMSCConfigs> configlist = configuration.getAllConfiguration();
		List<SMSCDetails> detaillist = detail.getSMSCDetails();


		for (SMSCDetails detailObj : detaillist) {
			if (result.getOpId() == (Integer.parseInt(detailObj.getId()))) {
				detailObj.setCountry(result.getCountry());
				detailObj.setOperator(result.getOperator());
				detailObj.setProtocol(result.getProtocol());
				detail.editDetails(detailObj);

			}
		}

		for (SMSCConfigs configObj : configlist) {
			if (result.getOpId() == (Integer.parseInt(configObj.getOpId()))) {
				configObj.setUserid(result.getUserid());
				configObj.setBindMode(result.getBindMode());
				configObj.setCircle(result.getCircle());
				configObj.setPassword(result.getPassword());
				configObj.setServiceUri(result.getServiceUri());
				configObj.setServerPort(result.getServerPort());
				configObj.setServerIp(result.getServerIp());
				configuration.saveOrUpdate(configObj);
			}

		}

		resp.setHeader("Refresh", "1;url=./config");

	}

}
