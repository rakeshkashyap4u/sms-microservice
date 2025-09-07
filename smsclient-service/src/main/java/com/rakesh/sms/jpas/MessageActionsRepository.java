package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.MessageActions;

@Repository
public interface MessageActionsRepository extends JpaRepository<MessageActions, Integer> {
    List<MessageActions> findByMoIdOrderByAidAsc(int moId);
    
}