package com.config;

import com.filter.backend.AutoLoginFilter;
import com.filter.backend.FunctionFilter;
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

    @Autowired
    private AutoLoginMemberFilter autoLoginMemberFilter;

    @Autowired
    private LoginStateMemberFilter loginStateMemberFilter;

    /**
     * 註冊自動登入Filter到Spring容器
     *
     * @return 返回註冊物件，後續將用於告訴Spring容器如何註冊與配置過濾器
     */
    @Bean
    public FilterRegistrationBean<Filter> autoLoginFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(autoLoginFilter);
        registration.addUrlPatterns("/backend/*");
        registration.setOrder(FIRST_ORDER);
        return registration;
    }

    /**
     * 註冊登入狀態Filter到Spring容器
     *
     * @return 返回註冊物件，後續將用於告訴Spring容器如何註冊與配置過濾器
     */
    @Bean
    public FilterRegistrationBean<Filter> loginStateFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(loginStateFilter);
        registration.addUrlPatterns("/backend/*");
        registration.setOrder(SECOND_ORDER);
        return registration;
    }

    /**
     * 註冊權限Filter到Spring容器
     *
     * @return 返回註冊物件，後續將用於告訴Spring容器如何註冊與配置過濾器
     */
    @Bean
    public FilterRegistrationBean<Filter> functionFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(functionFilter);
        registration.addUrlPatterns("/backend/administrator/addAdministrator", "/backend/administrator/updateAdministrator",
                "/backend/administrator/deleteAdministrator", "/backend/title/addTitle", "/backend/title/updateTitle", "/backend/title/deleteTitle");
        registration.setOrder(THIRD_ORDER);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> autoLoginMemberFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(autoLoginMemberFilter);
        registration.addUrlPatterns("/frontend/*");
        registration.setOrder(Constants.FIRST_ORDER);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> registration4() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>(loginStateMemberFilter);
        registration.addUrlPatterns("/frontend/*");
        registration.setOrder(Constants.SECOND_ORDER);
        return registration;
    }

}
