package com.backend.wear.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponseDto{

    // 상품 상세 dto
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DetailDto{
        private Long id;  //상품 아이디

        private UserResponseDto.ProductUserDto seller; //판매자

        private Integer price;

        private String productName;

        private String productStatus;

        private String postStatus;

        private String productContent;

        private List<String> productImage;

        private String place; //거래 장소

        private LocalDateTime createdAt; // 상품 등록 시간
    }

    // 썸네일
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ScreenDto{
        private Long id;  //상품 아이디

        private Integer price;

        private String productName;

        private String productStatus;

        private String postStatus;

        private List<String> productImage;

        private Boolean isSelected;

        private LocalDateTime createdAt;
    }

}