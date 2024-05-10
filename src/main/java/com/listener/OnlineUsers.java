package com.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class OnlineUsers implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext context = se.getSession().getServletContext();
        Integer onlineUsers = (Integer) context.getAttribute("onlineUsers");
        context.setAttribute("onlineUsers", onlineUsers + 1);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext context = se.getSession().getServletContext();
        Integer onlineUsers = (Integer) context.getAttribute("onlineUsers");
        context.setAttribute("onlineUsers", onlineUsers - 1);
    }
}
