package com.backend.wear.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;
}
