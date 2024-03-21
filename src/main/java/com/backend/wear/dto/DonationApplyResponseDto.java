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
public class DonationApplyResponseDto {

  //  @JsonProperty(value="id")
    private Long id; //기부 pk

  //  @JsonProperty(value="date")
    private String date;

//    @JsonProperty(value="clothes_count")
    private Integer clothesCount; //의류 개수

//    @JsonProperty(value="fashion_count")
    private Integer fashionCount; //잡화 개수

    private Boolean isDonationComplete; //기부 상태 (true, false)
}