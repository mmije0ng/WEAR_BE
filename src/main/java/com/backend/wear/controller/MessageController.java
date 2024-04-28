package com.backend.wear.controller;

import com.backend.wear.dto.chat.ChatMessageResponseDto;
import com.backend.wear.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins={"http://43.201.189.171:8080", "http://localhost:5173",
        "http://wear-frontend.s3-website.ap-northeast-2.amazonaws.com","http://localhost:8080"})
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final MessageService messageService;
    private static Logger log = LoggerFactory.getLogger(MessageController.class);

    // 메시지 보내기
    // pub/api/chat/message
    @MessageMapping("/api/chat/message")
    public void sendMessage (ChatMessageResponseDto dto, SimpMessageHeaderAccessor accessor) {

        //sub/api/chat/room/{roomId}

        log.info("채팅방 아이디: "+dto.getChatRoomId());
        log.info("보낸사람 아이디: "+dto.getSenderId());
        log.info("메시지 내용: "+dto.getContent());

        // 메시지 저장 로직

        // 구독자들에게 메시지 전달
        sendingOperations.convertAndSend("/sub/api/chat/room/"+dto.getChatRoomId(), dto);
    }
}