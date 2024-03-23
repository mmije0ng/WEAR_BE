package com.backend.wear.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRoomProfileDto {

    private Long chatRoomId; //채팅방 pk

    private Long productId; //상품 pk

    private String userNickName; //닉네임
    private String userProfileImage;
    private String userLevel;
}
