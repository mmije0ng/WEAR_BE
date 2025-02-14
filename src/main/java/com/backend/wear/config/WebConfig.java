package com.backend.wear.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Configuration
@EnableWebSocket
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com"
                        ) // 허용할 Origin 설정, 해당 도메인 요청의 경우에만 허용
                .allowedHeaders("*") // 요청을 허용할 헤더 설정
                .exposedHeaders("Authorization", "Authorization-Refresh")  // 응답 헤더 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드 명시
                .allowCredentials(true) // 자격 증명(쿠키, 인증 헤더 등)을 포함한 요청을 허용
                .maxAge(3600); // Preflight 요청 결과를 캐시하는 시간 (초)
    }

    @Bean
    public StompHandshakeInterceptor stompHandler() {
        return new StompHandshakeInterceptor();
    }
}