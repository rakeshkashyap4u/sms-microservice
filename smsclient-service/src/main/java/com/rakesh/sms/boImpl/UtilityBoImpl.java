package com.rakesh.sms.boImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rakesh.sms.beans.BlackoutHour;
import com.rakesh.sms.bo.UtilityBo;
import com.rakesh.sms.dao.UtilityDao;
import com.rakesh.sms.entity.BlackoutHours;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.MsgContents;
import com.rakesh.sms.entity.MsisdnSeries;
import com.rakesh.sms.entity.SMSBlacklist;
import com.rakesh.sms.entity.SMSWhitelist;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class UtilityBoImpl implements UtilityBo {

	@Autowired
	private UtilityDao utilDaoImpl;

	public UtilityDao getUtilDaoImpl() {
		return utilDaoImpl;
	}

	public void setUtilDaoImpl(UtilityDao utilDaoImpl) {
		this.utilDaoImpl = utilDaoImpl;
	}

	public void loadProperties() {
		CoreUtils.setProperties(this.utilDaoImpl.loadProperties());
	}// End Of Method

	public List<BlackoutHour> getBlackoutHours() {
		List<BlackoutHours> result = this.utilDaoImpl.getBlackoutHours();
		List<BlackoutHour> bhList = new ArrayList<BlackoutHour>(10);

		for (int i = 0; i < result.size(); i++) {
			BlackoutHours bhEntity = result.get(i);
			BlackoutHour bhBean = new BlackoutHour(bhEntity);
			Logger.sysLog(LogValues.info, this.getClass().getName(), bhBean.toString());
			bhList.add(bhBean);
		} // End Of Loop

		result.clear();
		return bhList;
	}// End Of Method

	public List<MsisdnSeries> getNumberSeriesDetails() {
		return this.utilDaoImpl.getNumberSeriesDetails();
	}// End Of Method
	
	public List<MsgContents> getMsgContentsData() {
		return this.utilDaoImpl.getMsgContentsDataDetails();
	}

	public List<LanguageSpecification> fetchLanguageSpecifications() {
		return this.utilDaoImpl.fetchLanguageSpecifications();
	}// End Of Method

	public List<SMSBlacklist> getBlacklisted() {
		return this.utilDaoImpl.getBlacklisted();
	}// End Of Method

	public List<SMSWhitelist> getWhitelisted() {
		return this.utilDaoImpl.getWhitelisted();
	}// End Of Method

	public int addBlacklistUsers(List<String> baseList, int isSeries) {
		return this.utilDaoImpl.addBlacklistUsers(baseList, isSeries);
	}// End Of Method

	public int addWhitelistUsers(List<String> baseList, String shortcode) {
		return this.utilDaoImpl.addWhitelistUsers(baseList, shortcode);
	}// End Of Method

	public String getRevenueInfo(String keyword, String shortcode) {
		return this.utilDaoImpl.getRevenueInfo(keyword, shortcode);
	}// End Of Method
}
