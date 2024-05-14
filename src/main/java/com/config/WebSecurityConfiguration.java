//package com.config;
//
//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableOAuth2Sso
//public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http
////                .csrf()
////                .disable()
////                .antMatcher("/**")
////                .authorizeRequests()
////                .antMatchers("/", "index.html", "/frontend/member/loginMember","/", "/js/**", "/images/**", "/fonts/**")
////                .permitAll()
////                .anyRequest()
////                .authenticated();
//        http
//                .csrf()
//                .disable()
//                .authorizeRequests()
//                // 靜態資源無需身份驗證
//                .antMatchers("/", "index.html", "/frontend/member/loginMember", "css/**","/js/**", "/images/**", "/fonts/**")
//                .permitAll()
//                // 其他請求需要身份驗證
//                .anyRequest()
//                .authenticated();
//    }
//}
