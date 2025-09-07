package com.rakesh.sms.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.CallbackDetails;

@Repository
public interface CallbackDetailsRepository extends JpaRepository<CallbackDetails, Integer> {
    Optional<CallbackDetails> findFirstByServiceidAndSubServiceidAndAction(String serviceid, String subServiceid, String action);
    Optional<CallbackDetails> findById(Integer id);
}