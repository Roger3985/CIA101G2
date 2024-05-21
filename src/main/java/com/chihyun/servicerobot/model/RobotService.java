package com.chihyun.servicerobot.model;

import com.chihyun.servicerobot.dao.RobotRepository;
import com.chihyun.servicerobot.entity.ServiceRobot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("robotService")
public class RobotService {

    @Autowired
    RobotRepository robotRepository;

    public void addRobotReply(ServiceRobot serviceRobot){
//        TODO:不允許重複輸入相同值
        robotRepository.save(serviceRobot);
    }

    public void updateRobotReply(ServiceRobot serviceRobot){
        robotRepository.save(serviceRobot);
    }

    public Set<ServiceRobot> getResponse(String keywordName){
        return robotRepository.findByKeywordNameContainingIgnoreCase(keywordName);
    }

}
