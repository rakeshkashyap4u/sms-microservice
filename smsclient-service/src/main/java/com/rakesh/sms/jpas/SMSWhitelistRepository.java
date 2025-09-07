package com.rakesh.sms.jpas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.SMSWhitelist;

@Repository
public interface SMSWhitelistRepository extends JpaRepository<SMSWhitelist, Integer> { }