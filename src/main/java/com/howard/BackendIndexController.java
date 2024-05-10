package com.howard;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@Controller
@RequestMapping("/backend")
public class BackendIndexController {

    /**
     * 當點選側邊欄的 icon 與 Home 後會觸發這個方法回到後台首頁
     *
     * @return 回到後台首頁
     */
    @GetMapping("/index")
    public String toBackendIndex() {
        return "/backend/index";
    }

    // 註冊會員
    @GetMapping("/register")
    public String register() {

        return "";
    }

    // 前往登入頁面
    @GetMapping("/login")
    public String login() {

        return "";
    }

    // 登出
    @GetMapping("/logout")
    public String logout() {
        return "";
    }

    // 最新消息推播
//    public



}
