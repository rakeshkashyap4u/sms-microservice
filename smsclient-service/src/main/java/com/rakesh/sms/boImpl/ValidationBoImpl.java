package com.rakesh.sms.boImpl;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.rakesh.sms.beans.MODetails;
import com.rakesh.sms.bo.ValidationBo;
import com.rakesh.sms.dao.ValidationDao;
import com.rakesh.sms.entity.DoubleConsent;
import com.rakesh.sms.entity.MessageActions;
import com.rakesh.sms.entity.MessageFormats;
import com.rakesh.sms.entity.MtResponse;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;

public class ValidationBoImpl implements ValidationBo {

	private ValidationDao validationDao;

	public void setValidationDao(ValidationDao validationDao) {
		this.validationDao = validationDao;
	}

	public ValidationDao getValidationDao() {
		return validationDao;
	}

	public boolean addMtResponse(MtResponse mtresponse) {
		return this.validationDao.addMtResponse(mtresponse);
	}
	
	
	public MtResponse getMTResponse(String tid) {
		return this.validationDao.getMTResponse(tid);
	}
	
	public int addMO(MessageFormats format, MessageActions action) {
		return this.validationDao.addMO(format, action);
	}

	public int validate(String serviceCode, String query, String msisdn) {
		String escapedQuery = StringEscapeUtils.escapeJava(query);
		return this.validationDao.validate(serviceCode.trim(), escapedQuery.trim(), msisdn);
	}

	public int getFailureScFormat() {
		return this.validationDao.getFailureScFormat();
	}

	public int addMOAction(MessageActions action) {
		return this.validationDao.addMOAction(action);
	}

	public List<MessageActions> getActionsforMO(int moFormatId) {
		return this.validationDao.getActionsforMO(moFormatId);
	}

	public List<MODetails> getAllMOs() {
		return validationDao.getAllMOs();
	}// End Of Method

	public List<MessageFormats> failureActionShortcodes() {
		return validationDao.failureActionShortcodes();
	}// End Of Method

	public void saveFirstConsent(DoubleConsent consent) {
		this.validationDao.saveFirstConsent(consent);
	}
	
	public String getQuestionFormat(String msisdn, String question , String options) {
		return this.validationDao.getQuestionFormat(msisdn, question , options);
	}
	
	public String getQuestionId(String msisdn) {
		return this.validationDao.getQuestionId(msisdn);
	}
	
	public String getContent(String key , String language) {
		return this.validationDao.getContent(key , language);
	}

	public DoubleConsent getFirstConsent(String msisdn, String moid) {
		if (msisdn != null && moid != null) {
			try {
				int moId = Integer.parseInt(moid.trim());
				return this.validationDao.getFirstConsent(msisdn.trim(), moId);
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" Invalid MO-ID to Process Second Consent :: " + moid);
			} // End Of Try Catch
		}
		return null;
	}// End Of Method

	public void removeConsent(String msisdn, String moid) {
		if (msisdn != null && moid != null) {
			try {
				int moId = Integer.parseInt(moid.trim());
				this.validationDao.removeConsent(msisdn.trim(), moId);
			} catch (NumberFormatException nbe) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" Invalid MO-ID to Delete First Consent Record :: " + moid);
			} catch (Exception e) {
				Logger.sysLog(LogValues.error, this.getClass().getName(),
						" Unable to Delete First Consent Record for MO-ID :: " + moid + " \n" + Logger.getStack(e));
			} // End Of Try Catch
		} // End Of Null Check
	}// End Of Method

	
	
	
	@Override
	public String getQuestionFormatforDemo(String msisdn, String question, String options) {
		// TODO Auto-generated method stub
		return this.validationDao.getQuestionFormatforDemo(msisdn, question , options);
	}

	@Override
	public String getlastquestionfordemo(String msisdn) {
		// TODO Auto-generated method stub
		return validationDao.getlastquestionfordemo(msisdn);
	}

	@Override
	public String adddintable(String msisdn, String questionId) {
		return  validationDao.adddintable( msisdn,  questionId);
		
	}

	
}
