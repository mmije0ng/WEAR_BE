package com.backend.wear.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

 //   @JsonProperty(value="product_name")
    private String productName;

 //   @JsonProperty(value="product_status")
    private String productStatus;

  //  @JsonProperty(value="post_status")
    private String postStatus;

 //   @JsonProperty(value="product_image")
    private String productImage;

    private Boolean isSelected;

    private UserResponseDto seller; //판매자

//    @JsonProperty(value="product_content")
    private String productContent;

    private String place; //거래 장소

    private Boolean isPrivate;
}