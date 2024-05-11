package com.backend.wear.dto.chat;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

//채팅방 dto
public class ChatRoomResponseDto {
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    // 채팅방 정보 dto
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

        //구매자 정보
        private Long customerId;
        private String customerNickName;
        private String[] customerProfileImage;
        private String customerLevel;

        private List<ChatMessageDto.MessageDetailInfoDto> messageInfoList;  // 메시지 정보 리스트


        // 현재 채팅방에서 나의 타입 (구매자인지 판매자인지)
        // seller, customer
        private String userType;
    }

    // 채팅방 생성 여부 dto
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CreatedDto{
        private Long chatRoomId;
        private Boolean isCreated;
    }

    // 나의 채팅 내역 dto
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScreenDto{
        private Long chatRoomId; //채팅방 아이디
        private String[] productImage; //상품 이미지
        private Long chatOtherId; //채팅 상대방 아이디
        private String chatOtherNickName;
        private String chatOtherLevel;
        private ChatMessageDto.MessageScreenInfoDto messageInfo;  // 마지막으로 보낸 메시지, 시간
    }
}
