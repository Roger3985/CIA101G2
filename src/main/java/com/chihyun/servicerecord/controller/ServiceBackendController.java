package com.chihyun.servicerecord.controller;

import javax.servlet.http.HttpSession;

import com.roger.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/backend/service")
public class ServiceBackendController {

    @GetMapping("/backendServiceChat")
    public String servierChat(HttpSession session,
                              ModelMap modelMap,
                              RedirectAttributes redirectAttributes) {


        return "/backend/service/backendServiceChat";
    }


}
