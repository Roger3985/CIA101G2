package com.config;

import com.filter.backend.AutoLoginFilter;
import com.filter.backend.FunctionFilter;
import com.filter.backend.LoginStateFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

import static com.ren.util.Constants.*;

/**
 * Filter、Listener原本需要透過web.xml來註冊，之後Javax新增在類別上加上@WebListener、@WebFilter的註解來直接註冊，
 * 這兩種方式都是將其作為Servlet管理的元件，無法使其受Spring容器管理，因此需要使用Config來註冊，以下為Filter的註冊Config
 */
@Configuration
public class FilterConfig {

    @Autowired
    private AutoLoginFilter autoLoginFilter;

    @Autowired
    private LoginStateFilter loginStateFilter;

    @Autowired
    private FunctionFilter functionFilter;

    @Bean
    public FilterRegistrationBean<Filter> autoLoginFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(autoLoginFilter);
        registration.addUrlPatterns("/backend/*");
        registration.setOrder(FIRST_ORDER);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> loginStateFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(loginStateFilter);
        registration.addUrlPatterns("/backend/*");
        registration.setOrder(SECOND_ORDER);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> functionFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(functionFilter);
        registration.addUrlPatterns("/backend/*/add*", "/backend/*/update*", "/backend/*/delete*");
        registration.setOrder(THIRD_ORDER);
        return registration;
    }

}
