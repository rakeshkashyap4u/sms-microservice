package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.SmsProperties;

@Repository
public interface SmsPropertiesRepository extends JpaRepository<SmsProperties, Long> {
    List<SmsProperties> findAll();
}
