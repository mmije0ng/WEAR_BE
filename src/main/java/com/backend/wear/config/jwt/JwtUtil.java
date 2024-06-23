package com.backend.wear.config.jwt;

import com.backend.wear.entity.User;
import com.backend.wear.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SignatureException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime; //access token 만료 시간
    private final long refreshTokenExpTime; //refresh token 만료 시간

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access_token.expiration_time}") long accessTokenExpTime,
            @Value("${jwt.refresh_token.expiration_time}") long refreshTokenExpTime,
            UserRepository userRepository, TokenRepository tokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime=refreshTokenExpTime;
        this.userRepository=userRepository;
        this.tokenRepository=tokenRepository;
    }

    public String resolveToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);  // "Bearer " 이후의 실제 토큰 부분만 반환
        }
        return null;
    }

    // Access Token 생성
    public String createAccessToken(CustomUserInfoDto user) {
        return doCreateToken(user, accessTokenExpTime);
    }

    // Refresh Token 생성
    public String createRefreshToken(CustomUserInfoDto user) {
        return doCreateToken(user, refreshTokenExpTime);
    }

    // JWT 생성
    private String doCreateToken(CustomUserInfoDto user, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getUserId()); //pk
        claims.put("role", user.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 재발급용  Refresh Token (userId 이용)
    public String createRefreshTokenById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Claims claims = Jwts.claims();
        claims.put("userId", userId); //pk
        claims.put("role", user.getRole().getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(refreshTokenExpTime);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        Token token = Token.builder()
                .id(refreshToken)
                .userId(user.getId())
                .build();

        tokenRepository.save(token);

        return refreshToken;
    }

    // 토큰에서 userId 추출
    public Long getTokenUserId(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    // JWT 토큰 검증
//    public boolean validateToken(String token)  {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT Token", e);
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT Token", e);
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT Token", e);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty.", e);
//        }
//        return false;
//    }

    public void validateToken(String token) throws ExpiredJwtException  {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    // JWT Claims 추출
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validateUserIdWithHeader (String authorizationHeader, Long userId) throws java.security.SignatureException {
        // 헤더의 토큰에서 userId 추출
        String token = resolveToken(authorizationHeader);
        Long tokenUserId = getTokenUserId(token);

        // 토큰 만료 기간 검증
        validateToken(token);

        // 토큰의 userId와 현재 인증된 사용자의 userId 비교
        if(!userId.equals(tokenUserId))
            throw new SignatureException("accessToken의 userId 불일치");
    }

//    // Authorization header의 userId와 로그인한 userId 일치 여부 검증
//    // SignatureException :  JWT의 기존 서명을 확인하지 못했을 때
//    public void validateUserIdWithHeader(String authorizationHeader) throws Exception {
//        String token = resolveToken(authorizationHeader);
//
//        // 토큰 만료 기간 검증
//        validateToken(token);
//
//        // 토큰에서 userId 추출
//        Long tokenUserId = getTokenUserId(token);
//
//        // SecurityContext에서 현재 인증된 사용자 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new SignatureException("authentication 오류");
//        }
//
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        Long currentUserId = Long.parseLong(userDetails.getUsername());
//
//        log.info("tokenUserId: "+tokenUserId+" currentUserId: "+currentUserId);
//
//        // 토큰의 userId와 현재 인증된 사용자의 userId 비교
//        if(!tokenUserId.equals(currentUserId))
//            throw new SignatureException("accessToken의 userId 불일치");
//    }
}