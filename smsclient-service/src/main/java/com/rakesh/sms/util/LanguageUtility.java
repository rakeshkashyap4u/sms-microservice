package com.rakesh.sms.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rakesh.sms.dao.LanguageDao;
import com.rakesh.sms.dao.SmscConfigDao;
import com.rakesh.sms.daoImpl.LanguageDaoImpl;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.SMSCConfigs;

@Service
public class LanguageUtility {

	
	public LanguageDao languagedao;
	
	@Autowired
	public LanguageUtility(LanguageDao languagedao)
	{
		this.languagedao=languagedao;
	}

	public static List<LanguageSpecification> LanguageList;
	public static String language;
	public static int dataCoding;
	public static String serviceType;
	public static String encoding;
	public static int script;

	public LanguageDao getLanguagedao() {
		return languagedao;
	}

	public void setLanguagedao(LanguageDao languagedao) {
		this.languagedao = languagedao;
	}

	public List<LanguageSpecification> getLanguages() {
		LanguageList = languagedao.getLanguages();
		return LanguageList;
	}

	/*
	 * public LanguageDao getLanguageDao() { return languageDao; }
	 */

	public static String getLanguage() {
		return language;
	}

	public static void setLanguage(String language) {
		LanguageUtility.language = language;
	}

	public static int getDataCoding() {
		return dataCoding;
	}

	public static void setDataCoding(int dataCoding) {
		LanguageUtility.dataCoding = dataCoding;
	}

	public static String getServiceType() {
		return serviceType;
	}

	public static void setServiceType(String serviceType) {
		LanguageUtility.serviceType = serviceType;
	}

	public static String getEncoding() {
		return encoding;
	}

	public static void setEncoding(String encoding) {
		LanguageUtility.encoding = encoding;
	}

	public static int getScript() {
		return script;
	}

	public static void setScript(int script) {
		LanguageUtility.script = script;
	}

	public int addLanguage(LanguageSpecification language) {
		
		LanguageList = languagedao.getLanguages();
		System.out.println("LanguageList size: " + (LanguageList != null ? LanguageList.size() : "null"));
		System.out.println("LanguageList");
	    if (LanguageList != null) {
	    	for (LanguageSpecification obj : LanguageList) {
	    	    if (Objects.equals(language.getLanguage(), obj.getLanguage())) { // handles nulls
	    	        return -1; // duplicate language
	    	    }
	    	}


	        // Persist and get the managed entity with ID
	        LanguageSpecification saved = languagedao.addLanguage(language);
	        LanguageList.add(saved);
	        System.out.println("LanguageList "+LanguageList);

	        return saved.getLid(); // ✅ now it's not null
	    }
	    
	    System.out.println("LanguageList is "+LanguageList);

	    return -1; // or throw exception if LanguageList is unexpectedly null
	}


	public String editLanguage(LanguageSpecification language) {
		if (language != null) {
			String status = languagedao.editLanguage(language);
			return status;
		}
		return "language object is null";
	}

}
