package com.rakesh.sms.jpas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakesh.sms.entity.PromotionMsisdn1;

@Repository
public interface PromotionMsisdnRepository extends JpaRepository<PromotionMsisdn1, Long> {
    List<PromotionMsisdn1> findByPromotionNameAndStatus(String promoName, String status);
    List<PromotionMsisdn1> findByPromotionNameAndMsisdnIn(String promoName, List<String> msisdns);
    
    @Query("SELECT MAX(p.id) FROM PromotionMsisdn1 p")
    Optional<Long> findMaxId();
    
    
    @Transactional
    @Modifying
    @Query("UPDATE PromotionMsisdn1 p SET p.status = :status " +
           "WHERE p.promotionName = :promoName AND p.msisdn IN :msisdns")
    int updateStatusForUsers(@Param("promoName") String promoName,
                             @Param("msisdns") List<String> msisdns,
                             @Param("status") String status);

}