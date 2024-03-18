package com.backend.wear.dto;

import com.backend.wear.entity.User;
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
public class ProductPostResponseDto {
    //상품 상세 dto
    //user 정보 포함

    private Long id; //상품 아이디
    private UserPostResponseDto seller; //판매자
    private Integer price;
    private String productName;
    private String productStatus; //상품 상태 (보통이에요 등)
    private String postStatus; //거래 상태 (판매 중, 완료)
    private String productContent;
    private String productImage;
    private String place; //거래 장소
}
