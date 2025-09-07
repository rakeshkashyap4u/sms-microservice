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

public interface SmsSubscriptionRepository extends JpaRepository<SmsSubscription, Long> {

    Optional<SmsSubscription> findByMsisdnAndServiceid(String msisdn, String serviceid);

    Optional<SmsSubscription> findByMsisdnAndServiceidAndSubserviceid(String msisdn, String serviceid, String subserviceid);

    List<SmsSubscription> findByServiceidAndStatus(String serviceid, String status);

    List<SmsSubscription> findByServiceidAndStatusAndSubserviceid(String serviceid, String status, String subserviceid);

    List<SmsSubscription> findByServiceidAndStatusAndLanguage(String serviceid, String status, String language);

    List<SmsSubscription> findByServiceidAndStatusAndSubserviceidAndLanguage(
        String serviceid, String status, String subserviceid, String language
    );

    @Query("SELECT s FROM SmsSubscription s " +
           "WHERE s.serviceid = :serviceid " +
           "AND s.status = :status " +
           "AND s.startdate <= :sd " +
           "AND s.enddate >= :ed")
    List<SmsSubscription> findActiveInDateRange(String serviceid, String status, Date sd, Date ed);

    @Query("""
        SELECT COUNT(s) > 0 
        FROM SmsSubscription s
        WHERE s.msisdn = :msisdn
          AND s.serviceid = :serviceid
          AND s.status = 'ACTIVE'
          AND (:subserviceid IS NULL OR :subserviceid = '' OR s.subserviceid = :subserviceid)
    """)
    boolean isSubscribed(
        @Param("msisdn") String msisdn,
        @Param("serviceid") String serviceid,
        @Param("subserviceid") String subserviceid
    );

    @Modifying
    @Transactional
    @Query("""
        UPDATE SmsSubscription s
        SET s.msgflag = :flag
        WHERE s.msisdn = :msisdn
          AND s.serviceid = :serviceid
          AND s.status = 'ACTIVE'
          AND (:subserviceid IS NULL OR :subserviceid = '' OR s.subserviceid = :subserviceid)
    """)
    int updateFlag(
        @Param("msisdn") String msisdn,
        @Param("serviceid") String serviceid,
        @Param("subserviceid") String subserviceid,
        @Param("flag") String flag
    );

    List<SmsSubscription> findBySubserviceid(String subserviceid);

    List<SmsSubscription> findByServiceidAndStatusAndStartdateBetween(
        String serviceid, String status, Date startdate, Date enddate
    );
}
