package com.backend.wear.service;

import com.backend.wear.config.jwt.*;
import com.backend.wear.dto.jwt.TokenRequestDto;
import com.backend.wear.dto.jwt.TokenResponseDto;
import com.backend.wear.entity.User;
import com.backend.wear.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

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

    @Transactional
    public TokenResponseDto getNewAccessToken(Long userId, TokenRequestDto dto) throws ExpiredJwtException{
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String requestAccessToken= dto.getAccessToken();
        String requestRefreshToken= dto.getRefreshToken();

        // refresh token 만료기간 검증
        jwtUtil.validateToken(requestRefreshToken);

        // userId로 유효한 토큰인지 검증
        Long requestAccessUserId= jwtUtil.getTokenUserId(requestAccessToken);
        Long requestRefreshUserId= jwtUtil.getTokenUserId(requestAccessToken);
        if( (requestAccessUserId==requestRefreshUserId) && (requestAccessUserId==userId)
            && (userId==requestRefreshUserId)
        ){
            // 새로운 accessToken 발급
            String accessToken = jwtUtil.createAccessToken(mapToCustomUser(user));

            return TokenResponseDto.builder()
                    .accessToken(accessToken)
                    .build();
        }

        else
            throw new IllegalArgumentException("유효한 토큰이 아닙니다.");
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
