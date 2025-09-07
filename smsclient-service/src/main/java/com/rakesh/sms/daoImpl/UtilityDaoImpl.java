package com.rakesh.sms.daoImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import org.hibernate.Session;

import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.UtilityDao;
import com.rakesh.sms.entity.BlackoutHours;
import com.rakesh.sms.entity.KPIReportDaily;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.entity.MsgContents;
import com.rakesh.sms.entity.MsisdnSeries;
import com.rakesh.sms.entity.SMSBlacklist;
import com.rakesh.sms.entity.SMSWhitelist;
import com.rakesh.sms.entity.SmsProperties;
import com.rakesh.sms.jpas.BlackoutHoursRepository;
import com.rakesh.sms.jpas.KPIReportDailyRepository;
import com.rakesh.sms.jpas.LanguageSpecificationRepository;
import com.rakesh.sms.jpas.MsgContentsRepository;
import com.rakesh.sms.jpas.MsisdnSeriesRepository;
import com.rakesh.sms.jpas.SMSBlacklistRepository;
import com.rakesh.sms.jpas.SMSWhitelistRepository;
import com.rakesh.sms.jpas.SmsPropertiesRepository;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class UtilityDaoImpl implements UtilityDao {
	
	
	private final SmsPropertiesRepository smsPropertiesRepo;
	private final BlackoutHoursRepository blackoutHoursRepo;
	private final MsgContentsRepository msgContentsRepo;
	private final LanguageSpecificationRepository languageSpecificationRepo;
	private final SMSWhitelistRepository smsWhitelistRepo;
	private final SMSBlacklistRepository smsBlacklistRepo;
	private final KPIReportDailyRepository kpiReportDailyRepo;
	private final MsisdnSeriesRepository msisdnSeriesRepo;

    public UtilityDaoImpl(SmsPropertiesRepository smsPropertiesRepo,
    		BlackoutHoursRepository blackoutHoursRepo,
    		MsgContentsRepository msgContentsRepo,
    		LanguageSpecificationRepository languageSpecificationRepo,
    		SMSWhitelistRepository smsWhitelistRepo,
    		SMSBlacklistRepository smsBlacklistRepo,
    		KPIReportDailyRepository kpiReportDailyRepo,
    		MsisdnSeriesRepository msisdnSeriesRepo
    		) {
        this.smsPropertiesRepo = smsPropertiesRepo;
        this.blackoutHoursRepo=blackoutHoursRepo;
        this.msgContentsRepo=msgContentsRepo;
        this.languageSpecificationRepo=languageSpecificationRepo;
        this.smsWhitelistRepo=smsWhitelistRepo;
        this.smsBlacklistRepo=smsBlacklistRepo;
        this.kpiReportDailyRepo=kpiReportDailyRepo;
        this.msisdnSeriesRepo=msisdnSeriesRepo;
    }

	@Transactional
	public HashMap<String, String> loadProperties() {
	    HashMap<String, String> properties = new HashMap<>();

	    try {
	        List<SmsProperties> list = smsPropertiesRepo.findAll();

	        for (SmsProperties smsc : list) {
	            String key = smsc.getKey();
	            String value = smsc.getValue();

	            if (key != null && value != null && !key.contains("NULL") && !value.contains("NULL")) {
	                properties.put(key.trim(), value.trim());
	                Logger.sysLog(LogValues.debug, this.getClass().getName(),
	                        "Properties:: Key=" + key.trim() + " | Value=" + value.trim());
	            } else {
	                Logger.sysLog(LogValues.warn, this.getClass().getName(),
	                        "Properties has NULL values");
	            }
	        }

	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	    }

	    return properties;
	}


	@Transactional
	public List<BlackoutHours> getBlackoutHours() {
	    try {
	        List<BlackoutHours> hours = blackoutHoursRepo.findAll();
	        Logger.sysLog(LogValues.debug, this.getClass().getName(),
	                "Fetched " + hours.size() + " blackout hours.");
	        return hours;
	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	        return new ArrayList<>();
	    }
	}


	@Transactional
	public List<MsisdnSeries> getNumberSeriesDetails() {
	    List<MsisdnSeries> list = msisdnSeriesRepo.findAll();
	    Logger.sysLog(LogValues.info, this.getClass().getName(), "numberSeries size: " + list.size());
	    return list;
	}
	
	@Transactional
	public List<MsgContents> getMsgContentsDataDetails() {
	    return msgContentsRepo.findAll();
	}

	@Transactional
	public List<LanguageSpecification> fetchLanguageSpecifications() {
	    return languageSpecificationRepo.findAll();
	}

	@Transactional
	public List<SMSBlacklist> getBlacklisted() {
	    return smsBlacklistRepo.findAll();
	}


	@Transactional
	public List<SMSWhitelist> getWhitelisted() {
	    return smsWhitelistRepo.findAll();
	}

	@Transactional
	public int addBlacklistUsers(List<String> baseList, int isSeries) {
	    List<SMSBlacklist> entities = baseList.stream()
	        .map(msisdn -> {
	            SMSBlacklist b = new SMSBlacklist();
	            b.setMsisdn(msisdn);
	            b.setIsseries(isSeries);
	            return b;
	        }).toList();

	    List<SMSBlacklist> saved = smsBlacklistRepo.saveAll(entities);
	    return saved.size() > 0 ? 1 : -1;
	}

	@Transactional
	public int addWhitelistUsers(List<String> baseList, String shortcode) {
	    List<SMSWhitelist> entities = baseList.stream()
	        .map(msisdn -> {
	            SMSWhitelist w = new SMSWhitelist();
	            w.setMsisdn(msisdn);
	            w.setShortcode(shortcode);
	            return w;
	        }).toList();

	    List<SMSWhitelist> saved = smsWhitelistRepo.saveAll(entities);
	    return saved.size() > 0 ? 1 : -1;
	


	}// End Of Method

	@Transactional
	public String getRevenueInfo(String keyword, String shortcode) {
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	    String reportDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

	    List<KPIReportDaily> records = kpiReportDailyRepo.findByReportDateAndKeywordAndShortcode(reportDate, keyword, shortcode);

	    StringBuilder info = new StringBuilder();
	    for (KPIReportDaily k : records) {
	        info.append(k.getType()).append("=").append(k.getTotal()).append(",");
	    }
	    return info.toString();
	}
}
