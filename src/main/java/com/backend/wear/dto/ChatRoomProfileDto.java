package com.backend.wear.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatRoomProfileDto {
//    private Long userId; //보낸 사람 pk
    private String userNickName;
    private String userProfileImage;
    private String userLevel;
    private String message;
    private String sendTime; //몇분전에 메시지 보냈는지
}