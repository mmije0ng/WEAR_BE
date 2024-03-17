package com.backend.wear.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPostDto {
    //상품 상세 페이지에서 사용자 정보 dto

    private Long id; //사용자 아이디
    private String name; //사용자 이름
    private String profileImage; //프로필 이미지
    private String level; //환경 레벨
}
