package com.rakesh.sms.dao;

import java.util.List;

import com.rakesh.sms.entity.MatchContent;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;

public interface GatewayDao {

	public String addFormatDetails(SMSCFormats format);

	public SMSCConfigs getConfigDetails(String circle);

	public SMSCFormats getFormatDetails(String cid);

	public List<SMSCConfigs> getAllCircles();

	public MatchContent getMatchContent(int id, String matchType);


}
