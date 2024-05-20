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

    @GetMapping("/policy")
    public String policy(){
        return "/frontend/service/policy";
    }

    @GetMapping("/contactus")
    public String contactus(){
        return "/frontend/service/contactus";
    }

}
