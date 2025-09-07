package com.rakesh.sms.dao;

import java.util.HashMap;
import java.util.List;

import com.rakesh.sms.entity.BlackoutHours;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.MsgContents;
import com.rakesh.sms.entity.MsisdnSeries;
import com.rakesh.sms.entity.SMSBlacklist;
import com.rakesh.sms.entity.SMSWhitelist;

public interface UtilityDao {

	public List<SMSBlacklist> getBlacklisted();

	public List<SMSWhitelist> getWhitelisted();

	public List<BlackoutHours> getBlackoutHours();

	public HashMap<String, String> loadProperties();

	public List<MsisdnSeries> getNumberSeriesDetails();

	public String getRevenueInfo(String keyword, String shortcode);

	public List<LanguageSpecification> fetchLanguageSpecifications();

	public int addBlacklistUsers(List<String> baseList, int isSeries);

	public int addWhitelistUsers(List<String> baseList, String shortcode);
	
	public List<MsgContents> getMsgContentsDataDetails();
}
