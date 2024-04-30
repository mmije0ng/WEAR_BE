package com.backend.wear.service;

import com.backend.wear.dto.ConvertTime;
import com.backend.wear.dto.chat.ChatMessageDto;
import com.backend.wear.entity.ChatMessage;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.entity.User;
import com.backend.wear.repository.ChatMessageRepository;
import com.backend.wear.repository.ChatRoomRepository;
import com.backend.wear.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    private static Logger log = LoggerFactory.getLogger(MessageService.class);
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    // String[]을 JSON 문자열로 변환하는 메서드
    private  String convertImageArrayToJSON(String[] imageArray) {
        try {
            return objectMapper.writeValueAsString(imageArray);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Array to JSON", e);
        }
    }

    @Autowired
    public MessageService(ChatMessageRepository chatMessageRepository,
                          ChatRoomRepository chatRoomRepository,
                          UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    // 채팅 상대방 아이디 찾기
    public Long getPartnerId(Long chatRoomId, Long userId, String userType) {
        // 채팅방 찾기
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못하였습니다."));

        User partner;
        Long partnerId;

        // 내가 구매자일경우
        if (userType.equals("customer")) {
            partner = chatRoomRepository.findByChatRoomIdAndUserIdSellerChatRoom(chatRoomId, userId)
                    .orElseThrow(() -> new IllegalArgumentException("판매자를 찾지 못하였습니다."));
            partnerId = partner.getId();
        }

        // 내가 판매자일경우
        else {
            partner = chatRoomRepository.findByChatRoomIdAndUserIdCustomerChatRoom(chatRoomId, userId)
                    .orElseThrow(() -> new IllegalArgumentException("판매자를 찾지 못하였습니다."));
            partnerId = partner.getId();
        }

        return partnerId;
    }

    // 메시지 저장
    @Transactional
    public void saveMessage(Long chatRoomId, Long userId, ChatMessageDto.ChatRoomMessageDto dto) throws Exception {
        // 채팅방 찾기
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못하였습니다."));

        // 보낸사람 찾기
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("메시지를 보낸 사용자를 찾지 못하였습니다."));

        // 메시지가 텍스트(String)일 경우 contentImage null
        // 메시지가 이미지(String[])일 경우 content는 null
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderId(dto.getSenderId())
                .userType(dto.getSenderType())
                .timestamp(dto.getTimestamp())
                .sendTime(ConvertTime.convertChatTimeStampToLocalDateTime(dto.getTimestamp()))
                .content(dto.getMessage() != null ? dto.getMessage() : null)
                .contentImage(dto.getMessageImage() != null ? convertImageArrayToJSON(dto.getMessageImage()) : null)
                .build();

        chatMessageRepository.save(chatMessage);

        log.info("저장된 sendTime: "+chatMessage.getSendTime());
    }
}
