package com.backend.wear.service;

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
    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public MessageService(ChatMessageRepository chatMessageRepository,
                          ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

//    @Transactional
//    public void saveMessage() {
//
//    }
}