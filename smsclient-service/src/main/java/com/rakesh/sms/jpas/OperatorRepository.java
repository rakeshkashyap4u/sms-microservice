package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rakesh.sms.entity.Operator;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
    List<Operator> findAllByOrderByIdAsc();
}
