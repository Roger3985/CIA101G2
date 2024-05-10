package com.ren.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class CookieListener implements HttpSessionAttributeListener {
    // 預計使用來確認登入狀況

    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        System.out.println("資料好像被放進去囉");
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