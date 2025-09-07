package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.MtResponse;

@Repository
public interface MtResponseRepository extends JpaRepository<MtResponse, Integer> {
    List<MtResponse> findByTid(String tid);
}