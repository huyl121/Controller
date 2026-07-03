package com.example.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有来源（包括 https://www.okx.com）
                .allowedOriginPatterns("*")
                // 允许所有请求方法（GET/POST/OPTIONS 等）
                .allowedMethods("*")
                // 允许所有请求头
                .allowedHeaders("*")
                // 允许发送 Cookie（如果你的接口需要登录态）
                .allowCredentials(true)
                // 预检请求的缓存时间（秒）
                .maxAge(3600);
    }
}
