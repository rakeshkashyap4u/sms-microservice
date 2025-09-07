package com.rakesh.sms.boImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rakesh.sms.bo.OperatorBo;
import com.rakesh.sms.dao.OperatorDao;
import com.rakesh.sms.entity.Operator;

@Service
public class OperatorServiceImpl implements OperatorBo {
	@Autowired
	OperatorDao operatorDao;

	public List<Operator> getAllOperators() {
		return operatorDao.getAllOperators();

	}
}
