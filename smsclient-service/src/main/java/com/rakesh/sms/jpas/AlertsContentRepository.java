package com.rakesh.sms.jpas;

import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.beans.AlertServiceDetails;
import com.rakesh.sms.entity.AlertsContent;

import jakarta.transaction.Transactional;

@Repository
public interface AlertsContentRepository extends JpaRepository<AlertsContent, Long> {
    List<AlertsContent> findByServiceIdAndMsgFlagAndMsgDay(String serviceId, String msgFlag, int msgDay);
    List<AlertsContent> findByServiceIdAndMsgFlagAndMsgDayAndSubServiceId(String serviceId, String msgFlag, int msgDay, String subServiceId);


    @Query("""
            SELECT a 
            FROM AlertsContent a 
            WHERE a.serviceId = :serviceId
              AND a.msgMonth = :month
              AND a.msgDay = :day
              AND (:subServiceId IS NULL OR :subServiceId = '' OR a.subServiceId = :subServiceId)
              AND (:language IS NULL OR :language = '' OR a.language = :language)
              AND a.sendingTime > :currentTime
            ORDER BY a.sendingTime ASC
        """)
        List<AlertsContent> findNamazMessages(
                @Param("serviceId") String serviceId,
                @Param("month") int month,
                @Param("day") int day,
                @Param("subServiceId") String subServiceId,
                @Param("language") String language,
                @Param("currentTime") Time currentTime
        );
    
    
    @Query("""
            SELECT a 
            FROM AlertsContent a
            WHERE a.serviceId = :serviceId
              AND (:subServiceId IS NULL OR :subServiceId = '' OR a.subServiceId = :subServiceId)
              AND (:language IS NULL OR :language = '' OR a.language = :language)
              AND a.sendingTime > :currentTime
            ORDER BY a.sendingTime ASC
        """)
        List findServiceAlertMessages(
                @Param("serviceId") String serviceId,
                @Param("subServiceId") String subServiceId,
                @Param("language") String language,
                @Param("currentTime") Time currentTime
        );
    
    @Query("""
            SELECT new com.rakesh.sms.beans.AlertServiceDetails(a.serviceId, a.subServiceId)
            FROM AlertsContent a
            GROUP BY a.serviceId, a.subServiceId
        """)
        List<AlertServiceDetails> findServicesWithSMSContent();
    
    
    @Modifying
    @Transactional
    @Query("DELETE FROM AlertsContent a WHERE a.serviceId = :serviceid AND a.subServiceId = :subserviceid")
    int deleteByServiceIdAndSubServiceId(@Param("serviceid") String serviceid,
                                         @Param("subserviceid") String subserviceid);

    @Modifying
    @Transactional
    @Query("DELETE FROM AlertsContent a WHERE a.serviceId = :serviceid AND (a.subServiceId IS NULL OR a.subServiceId = '')")
    int deleteByServiceIdAndNoSubService(@Param("serviceid") String serviceid);
}
