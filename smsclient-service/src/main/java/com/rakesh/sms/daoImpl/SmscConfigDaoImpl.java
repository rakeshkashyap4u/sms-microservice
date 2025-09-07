package com.rakesh.sms.daoImpl;


import java.util.List;


import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.rakesh.sms.dao.SmscConfigDao;
import com.rakesh.sms.entity.SMSCConfigs;
import com.rakesh.sms.entity.SMSCFormats;
import com.rakesh.sms.jpas.SMSCConfigsRepository;

@Repository
@Transactional
public class SmscConfigDaoImpl implements SmscConfigDao {

	/*
	 * @Override public boolean saveOrUpdate(SMSCConfigs details) { Session session
	 * = DBConnection.getSessionFactory("0").getCurrentSession();
	 * System.out.println("inside saveOrUpdate"); session.saveOrUpdate(details);
	 * return true; }
	 */
	
	private final SMSCConfigsRepository smscConfigsRepo;

    public SmscConfigDaoImpl(SMSCConfigsRepository smscConfigsRepo) {
        this.smscConfigsRepo = smscConfigsRepo;
    }

	@Override
	public Integer saveOrUpdate(SMSCConfigs config) {
		Session session = DBConnection.getSessionFactory("0").getCurrentSession();
		System.out.println("inside saveOrUpdate");
		session.saveOrUpdate(config);
		return config.getCid();
	}

	 public List<SMSCConfigs> getConfiguration() {
	        try {
	            List<SMSCConfigs> configs = smscConfigsRepo.findAllByOrderByCidAsc();
	            
	            System.out.println("Total configs: " + configs.size());
	            for (SMSCConfigs con : configs) {
	                System.out.println(con.getCircle() + " " + con.getPassword());
	            }
	            return configs;
	        } catch (Exception e) {
	            System.out.println("Error fetching configs: " + e.getMessage());
	            return List.of(); // return empty list on error
	        }
	    }

	@Override
	public String saveOrUpdate(SMSCFormats formats) {
		Session session = DBConnection.getSessionFactory("0").getCurrentSession();
		System.out.println("inside saveOrUpdate");
		session.save(formats);
		return formats.getRid();
	}

}
