package com.backend.wear.dto.login;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpRequestDto {
    private String userCreatedId;
    private String userPassword;
    private String userCheckPassword;
    private String userName;
    private String nickName;
    private String universityName;
    private String universityEmail;
    private List<String> styleNameList=new ArrayList<>();
}