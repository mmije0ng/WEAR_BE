package com.backend.wear.controller;

import com.backend.wear.config.jwt.JwtUtil;
import com.backend.wear.dto.jwt.TokenRequestDto;
import com.backend.wear.dto.jwt.TokenResponseDto;
import com.backend.wear.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getRefresh(@RequestBody TokenRequestDto tokenRequestDto) {
        try{
            TokenResponseDto tokenResponseDto = tokenService.getNewAccessToken(tokenRequestDto);
            return ResponseEntity.ok(tokenResponseDto);
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