package com.rakesh.sms.daoImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.hibernate.Session;
import org.hibernate.StaleStateException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.rakesh.sms.beans.MODetails;
import com.rakesh.sms.dao.ValidationDao;
import com.rakesh.sms.entity.DoubleConsent;
import com.rakesh.sms.entity.GamingUser;
import com.rakesh.sms.entity.MessageActions;
import com.rakesh.sms.entity.MessageFormats;
import com.rakesh.sms.entity.MtResponse;
import com.rakesh.sms.entity.QuestionResponse;
import com.rakesh.sms.entity.SmsLogs;
import com.rakesh.sms.entity.SmsMessages;
import com.rakesh.sms.jpas.DoubleConsentRepository;
import com.rakesh.sms.jpas.GamingUserRepository;
import com.rakesh.sms.jpas.MessageActionsRepository;
import com.rakesh.sms.jpas.MessageFormatsRepository;
import com.rakesh.sms.jpas.MtResponseRepository;
import com.rakesh.sms.jpas.QuestionResponseRepository;
import com.rakesh.sms.jpas.SmsLogsRepository;
import com.rakesh.sms.jpas.SmsMessagesRepository;
import com.rakesh.sms.main.SmsValidation;
import com.rakesh.sms.util.CoreUtils;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

@Service
public class ValidationDaoImpl implements ValidationDao {
	
	
	 @Autowired
	    private MessageFormatsRepository messageFormatsRepo;
	 
	 @Autowired
	    private MessageActionsRepository messageFormatsRepo1;

	    @Autowired
	    private MessageActionsRepository messageActionsRepo;

	    @Autowired
	    private MtResponseRepository mtResponseRepo;

	    @Autowired
	    private SmsMessagesRepository smsMessagesRepo;

	    @Autowired
	    private DoubleConsentRepository doubleConsentRepo;

	    @Autowired
	    private GamingUserRepository gamingUserRepo;

	    @Autowired
	    private SmsLogsRepository smsLogsRepo;

	    @Autowired
	    private QuestionResponseRepository questionResponseRepo;

	@Transactional
	public int addMO(MessageFormats format, MessageActions action) { // itika: for insert

		try {

			Session session = DBConnection.getSessionFactory("0").getCurrentSession();
			int moId = (Integer) session.save(format);

			if (moId > 0) {
				action.setMoId(moId);
				session.save(action);
			} else {
				Logger.sysLog(LogValues.warn, this.getClass().getName(), " Unable to add MO ");
				moId = -1;
			}

			return moId;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Error Adding NEW MO in DB \n" + Logger.getStack(e));
		} // End Of Try Catch

		return -1;
	}// End Of Method

	 @Transactional
	    public boolean addMtResponse(MtResponse mtResponse) {
	        mtResponseRepo.save(mtResponse);
	        return true;
	    }

	 @Transactional
	    public MtResponse getMTResponse(String tid) {
	        List<MtResponse> responses = mtResponseRepo.findByTid(tid);
	        if (!responses.isEmpty()) {
	            MtResponse response = responses.get(0);
	            mtResponseRepo.delete(response);
	            return response;
	        }
	        return null;
	    }

	 @Transactional
	    public String getContent(String key, String language) {
	        List<SmsMessages> list = smsMessagesRepo.findByKeyAndLanguage(key, language);
	        return list.isEmpty() ? "" : list.get(0).getContent();
	    }

	 @Transactional(readOnly = true)
	 public int getFailureScFormat() {
	     return messageFormatsRepo.findFirstByServiceCode("wrong_sc")
	             .map(MessageFormats::getId)
	             .orElse(-1);
	 }

	@Transactional
	public int validate(String serviceCode, String query, String msisdn) {

	    int matchedMessageId = -1;

	    try {
	        // 1. Split SMS into keyword, subkey, and remaining arguments
	        String[] args = query.split("\\s+");
	        if (args.length < 1) {
	            Logger.sysLog(LogValues.error, getClass().getName(),
	                    "SMS received has LESS arguments than expected: " + query);
	            return matchedMessageId;
	        }

	        String keyword = args[0].trim();
	        String subkey = args.length > 1 && !args[1].trim().isEmpty() ? args[1].trim() : "#";

	        String[] queryArgs = new String[args.length - 2 > 0 ? args.length - 2 : 0];
	        for (int i = 2; i < args.length; i++) queryArgs[i - 2] = args[i].trim();

	        Logger.sysLog(LogValues.info, getClass().getName(),
	                "serviceCode: " + serviceCode + ", keyword: " + keyword + ", subkey: " + subkey);

	        // 2. Fetch matching MessageFormats from DB
	        List<MessageFormats> formats = messageFormatsRepo
	                .findByServiceCodeAndKeywordInAndSubkeyInOrderByIdDesc(
	                        serviceCode,
	                        List.of(keyword, SmsValidation.ANY),
	                        List.of(subkey, SmsValidation.ANY)
	                );

	        // 3. Loop through results and match arguments dynamically
	        for (MessageFormats format : formats) {
	            if (isArgumentsMatching(format, queryArgs)) {
	                matchedMessageId = format.getId();
	                Logger.sysLog(LogValues.info, getClass().getName(),
	                        "SMS matched format: " + format);
	                break;
	            }
	        }

	        if (matchedMessageId == -1) {
	            Logger.sysLog(LogValues.warn, getClass().getName(),
	                    "SMS DID NOT match any format: " + query);
	        }

	    } catch (Exception e) {
	        Logger.sysLog(LogValues.error, getClass().getName(), Logger.getStack(e));
	    }

	    return matchedMessageId;
	}


	private boolean isArgumentsMatching(MessageFormats format, String[] queryArgs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Transactional
	public int addMOAction(MessageActions action) {

		try {

			Session session = DBConnection.getSessionFactory("0").getCurrentSession();
			int aid = (Integer) session.save(action);

			if (aid < 0) {
				Logger.sysLog(LogValues.warn, this.getClass().getName(), " Unable to add MO ");
				aid = -1;
			}

			return aid;

		} catch (Exception e) {
			Logger.sysLog(LogValues.error, this.getClass().getName(),
					" Error Adding NEW MO in DB \n" + Logger.getStack(e));
		} // End Of Try Catch

		return -1;

	}// End Of Method

	 @Transactional
	    public List<MessageActions> getActionsforMO(int moFormatId) {
	        return messageActionsRepo.findByMoIdOrderByAidAsc(moFormatId);
	    }

	 @Transactional(readOnly = true)
	 public List<MODetails> getAllMOs() {
	     List<MessageFormats> formats = messageFormatsRepo.findAll();
	     List<MessageActions> actions = messageFormatsRepo1.findAll();

	     Map<Integer, List<MessageActions>> actionsMap = actions.stream()
	             .collect(Collectors.groupingBy(MessageActions::getMoId));

	     return formats.stream().map(format -> {
	         MODetails detail = new MODetails();
	         detail.setMoId(format.getId());
	         detail.setServiceid(format.getServiceid());
	         detail.setServicecode(format.getServiceCode());

	         String keyword = Stream.of(format.getKeyword(), format.getSubkey(), format.getArgument1(),
                     format.getArgument2(), format.getArgument3())
                 .filter(s -> s != null)      // <-- lambda replaces Objects::nonNull
                 .map(s -> s.replace("#", ""))
                 .collect(Collectors.joining(" "));
          detail.setKeyword(keyword.trim());

	         List<MessageActions> moActions = actionsMap.getOrDefault(format.getId(), List.of());
	         moActions.forEach(detail::addAction);

	         return detail;
	     }).collect(Collectors.toList());
	 }


	 @Transactional(readOnly = true)
	 public List<MessageFormats> failureActionShortcodes() {
	     return messageFormatsRepo.findByKeywordAndSubkey("wrong_key", "#");
	 }

	  @Transactional
	    public DoubleConsent getFirstConsent(String msisdn, Integer moid) {
	        List<DoubleConsent> list = doubleConsentRepo.findByMsisdnAndMoId(msisdn, moid);
	        return list.isEmpty() ? null : list.get(0);
	    }

	  @Transactional
	    public void saveFirstConsent(DoubleConsent consent) {
	        Calendar cal = Calendar.getInstance();
	        consent.setTimestamp(cal.getTime());
	        cal.add(Calendar.HOUR_OF_DAY, 24);
	        consent.setExpiresAt(cal.getTime());

	        List<DoubleConsent> list = doubleConsentRepo.findByMsisdnAndMoId(consent.getMsisdn(), consent.getMoId());
	        if (!list.isEmpty()) {
	            DoubleConsent existing = list.get(0);
	            existing.setMessage(consent.getMessage());
	            existing.setTimestamp(consent.getTimestamp());
	            existing.setExpiresAt(consent.getExpiresAt());
	            doubleConsentRepo.save(existing);
	        } else {
	            doubleConsentRepo.save(consent);
	        }
	    }

	  @Transactional
	    public void removeConsent(String msisdn, Integer moid) {
	        List<DoubleConsent> list = doubleConsentRepo.findByMsisdnAndMoId(msisdn, moid);
	        list.forEach(doubleConsentRepo::delete);
	    }

	@Transactional
	public String getQuestionFormat(String msisdn, String question, String options) {

		String msg = question;
		
		
		String optionin = CoreUtils.getProperty("optionin");
		
		if(optionin!= null &&optionin.equalsIgnoreCase("ABCD"))
		{
			
			String aboptions = getQuestionFormatforDemo( msisdn,  question,  options);
			
			return aboptions;
			
			
		}

		else
		{
		String queHeader = CoreUtils.getProperty("queHeader");

		String[] optionList = options.split(",");

		int totalOptions = optionList.length;
		optionList[0] = optionList[0].substring(1);
		optionList[totalOptions - 1] = optionList[totalOptions - 1].replace("]", "");

		for (int i = 0; i < totalOptions; i++) 
		{
			int temp = i + 1;
			msg += "\n" + temp + ". " + optionList[i].substring(1, optionList[i].length() - 1);
		}

		if (queHeader != null) 
		{
			msg += "\n" + queHeader;
		}

		Logger.sysLog(LogValues.info, this.getClass().getName(), "Quiz Question: " + msg);
		return msg;
		}
	}

	@Transactional(readOnly = true)
	public String getQuestionId(String msisdn) {
//	    return questionResponseRepo
//	            .findTopByUserIdOrderByIdDesc(msisdn)
//	            .map((QuestionResponse q) -> q.getQuestionId())
//	            .orElse(null);
		
		return "Q1";
	}





	
	
	public   String getQuestionFormatforDemo(String msisdn, String question, String options) {
		String queHeader = "";
		
		String msg = question;

		

		String[] optionList = options.split(",");
		
		

		int totalOptions = optionList.length;
		
		optionList[0] = optionList[0].substring(1);
		
		optionList[totalOptions - 1] = optionList[totalOptions - 1].replace("]", "");
	char temp='A';
		for (int i = 0; i < totalOptions; i++) 
		{
			//int temp = i + 1;
			
			//msg += "\n" + temp + ". " + optionList[i].substring(1, optionList[i].length() - 1);
			msg += "\n" + temp + ". " + optionList[i].replace("\"", "");
			temp=(char) (temp+1);
			//System.out.println(msg);
		}

		if (queHeader != null) {
			msg += "\n" + queHeader;
			
		}

		System.out.println(msg);
		return msg;
	}


	 @Transactional
	    public String adddintable(String msisdn, String questionid) {
	        List<GamingUser> users = gamingUserRepo.findByUseridAndQuestionid(msisdn, questionid);
	        if (users.isEmpty()) {
	            GamingUser user = new GamingUser();
	            user.setUserid(msisdn);
	            user.setQuestionid(questionid);
	            gamingUserRepo.save(user);
	            return "SAVED";
	        }
	        return "ALREADY PRESENT";
	    }

	
	  @Transactional
	    public String getlastquestionfordemo(String msisdn) {
	        List<SmsLogs> logs = smsLogsRepo.findBySenderOrderByMessageIdDesc(msisdn);
	        return logs.isEmpty() ? null : logs.get(0).getTransId();
	    }
}
