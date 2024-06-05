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
                .allowedOrigins("http://localhost:8080", "http://localhost:5173",
                        "http://43.201.189.171:8080",
                        "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com"
                        ) // 허용할 Origin 설정
              //  .allowedOrigins("**")
          //      .allowedHeaders("Authorization")
                     .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","HEAD") // 허용할 HTTP 메서드 설정
                .allowCredentials(true)
                .maxAge(3600); // Preflight 요청 결과를 캐시하는 시간 (초)
    }

    @Bean
    public StompHandshakeInterceptor stompHandler() {
        return new StompHandshakeInterceptor();
    }
}