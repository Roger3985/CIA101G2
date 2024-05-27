package com.chihyun.servicerecord.controller;

import com.roger.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/frontend/service")
public class ServiceFrontendController {

    @GetMapping("/frontendServiceChat")
    public String serviceChat(HttpSession session,
                              RedirectAttributes redirectAttributes){

        Member myData = (Member) session.getAttribute("loginsuccess");

        if (myData == null) {
            redirectAttributes.addAttribute("error","尚未登入，請先登入");
            session.setAttribute("location2", "/frontend/service/frontendServiceChat");
            return "redirect:/frontend/member/loginMember";
        } else {
            session.setAttribute("member", myData);
            session.removeAttribute("location2");

            return "frontend/service/frontendServiceChat";
        }

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
