package com.backend.wear.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Duration;

@RedisHash("token")
@AllArgsConstructor
@Getter
@Builder
public class Token {
    @Id
    private Long id;

    private String refreshToken;

    @TimeToLive // Duration 타입으로 지정
    private Duration expiration;
}