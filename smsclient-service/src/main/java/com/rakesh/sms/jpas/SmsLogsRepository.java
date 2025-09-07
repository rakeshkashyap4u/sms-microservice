package com.rakesh.sms.jpas;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.CdrSmsLogs;
import com.rakesh.sms.entity.SmsLogs;

@Repository
public interface SmsLogsRepository extends JpaRepository<SmsLogs, Integer> {
    Optional<SmsLogs> findFirstByMessageIdAndAutotimestampAfter(String messageId, Date after);
    
    
    
    	
    	
    	
    	 List<SmsLogs> findBySenderOrderByMessageIdDesc(String sender);

}