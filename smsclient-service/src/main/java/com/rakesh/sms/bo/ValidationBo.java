package com.rakesh.sms.bo;

import java.util.List;

import com.rakesh.sms.beans.MODetails;
import com.rakesh.sms.entity.DoubleConsent;
import com.rakesh.sms.entity.MessageActions;
import com.rakesh.sms.entity.MessageFormats;
import com.rakesh.sms.entity.MtResponse;

public interface ValidationBo {

	public int addMO(MessageFormats format, MessageActions action);

	public int validate(String serviceCode, String query, String msisdn);

	public int getFailureScFormat();

	public int addMOAction(MessageActions action);

	public List<MessageActions> getActionsforMO(int moFormatId);

	public List<MODetails> getAllMOs();

	public List<MessageFormats> failureActionShortcodes();

	public DoubleConsent getFirstConsent(String msisdn, String moid);

	public void saveFirstConsent(DoubleConsent consent);

	public void removeConsent(String msisdn, String moid);
	
	public boolean addMtResponse(MtResponse mtresponse);
	
	public MtResponse getMTResponse(String tid);
	
	public String getQuestionFormat(String msisdn, String question , String options);

	public String getQuestionId(String msisdn);
	
	public String getContent(String key, String language);

	
	public String getQuestionFormatforDemo(String msisdn, String question, String options);

	public String getlastquestionfordemo(String msisdn);

	public String adddintable(String msisdn, String questionId);
}
