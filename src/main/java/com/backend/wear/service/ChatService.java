package com.backend.wear.service;

import com.backend.wear.dto.ConvertTime;
import com.backend.wear.dto.chat.ChatMessageResponseDto;
import com.backend.wear.dto.chat.ChatRoomResponseDto;
import com.backend.wear.dto.product.ProductResponseDto;
import com.backend.wear.entity.ChatMessage;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
import com.backend.wear.repository.ChatMessageRepository;
import com.backend.wear.repository.ChatRoomRepository;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

@Service
public class ChatService {
    private static final int pageSize=12;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepository,
                       ChatMessageRepository chatMessageRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository) {

        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository=chatMessageRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // JSON 문자열을 String[]으로 변환하는 메서드
    private  String[] convertImageJsonToArray(String productImageJson) {
        try {
            return objectMapper.readValue(productImageJson, String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    // 채팅방 생성하기
    // customerId == userId
    @Transactional
    public ChatRoomResponseDto.CreatedDto createdChatRoom(Long productId, Long customerId) throws Exception{
        // 상품 찾기
        Product product  = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "상품을 찾지 못하였습니다."));

        // 구매자 찾기
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException(
                "구매자를 찾지 못하였습니다."));

        // 판매자 찾기
        Long sellerId=product.getUser().getId();
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "판매자를 찾지 못하였습니다."));

        ChatRoom chatRoom;
        boolean isCreated=false;

        // 기존에 채팅방이 있는 경우
        if(chatRoomRepository.findByProductIdAndCustomerIdAndSellerId(productId,customerId,sellerId).isPresent()){
            chatRoom=chatRoomRepository.findByProductIdAndCustomerIdAndSellerId(productId,customerId,sellerId).get();
        }
        else {
            // 채팅방이 없는 경우 채팅방 생성
            chatRoom=ChatRoom.builder()
                    .product(product)
                    .customer(customer)
                    .seller(seller)
                    .build();

            chatRoomRepository.save(chatRoom);
            isCreated=true;
        }

        return ChatRoomResponseDto.CreatedDto.builder()
                .chatRoomId(chatRoom.getId())
                .isCreated(isCreated)
                .build();
    }

    // 채팅방 입장
    @Transactional
    public ChatRoomResponseDto.DetailDto enterChatRoom(Long chatRoomId, Long userId) throws Exception{
        // 사용자자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "사용자를 찾지 못하였습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "채팅방을 찾지 못하였습니다."));

        // 로그인한 사용자 타입
        String userType;
        // 구매자인 경우
        if(Objects.equals(chatRoom.getCustomer().getId(), userId))
           userType="customer";
        else
            userType="seller";

        String[] productImageArray = convertImageJsonToArray(chatRoom.getProduct().getProductImage());
        String[] customerProfileImageArray = convertImageJsonToArray(chatRoom.getCustomer().getProfileImage());
        String[] sellerProfileImageArray = convertImageJsonToArray(chatRoom.getSeller().getProfileImage());

        // 채팅방 메시지 내역
        List<ChatMessage> chatMessage = chatMessageRepository.findByChatRoomId(chatRoomId);

        // 판매자가 보낸 채팅 메시지, 시간
        List<ChatMessageResponseDto.MessageDto> sellerMessageList = chatMessage.stream()
                .filter(c -> c.getUserType().equals("customer"))
                .map(c -> ChatMessageResponseDto.MessageDto.builder()
                        .content( c.getContent())
                        .sendTime(ConvertTime.convertChatLocaldatetimeToTime(c.getCreatedAt()))
                        .build())
                .toList();

        // 구매자가 보낸 채팅 메시지, 시간
        List<ChatMessageResponseDto.MessageDto> customerMessageList = chatMessage.stream()
                .filter(c -> !c.getUserType().equals("customer"))
                .map(c -> ChatMessageResponseDto.MessageDto.builder()
                        .content( c.getContent())
                        .sendTime(ConvertTime.convertChatLocaldatetimeToTime(c.getCreatedAt()))
                        .build())
                .toList();

        return ChatRoomResponseDto.DetailDto.builder()
                .chatRoomId(chatRoomId)
                .productId(chatRoom.getProduct().getId())
                .productImage(productImageArray)
                .productName(chatRoom.getProduct().getProductName())
                .price(chatRoom.getProduct().getPrice())
                .sellerId(chatRoom.getSeller().getId())
                .sellerNickName(chatRoom.getSeller().getNickName())
                .sellerProfileImage(sellerProfileImageArray)
                .sellerLevel(chatRoom.getSeller().getLevel().getLabel())
                .sellerMessageList(sellerMessageList)
                .customerId(chatRoom.getCustomer().getId())
                .customerNickName(chatRoom.getCustomer().getNickName())
                .customerProfileImage(customerProfileImageArray)
                .customerLevel(chatRoom.getCustomer().getLevel().getLabel())
                .customerMessageList(customerMessageList)
                .userType(userType)
                .build();
    }

    // 모든 채팅방 조회
    @Transactional
    public Page <ChatRoomResponseDto.ScreenDto> findAllChatRooms(Long userId, Integer pageNumber) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Page<ChatRoom> chatRoomsPage = chatRoomRepository.findByUserId(userId, pageRequest(pageNumber));
        if(chatRoomsPage.isEmpty())
            throw new IllegalArgumentException("채팅 내역이 없습니다.");

        return chatRoomsPage.map(chatRoom -> mapToScreenDto(chatRoom, userId));
    }

    private ChatRoomResponseDto.ScreenDto mapToScreenDto(ChatRoom chatRoom, Long userId){

        // 채팅 상대방
        User partner;

        // 내가 구매자인지 판매자인지에 따라 상대방과 메시지를 설정
        if (Objects.equals(chatRoom.getCustomer().getId(), userId)) {
            partner = chatRoom.getSeller();
        } else {
            partner = chatRoom.getCustomer();
        }

        // 가장 마지막에 보낸 메시지
        ChatMessage lastMessage;
        ChatMessageResponseDto.MessageDto messageDto;
        if(!chatRoom.getMessageList().isEmpty()){
            lastMessage = chatRoom.getMessageList().get(chatRoom.getMessageList().size()-1);
            messageDto = ChatMessageResponseDto.MessageDto.builder()
                    .content(lastMessage.getContent())
                    .sendTime(ConvertTime.convertChatLocaldatetimeToTime(lastMessage.getCreatedAt()))
                    .build();
        }

        else{
            messageDto = ChatMessageResponseDto.MessageDto.builder()
                    .content(null)
                    .sendTime(null)
                    .build();
        }

        // 상품 이미지 JSON 배열 파싱
        String[] productImageArray = convertImageJsonToArray(chatRoom.getProduct().getProductImage());

        // DTO 생성
        return ChatRoomResponseDto.ScreenDto.builder()
                .chatRoomId(chatRoom.getId())
                .productImage(productImageArray)
                .chatPartnerId(partner.getId())
                .chatPartnerNickName(partner.getNickName())
                .chatPartnerLevel(partner.getLevel().getLabel())
                .messageInfo(messageDto)
                .build();
    }

    private Pageable pageRequest(Integer pageNumber){
        return
                PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
    }
}