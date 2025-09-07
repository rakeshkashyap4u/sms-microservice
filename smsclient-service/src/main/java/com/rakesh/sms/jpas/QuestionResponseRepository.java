package com.rakesh.sms.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.QuestionResponse;

@Repository
public interface QuestionResponseRepository extends JpaRepository<QuestionResponse, Integer> {
	 @Query("SELECT q FROM QuestionResponse q WHERE q.user_id = :userId ORDER BY q.id DESC")
	    Optional<QuestionResponse> findTopByUserIdOrderByIdDesc(@Param("userId") String userId);


   
}