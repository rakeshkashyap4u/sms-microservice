package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.DoubleConsent;

@Repository
public interface DoubleConsentRepository extends JpaRepository<DoubleConsent, Integer> {
    List<DoubleConsent> findByMsisdnAndMoId(String msisdn, Integer moId);
}