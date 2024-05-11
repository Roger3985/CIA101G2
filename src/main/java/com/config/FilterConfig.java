package com.config;

import com.filter.backend.AutoLoginFilter;
import com.filter.backend.LoginStateFilter;
import com.filter.frontend.AutoLoginMemberFilter;
import com.filter.frontend.LoginStateMemberFilter;
import com.roger.member.dto.LoginStateMember;
import com.roger.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

import static com.ren.util.Constants.FIRST_ORDER;
import static com.ren.util.Constants.SECOND_ORDER;

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
    private AutoLoginMemberFilter autoLoginMemberFilter;

    @Autowired
    private LoginStateMemberFilter loginStateMemberFilter;

    @Bean
    public FilterRegistrationBean<Filter> registration1() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(autoLoginFilter);
        registration.addUrlPatterns("/backend/*");
        registration.setOrder(FIRST_ORDER);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> registration2() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(loginStateFilter);
        registration.addUrlPatterns("/backend/*");
        registration.setOrder(SECOND_ORDER);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> registration3() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(autoLoginMemberFilter);
        registration.addUrlPatterns("/frontend/member/*");
        registration.setOrder(Constants.FIRST_ORDER);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> registration4() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(loginStateMemberFilter);
        registration.addUrlPatterns("/frontend/member/*");
        registration.setOrder(Constants.SECOND_ORDER);
        return registration;
    }


}
