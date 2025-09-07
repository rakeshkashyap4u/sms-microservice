package com.rakesh.sms.boImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakesh.sms.bo.CountryBo;
import com.rakesh.sms.dao.CountryDao;
import com.rakesh.sms.entity.Country;

@Service
public class CountryServiceImpl implements CountryBo {

	@Autowired
	CountryDao countryDao;

	public List<Country> getAllCountry() {
		return countryDao.getAllCountry();

	}
}
