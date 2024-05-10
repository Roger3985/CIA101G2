package com.ren.config;

import com.ren.listener.InitializerListener;
import com.ren.listener.OnlineUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Filter、Listener原本需要透過web.xml來註冊，之後Javax新增在類別上加上@WebListener、@WebFilter的註解來直接註冊，
 * 這兩種方式都是將其作為Servlet管理的元件，無法使其受Spring容器管理，因此需要使用Config來註冊，以下為Listener的註冊Config
 */
@Configuration
public class ListerConfig {

    @Autowired
    private OnlineUsers onlineUsers;

    @Autowired
    private InitializerListener initializerListener;

    @Bean
    public ServletListenerRegistrationBean<OnlineUsers> registrationOnlineUsers() {
        return new ServletListenerRegistrationBean<>(onlineUsers);
    }

    @Bean
    public ServletListenerRegistrationBean<InitializerListener> registrationInitializerListener() {
        return new ServletListenerRegistrationBean<>(initializerListener);
    }
}
