package com.backend.wear.dto;

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
public class DonationApplyResponseDto {
    private Long id; //기부 pk
    private String date;
    private Integer clothesCount; //의류 개수
    private Integer fashionCount; //잡화 개수
    private boolean isDonationComplete; //기부 상태 (true, false)
}