package com.backend.wear.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDtoTemp {
    //채팅방 아이디
    private Long chatRoomId;

    //보낸사람 pk
    private Long senderId;

    //보낸사람 닉네임
    private String senderNickName;

    //보낸 사람 프로필 사진
          private String[] profileImage;

    //채팅 메시지
    private String content;

    //보낸시간
    private String sendTime;

    //보낸 사람 타입
    // customer, seller
    private String userType;

    // 내가 보냈는지 여부
    @JsonProperty("is_mine")
    private boolean isMine;
}
