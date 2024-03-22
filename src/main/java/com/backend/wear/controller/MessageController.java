package com.backend.wear.controller;

import com.backend.wear.entity.ChatMessage;
import com.backend.wear.repository.ChatRoomRepository;
import com.backend.wear.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins="http://43.201.189.171:8080")
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final MessageService messageService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    //메시지 보내기
    @MessageMapping("/api/chat/message")
    public void enter(ChatMessage message) {
        log.info(message.getMessage());

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            System.out.println(message.getSender().getNickName()+"님이 입장하였습니다.");
        }

        messageService.saveMessage(message);

        sendingOperations.convertAndSend("/sub/api/chat/room/"+message.getId(), message);
    }
}