package com.backend.wear.dto.chat;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

public class ChatRoomResponseDto {
    //채팅방 dto
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    // 채팅방 생성 여부 dto
    public static class DetailDto{
        private Long chatRoomId; //pk

        private Long productId;
        private String[] productImage; //상품 이미지
        private String productName; //상품 이름
        private Integer price; //가격

        //판매자 정보
        private Long sellerId;
        private String sellerNickName;
        private String[] sellerProfileImage;
        private String sellerLevel;
        private List<ChatMessageDto.MessageInfoDto> sellerMessageList;  // 메시지, 시간

        //구매자 정보
        private Long customerId;
        private String customerNickName;
        private String[] customerProfileImage;
        private String customerLevel;
        private List<ChatMessageDto.MessageInfoDto> customerMessageList;  // 메시지, 시간

        // 현재 채팅방에서 나의 타입 (구매자인지 판매자인지)
        // seller, customer
        private String userType;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    // 채팅방 생성 여부 dto
    public static class CreatedDto{
        Long chatRoomId;
        Boolean isCreated;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScreenDto{
        private Long chatRoomId; //채팅방 아이디
        private String[] productImage; //상품 이미지
        private Long chatPartnerId; //채팅 상대방 아이디
        private String chatPartnerNickName;
        private String chatPartnerLevel;
        private ChatMessageDto.MessageInfoDto messageInfo;  // 마지막으로 보낸 메시지, 시간
    }
}