package com.chihyun.servicerecord.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontend/service")
public class ServiceFrontendController {

    @GetMapping("/frontendServiceChat")
    public String serviceChat(){
        return "frontend/service/frontendServiceChat";
    }

}
