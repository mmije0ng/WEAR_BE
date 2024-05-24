package com.backend.wear.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

public class ChatMessageDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    // 메시지 보낼 때, 받을 때
    public static class ChatRoomMessageDto{
        //채팅방 아이디
        //    private Long chatRoomId;

        //보낸사람 pk
        private Long senderId;

        //보낸사람 닉네임
        private String senderNickName;

        //보낸 사람 프로필 사진
        private String[] profileImage;

        //채팅 메시지
        private String message;

        //채팅 메시지 - 사진
        private String[] messageImage;

        //보낸시간
        private String timestamp;

        // 내가 보낸건지 여부
        @JsonProperty("is_mine")
        boolean mine;

        //보낸 사람 타입
        // customer, seller
        private String senderType;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    // 메시지 리스트
    public static class MessageDetailInfoDto{

        // 보낸 사람 타입
        private String messageUserType;

        //채팅 메시지
        private String message;

        //채팅 메시지 - 사진
        private String[] messageImage;

        //보낸시간
        private String timestamp;

        //보낸 시간 날짜 포함
        private String sendDateTime;

        // 내가 보낸건지 여부
        @JsonProperty("is_mine")
        boolean mine;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    // 메시지 리스트
    public static class MessageScreenInfoDto{

        //채팅 메시지
        private String message;

        //채팅 메시지 - 사진
        private String[] messageImage;

        // 내가 보낸건지 여부
        @JsonProperty("is_mine")
        private boolean mine;

        //보낸시간
        //몇분 전
        private String time;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    // wear 메시지
    public static class WearMessageDto{
        // 상품 이름
        private String productName;

        //채팅 메시지
        private String message;

        //보낸 사람 타입
        // admin
        private String senderType;

        //보낸시간
        private String timestamp;

        // 내가 보낸건지 여부
        @JsonProperty("is_mine")
        boolean mine;
    }
}
