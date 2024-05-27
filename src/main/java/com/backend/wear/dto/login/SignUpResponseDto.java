package com.backend.wear.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponseDto {
    @JsonProperty("sign_up_success")
    private boolean signUpSuccess;
    private Long userId;
}