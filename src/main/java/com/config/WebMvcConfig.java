package com.config;

import com.roger.converters.StringToDateConverter;
import com.roger.converters.StringToTimestampConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMVC 配置類，用於配置應用程式中的格式化和轉換器。
 * 通過實現 WebMvcConfigurer 介面，該類負責將自定義的轉換器添加到 Spring MVC 框架的格式化程序註冊表中。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加自定義的格式化程序和轉換器到格式化程序註冊表中。
     * 在這個方法中，將 StringToDateConverter 和 StringToTimestampConverter 添加到格式化程序註冊表。
     *
     * @param registry 用於註冊格式化程序和轉換器的註冊表。
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 將自定義 StringToDateConverter 轉換器添加到格式化程序註冊表中
        registry.addConverter(new StringToDateConverter());

        // 將自定義轉換器 StringToTimestampConverter 添加到格式化程序註冊表中
        registry.addConverter(new StringToTimestampConverter());
    }

}
