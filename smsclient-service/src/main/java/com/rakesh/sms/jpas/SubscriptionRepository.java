package com.rakesh.sms.jpas;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rakesh.sms.entity.SmsSubscription;
import com.rakesh.sms.entity.Subscription;

import jakarta.transaction.Transactional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	@Query("""
		    SELECT s
		    FROM Subscription s
		    WHERE s.serviceName = :serviceName
		      AND (:statuses IS NULL OR s.status IN :statuses)
		      AND (:msgFromDate IS NULL OR s.startDate >= :msgFromDate)
		      AND (:msgToDate IS NULL OR s.startDate < :msgToDate)
		""")
		List<Subscription> findServiceActiveUsers(
		    @Param("serviceName") String serviceName,
		    @Param("statuses") List<String> statuses,
		    @Param("msgFromDate") Date msgFromDate,
		    @Param("msgToDate") Date msgToDate
		);

}