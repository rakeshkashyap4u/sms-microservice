package com.rakesh.sms.daoImpl;

import java.util.ArrayList;
import java.util.List;


import org.hibernate.Session;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.dao.OperatorDao;
import com.rakesh.sms.entity.Operator;
import com.rakesh.sms.jpas.OperatorRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class OperatorDaoImpl implements OperatorDao {

    private final OperatorRepository operatorRepo;

    public OperatorDaoImpl(OperatorRepository operatorRepo) {
        this.operatorRepo = operatorRepo;
    }

    @Override
    public List<Operator> getAllOperators() {
        try {
            return operatorRepo.findAllByOrderByIdAsc();
        } catch (Exception e) {
            e.printStackTrace(); // Or use your Logger
            return List.of(); // Return empty list on error
        }
    }
}

