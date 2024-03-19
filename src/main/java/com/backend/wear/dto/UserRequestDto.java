package com.backend.wear.dto;

import com.backend.wear.entity.Style;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRequestDto {
    private Long id; //pk
    private String userName; //사용자 이름
    private String nickName;
    private  String profileImage;
    private StyleDto style; //스타일 태그
}