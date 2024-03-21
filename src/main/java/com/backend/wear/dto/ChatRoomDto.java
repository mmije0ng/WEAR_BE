package com.backend.wear.dto;


import com.backend.wear.entity.ChatMessage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRoomDto {
    //채팅방 dto

    private Long chatRoomId; //pk

    private Long productId;
    private String productImage; //상품 이미지
    private String productName; //상품 이름
    private Integer price; //가격

    //판매자 dto
    private Long sellerId;
    private String sellerNickName;
    private String sellerProfileImage;
    private String sellerLevel;

    //구매자 dto
    private Long customerId;
    private String customerNickName;
    private String customerProfileImage;
    private String customerLevel;

    private List <ChatMessage> messageList;
}