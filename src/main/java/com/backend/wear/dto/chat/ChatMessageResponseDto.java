package com.backend.wear.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatMessageResponseDto {

    //보낸사람 pk
    Long senderId;

    //채팅방 아이디
    Long chatRoomId;

    //채팅 메시지
    String content;

    //보낸시간
    String sendTime;

    //메시지 보낸 사람 프로필 사진
    private String[] profileImage;

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    // 채팅방 생성 여부 dto
    public static class MessageDto{

        //채팅 메시지
        String content;

        //보낸시간
        String sendTime;
    }
}