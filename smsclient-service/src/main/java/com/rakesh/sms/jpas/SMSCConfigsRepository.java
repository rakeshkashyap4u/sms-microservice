package com.rakesh.sms.jpas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.SMSCConfigs;

@Repository
public interface SMSCConfigsRepository extends JpaRepository<SMSCConfigs, String> {

    List<SMSCConfigs> findAllByOpIdOrderByCidAsc(String opId);

    Optional<SMSCConfigs> findByOpIdAndCircle(String opId, String circle);
    
    List<SMSCConfigs> findAllByOrderByCidAsc();
}
