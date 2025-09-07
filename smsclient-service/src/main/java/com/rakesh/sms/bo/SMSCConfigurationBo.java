package com.rakesh.sms.bo;

import java.util.List;

import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;

public interface SMSCConfigurationBo {

	public Integer saveOrUpdate(SMSCConfigs details);

	public List<SMSCConfigs> getAllConfiguration();

	public String saveOrUpdate(SMSCFormats formats);

}
