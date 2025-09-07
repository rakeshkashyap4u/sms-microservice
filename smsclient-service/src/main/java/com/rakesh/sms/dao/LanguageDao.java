package com.rakesh.sms.dao;

import java.util.*;

import com.rakesh.sms.entity.LanguageSpecification;

public interface LanguageDao {

	public List<LanguageSpecification> getLanguages();

	public boolean addLanguage(LanguageSpecification language);

	public String editLanguage(LanguageSpecification language);

}
