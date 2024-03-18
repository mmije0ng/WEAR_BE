package com.backend.wear.dto;

import com.backend.wear.entity.Style;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//사용자에 대한 응답 dto
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String userName; //사용자 이름
    private String nickName;
    private String universityName;
    private List<Style> style; //스타일 태그
    private String profileImage;
    private String level;
    private String nextLevel;
    private Integer point;
    private Integer remainLevelPoint;
}
