package com.backend.wear.dto.university;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UniversityResponseDto { // 대학 순위 응답 dto
    private String date; //현재날짜
    private String time; //현재시간
    private List<UniversityInfoDto> universityList; //상위 5개 대학 정보
    private String firstUniversityName; //1위 대학 이름
    private String firstTotalPoint; //1위 대학 총 포인트
    private String firstProductCount; //1위 대학 거래횟수
    private String firstDonationCount; //1위 대학 기부횟수

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UniversityInfoDto{
        private String universityName;
        private String universityPoint;
        private String[] universityImage;
    }
}