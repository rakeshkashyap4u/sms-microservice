package com.rakesh.sms.boImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakesh.sms.bo.SMSCDetailsBo;
import com.rakesh.sms.dao.smscDetailsDao;
import com.rakesh.sms.entity.SMSCDetails;

@Service
public class SMSCDetailsBoImpl implements SMSCDetailsBo {

	@Autowired
	smscDetailsDao SMSCDetails;

	public String saveOrUpdate(SMSCDetails details) {
		return SMSCDetails.saveOrUpdate(details);
	}

	public List<SMSCDetails> getSMSCDetails() {
		return SMSCDetails.getSMSCDetails();
	}

	public void editDetails(SMSCDetails details) {
		SMSCDetails.editDetails(details);
	}

}
