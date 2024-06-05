package com.backend.wear.config.jwt;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RedisHash("token")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Token {
    @Id
    private String id; //refreshToken

    private Long userId;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expiration;
}