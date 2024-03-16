package com.backend.wear.dto;

import com.backend.wear.entity.Wish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto{

    //상품 조회 시 응답  dto
    private Long id;
    private Integer price;
    private String productName;
    private String postStatus;
    private boolean isSelected;
    private String productImage;
}