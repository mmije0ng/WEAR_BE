package com.backend.wear.service;

import com.backend.wear.controller.ChatRoomController;
import com.backend.wear.entity.ChatMessage;
import com.backend.wear.repository.ChatMessageRepository;
import com.backend.wear.repository.ChatRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private static Logger log = LoggerFactory.getLogger(MessageService.class);

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public MessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public void saveMessage(ChatMessage message) {
        // 메시지 저장 로직
        chatMessageRepository.save(message);
        log.info("메시지: "+message+" 저장 완료");
    }
}