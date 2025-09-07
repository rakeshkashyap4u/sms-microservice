package com.rakesh.sms.daoImpl;


import org.hibernate.Session;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.smscDetailsDao;
import com.rakesh.sms.entity.SMSCDetails;
import com.rakesh.sms.jpas.SMSCDetailsRepository;

import java.util.*;

@Repository
@Transactional
public class smscDetailsDaoImpl implements smscDetailsDao {
	
	
	 private final SMSCDetailsRepository smscDetailsRepo;

	    public smscDetailsDaoImpl(SMSCDetailsRepository smscDetailsRepo) {
	        this.smscDetailsRepo = smscDetailsRepo;
	    }

	@Override
	public String saveOrUpdate(SMSCDetails details) {
		Session session = DBConnection.getSessionFactory("0").getCurrentSession();

		session.save(details);
		return details.getId();

	}

	@Override
	public void editDetails(SMSCDetails details) {
		Session session = DBConnection.getSessionFactory("0").getCurrentSession();

		session.saveOrUpdate(details);
	}

	 @Override
	    public List<SMSCDetails> getSMSCDetails() {
	        return smscDetailsRepo.findAll()
	                              .stream()
	                              .sorted((d1, d2) -> d1.getId().compareTo(d2.getId()))
	                              .toList();
	    }

}
