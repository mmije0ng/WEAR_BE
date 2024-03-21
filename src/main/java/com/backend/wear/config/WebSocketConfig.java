package com.backend.wear.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // sockJS Fallback을 이용해 노출할 endpoint 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // 웹소켓이 handshake를 하기 위해 연결하는 endpoint
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    //메세지 브로커에 관한 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        // 토픽 구독 시 /sub 로 요청
        registry.enableSimpleBroker("/sub"); //구독
        // 도착 경로에 대한 prefix 설정
        registry.setApplicationDestinationPrefixes("/pub"); //메시지 송신
    }
}