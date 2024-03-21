package com.backend.wear.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponseDto{

    //상품 조회 시 응답  dto
    private Long id;  //상품 아이디
    private Integer price;
    private String productName;
    private String productStatus;
    private String postStatus;
    private String productImage;
    @JsonProperty(value="is_selected")
    private boolean isSelected;

    private UserResponseDto seller; //판매자
    private String productContent;
    private String place; //거래 장소

    @JsonProperty(value="is_private")
    private boolean isPrivate;
}