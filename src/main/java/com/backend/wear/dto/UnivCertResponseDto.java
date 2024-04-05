package com.backend.wear.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UnivCertResponseDto {

    private String universityEmail; //대학 이메일
    private String universityName; //대학 이름
    private Boolean success;

    public static UnivCertResponseDto fromLoginDto(SignUpDto signUpDto) {
        UnivCertResponseDto dto = new UnivCertResponseDto();
        // Copy fields from LoginDto to UnivCertRequestDto
        BeanUtils.copyProperties(signUpDto, dto);
        return dto;
    }
}