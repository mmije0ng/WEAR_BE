package com.backend.wear.controller;

import com.backend.wear.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final TokenService tokenService;

    @Autowired
    TokenController(TokenService tokenService){
        this.tokenService=tokenService;
    }

    // accessToken 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> getRefresh(
            @RequestParam(name="userId") Long userId,
            HttpServletRequest request) {
        try{
            String newAccessToken = tokenService.getNewAccessToken(userId,
                    request.getHeader("Authorization"), request.getHeader("Authorization-Refresh"));

            // 헤더를 생성하고 토큰을 추가
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", newAccessToken);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(Collections.emptyMap()); // 빈 본문을 명시적으로 반환

        }  catch (ExpiredJwtException e){
            // refreshToken 만료시
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("refreshToken 만료. 재로그인 필요.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}