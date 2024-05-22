package com.chihyun.servicerobot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend/serviceRobot")
public class RobotFrontendController {

    @GetMapping("/chatRobot")
    public String gochatRobot() {
        return "frontend/serviceRobot/chatRobot";
    }
}
