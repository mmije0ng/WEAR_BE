package com.backend.wear.dto.product;


import com.backend.wear.entity.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductPostRequestDto {
    // 상품 등록시 요청 dto

    // 상품 이름, 제목
    private String productName;
    // 상품 가격
    private Integer price;
    // 상품 이미지 (수정)
    private List<String> productImage; // 빈 리스트로 초기화
    // 상품 내용, 설명
    private String productContent;
    // 상품 상태
    private String productStatus;
    // 거래 장소
    private String place;
    // 판매자
    private User user;
    // 카테고리 이름
    private String categoryName;
}