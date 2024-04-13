package com.backend.wear.dto.blockeduser;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BlockedUserResponseDto {
    Long blockedUserId; // 차단한 사용자 아이디
    String blockedUserNickName; // 차단한 사용자 닉네임
    String[] blockedUserProfileImage; // 차단한 사용자 프로픨 이미지
    String blockedUserLevel; // 차단한 사용자 레벨
}
