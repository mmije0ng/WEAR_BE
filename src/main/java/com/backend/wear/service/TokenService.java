package com.backend.wear.service;

import com.backend.wear.config.jwt.*;
import com.backend.wear.entity.User;
import com.backend.wear.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TokenService {
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Autowired
    TokenService(JwtUtil jwtUtil, TokenRepository tokenRepository,
                 UserRepository userRepository){
        this.jwtUtil=jwtUtil;
        this.tokenRepository=tokenRepository;
        this.userRepository=userRepository;
    }

    // token userId 검증
    public Boolean isEqualsUserIdJWT (String authorizationHeader, Long userId){
        // JWT 토큰에서 "Bearer " 접두사 제거
        String token = authorizationHeader.substring(7);
        // JWT 토큰에서 userId 추출
        Long tokenUserId = jwtUtil.getTokenUserId(token);

        // URL의 userId와 JWT 토큰의 userId 비교
        if (userId.equals(tokenUserId))
            return true;
        else
            return false;
    }

    // accessToken 재발급
    @Transactional
    public String getNewAccessToken(Long id, String authorizationHeader, String authorizationRefreshHeader )
            throws ExpiredJwtException{

        Long userId = id;
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        log.info("재발급");

        // JWT 토큰에서 "Bearer " 접두사 제거
        String requestAccessToken = authorizationHeader.substring(7);
        String requestRefreshToken = authorizationRefreshHeader.substring(7);

        // token 만료기간 검증
        jwtUtil.validateToken(requestRefreshToken);

        log.info("refresh");

        // userId로 유효한 토큰인지 검증
        Long requestRefreshUserId= jwtUtil.getTokenUserId(requestRefreshToken);
        if(userId==requestRefreshUserId){

            log.info("repo");
            tokenRepository.findById(requestRefreshToken)
                    .orElseThrow(() -> new IllegalArgumentException("refreshToken 만료. 재로그인 필요."));

            // 새로운 accessToken 발급
            String newAccessToken = jwtUtil.createAccessToken(mapToCustomUser(user));

            return newAccessToken;
        }

        else
            throw new IllegalArgumentException("refreshToken의 userId 불일치");
    }

    private CustomUserInfoDto mapToCustomUser(User user){
        return CustomUserInfoDto.builder()
                .userId(user.getId())
                .email(user.getUniversityEmail())
                .password(user.getUserPassword())
                .nickName(user.getNickName())
                .role(user.getRole().getRole())
                .build();
    }
}
