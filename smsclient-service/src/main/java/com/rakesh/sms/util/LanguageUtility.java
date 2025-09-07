package com.rakesh.sms.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rakesh.sms.dao.LanguageDao;
import com.rakesh.sms.dao.SmscConfigDao;
import com.rakesh.sms.daoImpl.LanguageDaoImpl;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.SMSCConfigs;

@Service
public class LanguageUtility {

	@Autowired
	public LanguageDao languagedao;

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
		if (LanguageList != null) {
			for (Iterator<LanguageSpecification> iter = LanguageList.iterator(); iter.hasNext();) {
				LanguageSpecification obj = iter.next();
				if (language.getLanguage().equals(obj.getLanguage()))
					return -1;
			}

			LanguageList.add(language);
			boolean statusAdd = languagedao.addLanguage(language);
		}
		return language.getLid();

	}

	public String editLanguage(LanguageSpecification language) {
		if (language != null) {
			String status = languagedao.editLanguage(language);
			return status;
		}
		return "language object is null";
	}

}
