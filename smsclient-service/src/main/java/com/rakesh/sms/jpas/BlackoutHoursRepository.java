package com.rakesh.sms.jpas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.BlackoutHours;

@Repository
public interface BlackoutHoursRepository extends JpaRepository<BlackoutHours, Long> {
    // JpaRepository already provides findAll()
}
