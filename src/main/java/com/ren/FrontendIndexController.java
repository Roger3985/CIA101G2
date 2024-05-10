package com.ren;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/frontend")
public class FrontendIndexController {

    /**
     * 前往前台首頁
     *
     * @return forward to frontend index
     */
    @GetMapping("/index")
    public String toFrontendIndex(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        for (var cookie : cookies) {
            System.out.println("cookieName: " + cookie.getName() + ", cookieValue: " + cookie.getValue());
        }
        return "frontend/index";
    }

//    @GetMapping("/member/loginMember")
//    public String login() {
//        return "frontend/member/loginMember";
//    }
}
