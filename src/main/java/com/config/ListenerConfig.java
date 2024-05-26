package com.config;

import com.listener.InitializerListener;
//import com.listener.OnlineUsers;
import com.listener.backend.LoginStateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EventListener;

/**
 * Filter、Listener原本需要透過web.xml來註冊，之後Javax新增在類別上加上@WebListener、@WebFilter的註解來直接註冊，
 * 這兩種方式都是將其作為Servlet管理的元件，無法使其受Spring容器管理，因此需要使用Config來註冊，以下為Listener的註冊Config
 */
@Configuration
public class ListenerConfig {

//    @Autowired
//    private OnlineUsers onlineUsers;

    @Autowired
    private InitializerListener initializerListener;

    @Autowired
    private LoginStateListener loginStateListener;

//    @Bean
//    public ServletListenerRegistrationBean<EventListener> registrationOnlineUsers() {
//        return new ServletListenerRegistrationBean<>(onlineUsers);
//    }

    @Bean
    public ServletListenerRegistrationBean<EventListener> registrationInitializerListener() {
        return new ServletListenerRegistrationBean<>(initializerListener);
    }

    @Bean
    public ServletListenerRegistrationBean<EventListener> registrationLoginStateListener() {
        return new ServletListenerRegistrationBean<>(loginStateListener);
    }
}
