package com.rakesh.sms.daoImpl;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.LanguageDao;
import com.rakesh.sms.entity.LanguageSpecification;
import com.rakesh.sms.jpas.LanguageSpecificationRepository;
import com.rakesh.sms.util.LogValues;

@Repository
@Transactional
public class LanguageDaoImpl implements LanguageDao {
	
	private final LanguageSpecificationRepository languageRepo;

    public LanguageDaoImpl(LanguageSpecificationRepository languageRepo) {
        this.languageRepo = languageRepo;
    }

    @Override
    public List<LanguageSpecification> getLanguages() {
        try {
            return languageRepo.findAllByOrderByLidAsc();
        } catch (Exception e) {
           // Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
            return List.of(); // Return empty list on error
        }
    }

	public boolean addLanguage(LanguageSpecification language) {

		try {
			Session session = DBConnection.getSessionFactory("0").getCurrentSession();
			session.saveOrUpdate(language);

			return true;
		} catch (Exception e) {

			return false;
		} // End Of Try-Catch
	}// End Of Method

	public String editLanguage(LanguageSpecification language) {

		Session session = DBConnection.getSessionFactory("0").getCurrentSession();
		try {
			session.saveOrUpdate(language);
		} catch (Exception e) {
			return "Failure";
		}

		return "Success";

	}// End Of Method

}
