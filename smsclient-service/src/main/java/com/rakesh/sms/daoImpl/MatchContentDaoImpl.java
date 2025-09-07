package com.rakesh.sms.daoImpl;


import java.util.Iterator;

import org.hibernate.Session;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.MatchContentDao;
import com.rakesh.sms.entity.MatchContent;
import com.rakesh.sms.jpas.LanguageSpecificationRepository;
import com.rakesh.sms.jpas.MatchContentRepository;
import com.rakesh.sms.util.LogValues;
import com.rakesh.sms.util.Logger;


public class MatchContentDaoImpl implements MatchContentDao  {
	

        private final MatchContentRepository matchContentRepo;

        public MatchContentDaoImpl(MatchContentRepository matchContentRepo) {
            this.matchContentRepo = matchContentRepo;
        }

        public MatchContent getMatchContent(int id, String matchStage) {
            try {
                // Use repository method that matches your query
                return matchContentRepo.findFirstByIdAndType(id, matchStage).orElse(null);
            } catch (Exception e) {
                Logger.sysLog(LogValues.error, this.getClass().getName(), Logger.getStack(e));
                return null;
            }
        }
	
}
