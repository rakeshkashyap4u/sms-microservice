package com.rakesh.sms.boImpl;

import com.rakesh.sms.bo.MatchContentBo;
import com.rakesh.sms.dao.MatchContentDao;
import com.rakesh.sms.entity.MatchContent;


public class MatchContentBoImpl implements MatchContentBo{

	
	private MatchContentDao matchContentDao;
	
	
	
	 public MatchContentDao getMatchContentDao() {
		return matchContentDao;
	}



	public void setMatchContentDao(MatchContentDao matchContentDao) {
		this.matchContentDao = matchContentDao;
	}



	public MatchContent getMatchContent(int id , String matchStage) {
			return this.matchContentDao.getMatchContent(id, matchStage);
		}

}
