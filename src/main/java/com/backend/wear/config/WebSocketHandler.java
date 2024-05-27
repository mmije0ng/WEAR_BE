package com.backend.wear.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // synchronizedSet  :  동기화된 Set을 반환
    //  --> 멀티쓰레드 환경에서 하나의 컬렉션 요소에 여러 스레드가 접근하면 충돌이 발생할수있으므로
    //     동기화(충돌이 안나도록 줄세움 )를 진행

    private final Map<String, WebSocketSession> sessions
            = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        sessions.put(session.getId(), session);

        log.info("웹소켓 Connection established: " + session.getId());
        // 사용자 연결 처리 로직
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        System.out.println("웹소켓 Connection closed: " + session.getId());
        // 사용자 연결 해제 처리 로직
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지 처리 로직
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Transport error: " + session.getId() + ", " + exception.getMessage());
        // 오류 처리 로직
    }
}