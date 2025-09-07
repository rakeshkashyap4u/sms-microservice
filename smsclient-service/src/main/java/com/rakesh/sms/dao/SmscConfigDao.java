package com.rakesh.sms.dao;

import java.util.List;

import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCDetails;
import com.rakesh.sms.entity.SMSCFormats;

public interface SmscConfigDao {

	public Integer saveOrUpdate(SMSCConfigs details);

	public List<SMSCConfigs> getConfiguration();

	public String saveOrUpdate(SMSCFormats formats);

}
