package com.rakesh.sms.daoImpl;

import java.awt.print.Pageable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.SmsPromoDao;
import com.rakesh.sms.entity.PromotionMsisdn1;
import com.rakesh.sms.entity.SMSPromotion1;
import com.rakesh.sms.entity.Subscription;
import com.rakesh.sms.jpas.PromotionMsisdnRepository;
import com.rakesh.sms.jpas.SMSPromotionRepository;
import com.rakesh.sms.jpas.SmsLogsRepository;
import com.rakesh.sms.jpas.SmsSubscriptionRepository;
import com.rakesh.sms.jpas.SubscriptionRepository;
import com.rakesh.sms.jpas.SubscriptionRepository;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class SmsPromoDaoImpl implements  SmsPromoDao{

	 private final SMSPromotionRepository promotionRepo;
	    private final PromotionMsisdnRepository msisdnRepo;
	    private final SmsSubscriptionRepository subscriptionRepo;
	    private final SubscriptionRepository  subRepo;
	    
	    private final SmsLogsRepository smsLogsRepo;

	    public SmsPromoDaoImpl(SMSPromotionRepository promotionRepo,
	                           PromotionMsisdnRepository msisdnRepo,SmsSubscriptionRepository subscriptionRepo
	                           ,SmsLogsRepository smsLogsRepo,SubscriptionRepository  subRepo) {
	        this.promotionRepo = promotionRepo;
	        this.msisdnRepo = msisdnRepo;
	        this.subscriptionRepo=subscriptionRepo;
	        this.smsLogsRepo=smsLogsRepo;
	        this.subRepo=subRepo;
	    }

	    @Override
	    public List<SMSPromotion1> getSmsPromotion(String dateStr) {
	        try {
	            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
	            return promotionRepo.findByStatusAndStartDateTimeLessThanEqualOrStatusAndExpiryDateTimeGreaterThanEqual(
	                    "new", date, "running", date
	            );
	        } catch (ParseException e) {
	            Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	            return new ArrayList<>(); // Return empty list if date parsing fails
	        }
	    }


	    @Override
	    public List<PromotionMsisdn1> getPromoMsisdn(String promoName) {
	        return msisdnRepo.findByPromotionNameAndStatus(promoName, "new");
	    }

	    @Override
	    public SMSPromotion1 getPromotion(String promoName) {
	        return promotionRepo.findByPromotionName(promoName);
	    }

	    @Override
	    public boolean updateStatus(String promoName, String status) {
	        SMSPromotion1 promo = promotionRepo.findByPromotionName(promoName);
	        if (promo != null) {
	            promo.setStatus(status);
	            promotionRepo.save(promo);
	            return true;
	        }
	        return false;
	    }

	    @Override
	    public boolean updatePromoMsisdnStatus(String promoName, List<String> msisdns, String status, int count) {
	        List<PromotionMsisdn1> list = msisdnRepo.findByPromotionNameAndMsisdnIn(promoName, msisdns);
	        list.forEach(p -> p.setStatus(status));
	        msisdnRepo.saveAll(list);
	        return !list.isEmpty();
	    }

	    @Override
	    public List<String> getActiveUser(String serviceName, String userStatus, Date msgFromDate, Date msgToDate) {
	        List<String> statuses = null;
	        if (userStatus != null && !userStatus.isEmpty()) {
	            statuses = Arrays.asList(userStatus.split(",")); // split multiple statuses if comma-separated
	        }

	        List<Subscription> subscriptions = subRepo.findServiceActiveUsers(
	            serviceName, statuses, msgFromDate, msgToDate
	        );

	        // Extract msisdns
	        return subscriptions.stream()
	                            .map(Subscription::getMsisdn)
	                            .toList();
	    }



	    @Override
	    public boolean updateSubscribedUserStatus(String promoName, List<String> msisdns, String status, int count) {
	        if(msisdns == null || msisdns.isEmpty()) return false;

	        // Limit the list to 'count' if needed
	        List<String> limitedList = msisdns.size() > count ? msisdns.subList(0, count) : msisdns;

	        int updated = msisdnRepo.updateStatusForUsers(promoName, limitedList, status);

	        return updated > 0;
	    }

		@Transactional
		public boolean insertServiceMsisdns(SMSPromotion1 sp, String promoName, long promoId, List<String> msisdns) {
		    List<PromotionMsisdn1> entities = new ArrayList<>();
		    long maxId = msisdnRepo.findMaxId().orElse(0L);

		    for (int i = 0; i < msisdns.size(); i++) {
		        PromotionMsisdn1 p = new PromotionMsisdn1();
		        p.setId(maxId + i + 1);
		        p.setPromotionName(promoName);
		        p.setMsisdn(msisdns.get(i));
		        p.setStatus("new");
		        p.setSmspromo1(sp);
		        entities.add(p);
		    }

		    msisdnRepo.saveAll(entities);
		    return true;
		}

		public List<String> getMostActiveUser(int limit){
			
			
			//CommonDaoImpl sf = new CommonDaoImpl();

			List<String> msisdns = new ArrayList();//sf.getMostActiveUser(limit);

			return msisdns;
			
		}


	}