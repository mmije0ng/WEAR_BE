package com.backend.wear.config;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// 메시지를 주고 받으면서 메시지 전송과 관련한 추가 로직을 처리할 때 사용
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class FilterChannelInterceptor implements ChannelInterceptor {

    // 메시지가 채널로 전송되기 전 호출되는 메서드
    // 프런트 -> 서버로 메시지가 전송될 때
    @Override
    @Transactional
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println(this.getClass().getName()+" 호출 완료");
        System.out.println("full message:" + message);

        // 메시지의 본문(body) 추출
        Object payload = message.getPayload();
        System.out.println("Message Payload: " + payload.getClass());;
        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));
        System.out.println(headerAccessor.getHeader("nativeHeaders").getClass());
        return message;
    }

//    // 발생한 예외에 관계없이 전송이 완료된 후 호출
//    // 클라이언트 -> 서버 메시지 전송 완료 후
//    @Override
//    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex){
//        System.out.println(this.getClass().getName()+" 호출 완료");
//
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//        System.out.println("full message:" + message);
//        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));
//        System.out.println(headerAccessor.getHeader("nativeHeaders").getClass());
//    }
}