package com.rakesh.sms.jpas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rakesh.sms.entity.GamingUser;

@Repository
public interface GamingUserRepository extends JpaRepository<GamingUser, Integer> {
    List<GamingUser> findByUseridAndQuestionid(String userid, String questionid);
}
