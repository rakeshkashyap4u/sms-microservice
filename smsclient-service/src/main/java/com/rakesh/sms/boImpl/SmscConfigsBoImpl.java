package com.rakesh.sms.boImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakesh.sms.bo.SMSCConfigurationBo;
import com.rakesh.sms.dao.SmscConfigDao;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;

@Service
public class SmscConfigsBoImpl implements SMSCConfigurationBo {

	@Autowired
	SmscConfigDao smscconfig;

	public Integer saveOrUpdate(SMSCConfigs details) {
		return smscconfig.saveOrUpdate(details);
	}

	public List<SMSCConfigs> getAllConfiguration() {
		return smscconfig.getConfiguration();
	}

	public String saveOrUpdate(SMSCFormats formats) {
		return smscconfig.saveOrUpdate(formats);

	}

}
