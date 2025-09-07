package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.KPIReportDaily;

@Repository
public interface KPIReportDailyRepository extends JpaRepository<KPIReportDaily, Long> {
    List<KPIReportDaily> findByReportDateAndKeywordAndShortcode(String reportDate, String keyword, String shortcode);
}