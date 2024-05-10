//package com.ren.config;
//
//import com.ren.Interceptor.LoginInterceptor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class MyWebMvcConfigure implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
//        registration.addPathPatterns("/**");
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }
//}
