package com.rakesh.sms.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.MatchContent;

@Repository
public interface MatchContentRepository extends JpaRepository<MatchContent, Long> {
    Optional<MatchContent> findByIdAndType(int id, String type);
Optional<MatchContent> findFirstByType(String type);
    
    // If you also want to filter by matchId:
    Optional<MatchContent> findFirstByIdAndType(int id, String type);
}