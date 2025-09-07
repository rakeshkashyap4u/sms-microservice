package com.rakesh.sms.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.SMSCDetails;

@Repository
public interface SMSCDetailsRepository extends JpaRepository<SMSCDetails, Long> {
    Optional<SMSCDetails> findByOperatorAndCountryAndProtocol(String operator, String country, String protocol);
}