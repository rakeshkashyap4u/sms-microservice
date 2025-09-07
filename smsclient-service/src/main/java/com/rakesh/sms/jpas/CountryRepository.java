package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    // By default, findAll() returns all records
    // We can add a method to return sorted list
    List<Country> findAllByOrderByIdAsc();
}

