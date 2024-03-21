package com.backend.wear.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOrigins("*") // 모든 origin에서 접근 허용, 필요에 따라 수정 가능
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드 설정
                .allowedHeaders("Authorization", "Content-Type") // 허용할 헤더 설정
                .exposedHeaders("Custom-Header") // 노출할 헤더 설정
                .maxAge(3600); // Preflight 요청 결과를 캐시하는 시간 (초)
    }
}