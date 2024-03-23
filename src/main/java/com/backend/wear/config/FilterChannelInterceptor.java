package com.backend.wear.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class FilterChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println("full message:" + message);  
        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));
        System.out.println(headerAccessor.getHeader("nativeHeaders").getClass());

        System.out.println(message.getClass());

        String messageContent = headerAccessor.getNativeHeader("message").get(0);
        String chatRoomId = headerAccessor.getNativeHeader("chat_rood_id").get(0); // 오타 수정

        // 추출한 값들을 사용하여 필요한 작업 수행
        System.out.println("Message: " + messageContent);
        System.out.println("Chat Room ID: " + chatRoomId);


        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            System.out.println("msg: " + "conne");
        }
        //throw new MessagingException("no permission! ");
        return message;
    }
}