package com.backend.wear.service;

import com.backend.wear.dto.ConvertTime;
import com.backend.wear.dto.chat.ChatMessageDto;
import com.backend.wear.dto.chat.ChatRoomResponseDto;
import com.backend.wear.entity.*;
import com.backend.wear.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ChatRoomService {
    private static final int PAGE_SIZE=12;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final BlockedUserRepository blockedUserRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository,
                           ChatMessageRepository chatMessageRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository,
                           BlockedUserRepository blockedUserRepository) {

        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository=chatMessageRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.blockedUserRepository=blockedUserRepository;
    }

    // JSON 문자열을 String[]으로 변환하는 메서드
    private  String[] convertImageJsonToArray(String productImageJson) {
        if(productImageJson==null) return null;
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
        // 보낸 순서대로 정렬
        List<ChatMessage> chatMessage = chatMessageRepository.findByChatRoomIdOrderBySendTimeAsc(chatRoomId);

        List<ChatMessageDto.MessageDetailInfoDto> messageInfoList = chatMessage.stream()
                .map(c -> {
                    String message = c.getContent();
                    String[] messageImage = convertImageJsonToArray(c.getContentImage());
                    return ChatMessageDto.MessageDetailInfoDto.builder()
                            .messageUserType(c.getUserType())
                            .message(message)
                            .messageImage(messageImage)
                            .timestamp(c.getTimestamp())
                            .sendDateTime(c.getSendTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")))
                            .mine(Objects.equals(c.getSenderId(), userId))  // 메시지를 보낸 사용자가 현재 사용자와 일치하지 않는 경우 false
                            .build();
                })
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

                .customerId(chatRoom.getCustomer().getId())
                .customerNickName(chatRoom.getCustomer().getNickName())
                .customerProfileImage(customerProfileImageArray)
                .customerLevel(chatRoom.getCustomer().getLevel().getLabel())

                .messageInfoList(messageInfoList)
                .userType(userType)
                .build();
    }

    // 모든 채팅방 조회
    @Transactional
    public Page <ChatRoomResponseDto.ScreenDto> findAllChatRooms(Long userId, Integer pageNumber) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        // 내간 차단한 유저 아이디 리스트
        List<Long> blockedUserIdList = blockedUserRepository.findByUserId(userId);
        for(Long id: blockedUserIdList){
            log.info("내가 차단한 유저 아이디 "+id);
        }

        // 나를 차단한 유저 아이디 리스트
        List<Long> userIdListBlocked = blockedUserRepository.findByUserIdBlocked(userId);
        for(Long id: userIdListBlocked){
            log.info("나를 차단한 유저 아이디 "+id);
        }

        // 사용자와의 채팅방
        Page<ChatRoom> chatRoomsPage = chatRoomRepository.findByUserId(userId, blockedUserIdList, userIdListBlocked, pageRequest(pageNumber));

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
        ChatMessageDto.MessageScreenInfoDto messageInfoDto;
        if(!chatRoom.getMessageList().isEmpty()){
            lastMessage = chatRoom.getMessageList().get(chatRoom.getMessageList().size()-1);

            Long senderId = lastMessage.getSenderId();
            boolean isMine=false;
            if(senderId.equals(userId))
                isMine=true;

            messageInfoDto = ChatMessageDto.MessageScreenInfoDto.builder()
                    .message(lastMessage.getContent())
                    .messageImage(convertImageJsonToArray(lastMessage.getContentImage()))
                    .mine(isMine)
                    .time(ConvertTime.convertLocalDatetimeToTime(lastMessage.getSendTime()))
                    .build();
        }

        else{
            messageInfoDto = ChatMessageDto.MessageScreenInfoDto.builder()
                    .message(null)
                    .messageImage(null)
                    .time(null)
                    .build();
        }

        // 상품 이미지 JSON 배열 파싱
        String[] productImageArray = convertImageJsonToArray(chatRoom.getProduct().getProductImage());

        // DTO 생성
        return ChatRoomResponseDto.ScreenDto.builder()
                .chatRoomId(chatRoom.getId())
                .productImage(productImageArray)
                .chatOtherId(partner.getId())
                .chatOtherNickName(partner.getNickName())
                .chatOtherLevel(partner.getLevel().getLabel())
                .messageInfo(messageInfoDto)
                .build();
    }

    // 사용자 차단하기
    @Transactional
    public void blockedChatUser(Long chatRoomId, Long userId) throws Exception{
        // 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "사용자를 찾지 못하였습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "채팅방을 찾지 못하였습니다."));

        // 로그인한 사용자 타입에 따라 채팅 상대방 차단
        String userType = Objects.equals(chatRoom.getCustomer().getId(), userId) ? "customer" : "seller";
        User blockedUser = Objects.equals(userType, "customer") ?
                chatRoomRepository.findByChatRoomIdAndCustomerIdBySeller(chatRoomId, userId).orElseThrow(() -> new IllegalArgumentException("차단하기 위한 판매자를 찾지 못하였습니다.")) :
                chatRoomRepository.findByChatRoomIdAndSellerIdByCustomer(chatRoomId, userId).orElseThrow(() -> new IllegalArgumentException("차단하기 위한 구매자를 찾지 못하였습니다."));

        BlockedUser block = BlockedUser.builder()
                .user(user)
                .blockedUserId(blockedUser.getId())
                .build();

        // 사용자 차단
        blockedUserRepository.save(block);

        log.info("채팅 차단한 사용자 아이디: "+ blockedUser.getId());
    }

    @Transactional
    public void deleteChatRoom(Long chatRoomId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾지 못하였습니다."));

        chatRoomRepository.delete(chatRoom);
    }

    private Pageable pageRequest(Integer pageNumber){
        return
                PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("createdAt").descending());
    }
}