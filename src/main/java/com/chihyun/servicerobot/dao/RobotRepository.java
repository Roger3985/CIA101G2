package com.chihyun.servicerobot.dao;

import com.chihyun.servicerobot.entity.ServiceRobot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("robotRepository")
public interface RobotRepository extends JpaRepository<ServiceRobot, Integer > {

    Set<ServiceRobot> findByKeywordNameContainingIgnoreCase(String keywordName);

}
