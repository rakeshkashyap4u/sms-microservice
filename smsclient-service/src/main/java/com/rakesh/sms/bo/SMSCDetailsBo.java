package com.rakesh.sms.bo;

import java.util.*;

import com.rakesh.sms.entity.SMSCDetails;

public interface SMSCDetailsBo {

	public String saveOrUpdate(SMSCDetails details);

	public List<SMSCDetails> getSMSCDetails();

	public void editDetails(SMSCDetails details);

}
