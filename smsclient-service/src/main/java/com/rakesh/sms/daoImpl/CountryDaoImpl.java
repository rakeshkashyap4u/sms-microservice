package com.rakesh.sms.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.CountryDao;
import com.rakesh.sms.entity.Country;
import com.rakesh.sms.jpas.CountryRepository;





@Repository
@Transactional
public class CountryDaoImpl implements CountryDao {
	
	
	     private final CountryRepository countryRepository;

	     public CountryDaoImpl(CountryRepository countryRepository) {
	         this.countryRepository = countryRepository;
	     }

	     @Override
	     public List<Country> getAllCountry() {
	         try {
	             return countryRepository.findAllByOrderByIdAsc();
	         } catch (Exception e) {
	          //  Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
	             return new ArrayList<>();
	         }
	     }
	 }

