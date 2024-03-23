package com.backend.wear.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UnivCertRequestDto {
    private String universityEmail; //대학 이메일
    private String universityName; //대학 이름
    private boolean check;
    private int code; //인증코드

    //인증 요청
    public UnivCertRequestDto(String universityEmail, String universityName,
                              boolean check){
        this.universityEmail=universityEmail;
        this.universityName=universityName;
        this.check=check;
    }

    //인증 포함
    public UnivCertRequestDto(String universityEmail, String universityName,
                              int code){
        this.universityEmail=universityEmail;
        this.universityName=universityName;
        this.code=code;
    }
}
