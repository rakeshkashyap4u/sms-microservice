package com.rakesh.sms.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.ActiveAlerts;

@Repository
public interface ActiveAlertsRepository extends JpaRepository<ActiveAlerts, Long> {
    // You already get findAll() for free
	Optional<ActiveAlerts> findFirstByServiceid(String serviceid);
    Optional<ActiveAlerts> findFirstByServiceidAndSubserviceid(String serviceid, String subserviceid);
}
