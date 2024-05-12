package com.listener.backend;

import com.ren.administrator.dto.LoginState;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@Component
public class AdmListener implements HttpSessionAttributeListener {
    // 預計使用來確認登入狀況

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
//        System.out.println("資料好像被放進去囉");
//        HttpSession session = se.getSession();
//        LoginState loginState = (LoginState) session.getAttribute("loginState");
//        if () {
//
//        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        HttpSessionAttributeListener.super.attributeRemoved(se);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        HttpSessionAttributeListener.super.attributeReplaced(se);
    }
}