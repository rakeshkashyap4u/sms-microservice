package com.rakesh.sms.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.SMSCFormats;

@Repository
public interface SMSCFormatsRepository extends JpaRepository<SMSCFormats, String> {
    Optional<SMSCFormats> findByCid(String cid);
}