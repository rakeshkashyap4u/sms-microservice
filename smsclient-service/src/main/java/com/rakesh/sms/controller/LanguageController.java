package com.rakesh.sms.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestBody;


import com.rakesh.sms.dao.LanguageDao;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.util.LanguageUtility;


@RestController
public class LanguageController {

	@Autowired
	LanguageDao languagedao;

	@Autowired
	LanguageUtility util;

	public void setUtility(LanguageUtility utility) {
		this.util = utility;
	}

	public void setLanguagedao(LanguageDao languagedao) {
		this.languagedao = languagedao;
	}

	@RequestMapping(value = "/newLanguage")
	public ModelAndView addNewSMSLanguage(ModelMap map) {
		List<LanguageSpecification> languageList = util.getLanguages();

		if (languageList != null && languageList.size() > 0) {
			map.addAttribute("LanguageList", languageList);
		}
		return new ModelAndView("newLanguage", "language", new LanguageSpecification());
		// return "newLanguage";
	}

	/*
	 * @RequestMapping(value = "/addLanguage", method = RequestMethod.POST) public
	 * void addService(
	 * 
	 * @RequestParam("languageName") String languageName,
	 * 
	 * @RequestParam("dataCoding") int dataCoding,
	 * 
	 * @RequestParam("encoding" ) String encoding,
	 * 
	 * @RequestParam("serviceType") String serviceType,
	 * 
	 * @RequestParam("script") int script, ModelMap model, HttpServletResponse resp)
	 * { System.out.print("In Controller");
	 * 
	 * System.out.print("Recieved" + languageName + dataCoding + encoding +
	 * serviceType + script); List<LanguageSpecification> languageList =
	 * util.getLanguages(); LanguageSpecification language = new
	 * LanguageSpecification(); language.setLanguage(languageName);
	 * language.setDataCoding(dataCoding); language.setEncoding(encoding);
	 * language.setScript(script); language.setServiceType(serviceType);
	 * 
	 * int status = util.addLanguage(language); System.out.print(status);
	 * 
	 * 
	 * 
	 * if(languageList != null && languageList.size() > 0) {
	 * model.addAttribute("LanguageList",languageList); } resp.setHeader("Refresh",
	 * "1;url=./languages");
	 * 
	 * }
	 */

	@RequestMapping(value = "/languages")
	public List<LanguageSpecification>  displayLanguages(ModelMap model) {

		List<LanguageSpecification> languagelist = util.getLanguages();

		if (languagelist != null && languagelist.size() > 0)
			model.addAttribute("languageList", languagelist);

		return languagelist;
	}// End Of Mapping

	@RequestMapping(value = "/languageInfo", method = RequestMethod.GET)
	public String languageInfo(HttpServletRequest request, ModelMap model, HttpServletResponse resp) {

		// System.out.println("in service info");
		List<LanguageSpecification> languageList = util.getLanguages();

		for (LanguageSpecification obj : languageList)

		if (languageList != null && languageList.size() > 0)
			model.addAttribute("LanguageList", languageList);

		if (request.getParameter("languagename") != null) {
			model.addAttribute("languagename", request.getParameter("languagename"));
			// System.out.println(request.getParameter("servicename"));
		}
		return "LanguageInfo";

	}

	@RequestMapping(value = "/editLanguage", method = RequestMethod.POST)
	public void editService(@RequestParam(value = "lid") int id,
			@RequestParam(value = "languageName") String languageName,
			@RequestParam(value = "dataCoding") Integer dataCoding,
			@RequestParam(value = "serviceType", required = false) String serviceType,
			@RequestParam(value = "encoding") String encoding, @RequestParam(value = "script") int script,
			HttpServletRequest request, HttpServletResponse resp) {

		List<LanguageSpecification> languageList = util.getLanguages();

		for (LanguageSpecification language : languageList) {

			if (language.getLid().equals(id)) {
				language.setLanguage(languageName);
				language.setDataCoding(dataCoding);
				language.setEncoding(encoding);
				language.setScript(script);
				language.setServiceType(serviceType);
				util.editLanguage(language);
				resp.setHeader("Refresh", "1;url=./languages");

			} else {
				/*
				 * resp.setHeader("Refresh", "1;url=./serviceInfo?servicename=" + languageName);
				 */
			}
		}

	}

	@RequestMapping(value = "/addLanguage", method = RequestMethod.POST)
	public String  add(@RequestBody LanguageSpecification lang, HttpServletResponse resp) {
		
		 System.out.print("Recieved" + lang.getLanguage()+ lang.getDataCoding()+
		 lang.getServiceType()+ lang.getScript() + lang.getEncoding());
		 
		//List<LanguageSpecification> languageList = util.getLanguages();
		
		LanguageSpecification language = new LanguageSpecification();
		language.setLanguage(lang.getLanguage());
		language.setDataCoding(lang.getDataCoding());
		language.setEncoding(lang.getEncoding());
		language.setScript(lang.getScript());
		language.setServiceType(lang.getServiceType());

		int status = util.addLanguage(language);
		
		System.out.println("status ="+status);

//		if (languageList != null && languageList.size() > 0) {
//			model.addAttribute("LanguageList", languageList);
//		}
//		resp.setHeader("Refresh", "1;url=./languages");
//		int status = util.addLanguage(language);

	    return status > 0 ? "success" : "failure";

	}

}// End Of Mapping
