package com.backend.wear.dto;


import com.backend.wear.entity.Category;
import com.backend.wear.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductPostRequestDto {
    //상품 등록시 요청 dto

    //상품 이름, 제목
    private String productName;
    //상품 가격
    private Integer price;
    //상품 이미지
    private String productImage;
    //상품 내용, 설명
    private String productContent;
    //상품 상태
    private String productStatus;
    //거래 장소
    private String place;
    //판매자
    private User user;
    //카테고리
    private Long categoryId;
}
