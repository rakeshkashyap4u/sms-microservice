package com.rakesh.sms.dao;

import java.util.List;

import com.rakesh.sms.entity.SMSCDetails;

public interface smscDetailsDao {

	public String saveOrUpdate(SMSCDetails details);

	public List<SMSCDetails> getSMSCDetails();

	public void editDetails(SMSCDetails details);

}
