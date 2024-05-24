package com.chihyun.servicerecord.controller;

import com.roger.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/frontend/service")
public class ServiceFrontendController {

    @GetMapping("/frontendServiceChat")
    public String serviceChat(HttpSession session){
        Member myData = (Member) session.getAttribute("loginsuccess");
        session.setAttribute("member", myData);

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

    @GetMapping("/faq")
    public String faq(){
        return "/frontend/service/faq";
    }

}
