package com.chihyun.servicerecord.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/backend/service")
public class ServiceBackendController {

    @GetMapping("/backendServiceChat")
    public String servierChat(){
        return "/backend/service/backendServiceChat";
    }

}
