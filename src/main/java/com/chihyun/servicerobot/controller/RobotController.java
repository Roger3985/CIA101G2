package com.chihyun.servicerobot.controller;

import com.chihyun.servicerobot.entity.ServiceRobot;
import com.chihyun.servicerobot.model.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/robot")
public class RobotController {

    @Autowired
    RobotService robotService;

    @GetMapping("/reply")
    public Set<ServiceRobot> getRobotReply(String keywordName) {
        return robotService.getResponse(keywordName);
    }

}
