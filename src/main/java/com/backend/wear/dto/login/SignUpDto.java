package com.backend.wear.dto.login;

import com.backend.wear.entity.Style;
import com.fasterxml.jackson.annotation.JsonInclude;
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
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SignUpDto {

    private String userId;
    private String userPassword;
    private String userCheckPassword;
//    private String universityName;
//    private String universityEmail;
    private List<String> styleList=new ArrayList<>();
}