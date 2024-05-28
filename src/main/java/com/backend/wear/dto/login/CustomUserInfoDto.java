package com.backend.wear.dto.login;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomUserInfoDto {
    private Long userId;
    private String email;
    private String password;
    private String nickName;
    private String role;
}
