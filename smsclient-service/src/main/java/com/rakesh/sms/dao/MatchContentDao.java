package com.rakesh.sms.dao;

import java.util.*;

import com.rakesh.sms.entity.MatchContent;


public interface MatchContentDao {

	MatchContent getMatchContent(int id , String matchStage);

	//public void mytest();
}
