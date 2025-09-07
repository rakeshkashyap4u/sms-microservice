package com.rakesh.sms.jpas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.MessageFormats;
import com.rakesh.sms.entity.QuestionResponse;

@Repository
public interface MessageFormatsRepository extends JpaRepository<MessageFormats, Integer> {
    List<MessageFormats> findByServiceCodeAndKeywordAndSubkey(String serviceCode, String keyword, String subkey);
    
 // Find formats for a service code matching either exact keyword/subkey or ANY/ANY
    List<MessageFormats> findByServiceCodeAndKeywordInAndSubkeyInOrderByIdDesc(
            String serviceCode, List<String> keywords, List<String> subkeys
    );
    
    Optional<MessageFormats> findFirstByServiceCode(String serviceCode);
    
    List<MessageFormats> findByKeywordAndSubkey(String keyword, String subkey);
    
   

}