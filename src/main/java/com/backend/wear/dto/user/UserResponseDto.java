package com.backend.wear.dto.user;

import com.backend.wear.entity.Style;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

//사용자에 대한 응답 dto
@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponseDto {

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MyPageDto{
        private String userName; //사용자 이름
        private String nickName;
        private String universityName;
        private List<String> style; //스타일 태그 이름 리스트
        private List<String> profileImage;
        private String level;
        private String nextLevel;
        private Integer point;
        private Integer remainLevelPoint;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProfileDto{
        private String userName; //사용자 이름
        private String nickName;
        private List<String> style; //스타일 태그 이름 리스트
        private List<String> profileImage;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InfoDto{
        private String userName;
        private String universityName;
        private String universityEmail;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProductUserDto{
        private Long id; // pk
        private String nickName;
        private List<String> profileImage;
        private String level;
    }
}