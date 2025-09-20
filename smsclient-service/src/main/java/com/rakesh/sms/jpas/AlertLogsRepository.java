package com.rakesh.sms.jpas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.AlertLogs;

@Repository
public interface AlertLogsRepository extends JpaRepository<AlertLogs, Integer> {
}
