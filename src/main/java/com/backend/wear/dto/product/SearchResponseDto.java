package com.backend.wear.dto.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchResponseDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RankDto{ //인기 검색 Dto
        List<String> searchNameRankList; //인기 검색어 이름 리스트
        String date; //스케줄링 날짜
        String time; //스케줄링 시간
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class UserDto{ //사용자 검색 Dto
        List<String> searchNameList;
    }
}
