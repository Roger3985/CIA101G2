package com.ren.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.ren.util.Constants.YES;

@Controller
@RequestMapping("/frontend/product")
public class ProductFrontEndController {

    // 前往商品瀏覽頁面
    @GetMapping("/selectProduct")
    public String toBrowse() {
        return "frontend/product/selectProduct";
    }

    // 加入購物車

    // 前往結帳畫面

    // 確認是否有無登入

    // 書籤的篩選功能

    // 複合查詢
    // 使用cookie追蹤瀏覽資訊
    @PostMapping()
    public void cookieAdd() {
//        Optional<Cookie> userCookie = Optional.ofNullable(((HttpServletRequest) req)
//                        .getCookies())
//                .flatMap(this::userCookie);
//        Cookie cookie = null;
//        // 確認使用者是否要自動登入
//        if (autoLogin == YES) {
//            String cookieName = administrator.getAdmNo().toString();
//            String cookieValue = administrator.getTitle().getTitleNo().toString();
//            cookie = new Cookie(cookieName, cookieValue);
//            cookie.setMaxAge(864000);
//            ((HttpServletResponse) res).addCookie(cookie);
//            System.out.println("我要Cookie");
//        }
    }
}
