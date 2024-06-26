package com.backend.wear.config;

import com.backend.wear.config.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 메시지를 주고 받으면서 메시지 전송과 관련한 추가 로직을 처리할 때 사용
@RequiredArgsConstructor

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선순위 올리기
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private static final String BEARER_PREFIX = "Bearer ";

    // 메시지가 채널로 전송되기 전 호출되는 메서드
    // 프런트 -> 서버로 메시지가 전송될 때
    // 메시지 전송 전에 인증 토큰을 검증하거나, 메시지 내용을 필터링 가능
    @Override
    @Transactional
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("full message:" + message);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));

        // stomp 연결시 jwt 검사 수행
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())){

            log.info("preSend 메서드에서 stomp connect 연결 시 토큰 검사");

            // 헤더에서 토큰 얻기
            List<String> authorizationHeaders = headerAccessor.getNativeHeader("Authorization");

            String authorizationHeader = (authorizationHeaders != null && !authorizationHeaders.isEmpty()) ? authorizationHeaders.get(0) : null;

            if (authorizationHeader == null || authorizationHeader.equals("null")) {
                log.info("헤더에 토큰 없음");
                return buildErrorMessage(headerAccessor.getSessionId(), "토큰이 없습니다");
            }

            String token = authorizationHeader.substring(BEARER_PREFIX.length());

            // 토큰 인증
            try {
                jwtUtil.validateToken(token);
            } catch (ExpiredJwtException e) { // 토큰 만료시
                log.info("Expired JWT Token", e);
                return buildErrorMessage(headerAccessor.getSessionId(), "AccessToken 만료");
            } catch (MalformedJwtException e) {
                log.info("Malformed JWT Token", e);
                return buildErrorMessage(headerAccessor.getSessionId(), "MalformedJwtException 예외");
            } catch (Exception e) {
                log.info("Token validation error", e);
                return buildErrorMessage(headerAccessor.getSessionId(), "Token validation error");
            }
        }

        return message;
    }

    private Message<?> buildErrorMessage(String sessionId, String errorMessage) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setMessage(errorMessage);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("access", false);
        responseBody.put("message", errorMessage);
        responseBody.put("timestamp", Instant.now().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(responseBody);
        } catch (JsonProcessingException e) {
            jsonResponse = "{\"access\":false,\"message\":\"JSON processing error\",\"timestamp\":\"" + Instant.now().toString() + "\"}";
        }

        return MessageBuilder.withPayload(jsonResponse)
                .setHeaders(headerAccessor)
                .build();
    }
}