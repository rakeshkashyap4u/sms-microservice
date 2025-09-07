package com.rakesh.sms.bo;

import java.util.List;

import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;

public interface GatewayBo {

	public String addFormatDetails(SMSCFormats format);

	public SMSCConfigs getConfigDetails();

	public SMSCConfigs getConfigDetails(String circle);

	public SMSCFormats getFormatDetails(String cid);

	public List<SMSCConfigs> getAllCircleDetails();

	public List<String> getAllCircleNames();

	public List<String> getTRXCircleNames();

	public List<String> getTXCircleNames();

	public List<String> getRXCircleNames();
}
