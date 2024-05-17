package com.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用於計算當前在線人數: 於ServletContextListener的contextInitialized方法內新增onlineUsers的屬性，
 * 用於記數在線使用者人數。
 * 1.當Session創建時，新增ServletContext的onlineUsers記數(+1)
 * 2.當Session註銷時，減少ServletContext的onlineUsers記數(-1)
 */
@Component
public class OnlineUsers implements HttpSessionListener {

    private static final String ONLINE_USERS = "onlineUsers";

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext context = se.getSession().getServletContext();
        AtomicInteger onlineUsers = (AtomicInteger) context.getAttribute(ONLINE_USERS);
        if (onlineUsers == null) {
            onlineUsers = new AtomicInteger(0);
            context.setAttribute(ONLINE_USERS, onlineUsers);
        }
        onlineUsers.incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext context = se.getSession().getServletContext();
        AtomicInteger onlineUsers = (AtomicInteger) context.getAttribute(ONLINE_USERS);
        if (onlineUsers != null && onlineUsers.get() > 0) {
            onlineUsers.decrementAndGet();
        }
    }
}
