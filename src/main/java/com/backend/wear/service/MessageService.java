package com.backend.wear.service;

import com.backend.wear.dto.chat.ChatMessageDto;
import com.backend.wear.entity.ChatMessage;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.entity.User;
import com.backend.wear.repository.ChatMessageRepository;
import com.backend.wear.repository.ChatRoomRepository;
import com.backend.wear.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private static Logger log = LoggerFactory.getLogger(MessageService.class);
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final UserRepository userRepository;

    @Autowired
    public MessageService(ChatMessageRepository chatMessageRepository,
                          ChatRoomRepository chatRoomRepository,
                          UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository=userRepository;
    }

    public Long getPartnerId(Long chatRoomId, Long userId, String userType){
        // 채팅방 찾기
        ChatRoom chatRoom=chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못하였습니다."));

        User partner;
        Long partnerId;

        // 내가 구매자일경우
        if(userType.equals("customer")){
            partner=chatRoomRepository.findByChatRoomIdAndUserIdSellerChatRoom(chatRoomId,userId)
                    .orElseThrow(() -> new IllegalArgumentException("판매자를 찾지 못하였습니다."));
            partnerId=partner.getId();
        }

        // 내가 판매자일경우
        else{
            partner=chatRoomRepository.findByChatRoomIdAndUserIdCustomerChatRoom(chatRoomId,userId)
                    .orElseThrow(() -> new IllegalArgumentException("판매자를 찾지 못하였습니다."));
            partnerId=partner.getId();
        }

        return partnerId;
    }

    @Transactional
    public void saveMessage(Long chatRoomId, Long userId, ChatMessageDto.MessageDetailDto dto) {
        // 채팅방 찾기
        ChatRoom chatRoom=chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못하였습니다."));

        // 보낸사람 찾기
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("메시지를 보낸 사용자를 찾지 못하였습니다."));

        ChatMessage chatMessage=ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(dto.getContent())
                .senderId(dto.getSenderId())
                .userType(dto.getUserType())
                .build();

        chatMessageRepository.save(chatMessage);
    }
}