package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.CdrSmsLogs;

@Repository
public interface CdrSmsLogsRepository extends JpaRepository<CdrSmsLogs, Long> {

    @Query(value = """
        SELECT receiver as msisdn, COUNT(*) as cnt
        FROM CdrSmsLogs
        WHERE DATE(submit_time) = :currentDate AND msg_type = 'MO'
        GROUP BY receiver
        ORDER BY cnt DESC
        LIMIT :mlimit
    """, nativeQuery = true)
    List<Object[]> findMostActiveUsers(@Param("currentDate") String currentDate, @Param("mlimit") int mlimit);
}
