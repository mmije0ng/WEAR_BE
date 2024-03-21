package com.backend.wear.service;

import com.backend.wear.dto.ChatRoomDto;
import com.backend.wear.dto.ChatRoomIdDto;
import com.backend.wear.entity.ChatRoom;
import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
import com.backend.wear.repository.ChatRoomRepository;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepository,
                       ProductRepository productRepository,
                       UserRepository userRepository){

        this.chatRoomRepository = chatRoomRepository;
        this.productRepository=productRepository;
        this.userRepository=userRepository;
    }

    //채팅방 생성
    public  ChatRoomIdDto createRoom(Long productId, Long customerId) {

        //상품
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        //구매자
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다."));

        //판매자
        User seller = productRepository.findById(productId).get().getUser();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setCustomer(customer);
        chatRoom.setSeller(seller);
        chatRoom.setProduct(product);

        chatRoomRepository.save(chatRoom);

        ChatRoomIdDto dto =ChatRoomIdDto.builder()
                .chatRoomId(chatRoom.getId())
                .build();

        return dto;
    }

    //채팅방 입장
    public ChatRoomDto chatRoom(Long roomId, Long productId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 내역이 없습니다."));

        Product product=productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("채팅 내역이 없습니다."));

        User seller= chatRoom.getSeller();

        User customer = chatRoom.getCustomer();

        ChatRoomDto chatRoomDto=ChatRoomDto.builder()
                .chatRoomId(chatRoom.getId())
                .productId(product.getId())
                .productImage(product.getProductImage())
                .productName(product.getProductName())
                .price(product.getPrice())
                .sellerId(seller.getId())
                .sellerNickName(seller.getNickName())
                .sellerProfileImage(seller.getProfileImage())
                .sellerLevel(seller.getLevel().getLabel())
                .customerId(customer.getId())
                .customerNickName(customer.getNickName())
                .customerProfileImage(customer.getProfileImage())
                .customerLevel(customer.getLevel().getLabel())
                .build();

        return chatRoomDto;
    }

    //채팅방 불러오기
    public List <ChatRoom> findAllRoom(Long userId) {

        List<ChatRoom> chatRoomList = chatRoomRepository
                .findBySellerIdOrCustomerId(userId,userId);

        return chatRoomList;
    }
}
