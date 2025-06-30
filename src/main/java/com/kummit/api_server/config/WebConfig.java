package com.kummit.api_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4173/") // 프론트 주소
                .allowedMethods("GET", "POST", "PATCH")
                .allowCredentials(true) // ★ 이거 중요: 쿠키 전송 허용
                .allowedHeaders("*");
    }
}