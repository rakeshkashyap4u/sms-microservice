package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.LanguageSpecification;

@Repository
public interface LanguageSpecificationRepository extends JpaRepository<LanguageSpecification, Long> {
    List<LanguageSpecification> findAllByOrderByLidAsc();
}
