package com.backend.wear.dto.login;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponseDto {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
