package com.backend.wear.dto;

import com.backend.wear.entity.EnvironmentLevel;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserPostResponseDto {
    //상품 상세 페이지에서 사용자 정보 dto

    private Long id; //사용자 아이디
    private String nickName; //사용자 닉네임
    private String profileImage; //프로필 이미지
    private EnvironmentLevel level; //환경 레벨
}