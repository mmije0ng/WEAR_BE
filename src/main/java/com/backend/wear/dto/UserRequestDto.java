package com.backend.wear.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UserRequestDto {
    private Long id; //pk
    private String userName; //사용자 이름
    private String nickName;
    private  String profileImage;
    private List<String> style; //스타일 태그 이름 리스트
    private String universityEmail;
    private String universityName;

    //프로필 dto
    public UserRequestDto(String userName, String nickName,
                          String profileImage, List<String> style){
        this.userName=userName;
        this.nickName=nickName;
        this.profileImage=profileImage;
        this.style=style;
    }

    //info dto
    public UserRequestDto(String userName, String universityEmail, String universityName){
        this.userName=userName;
        this.universityEmail=universityEmail;
        this.universityName=universityName;
    }
}