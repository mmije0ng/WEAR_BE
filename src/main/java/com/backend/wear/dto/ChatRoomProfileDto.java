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

    private Long roomId; //채팅방 pk

    private Long productId;

    private String userNickName;
    private String userProfileImage;
    private String userLevel;
}
