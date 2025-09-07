package com.rakesh.sms.jpas;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.SMSPromotion1;

@Repository
public interface SMSPromotionRepository extends JpaRepository<SMSPromotion1, Long> {
    List<SMSPromotion1> findByStatusAndStartDateTimeLessThanEqualOrStatusAndExpiryDateTimeGreaterThanEqual(
        String status1, Date startDate, String status2, Date expiryDate
    );

    SMSPromotion1 findByPromotionName(String promoName);
}
