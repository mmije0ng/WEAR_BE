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
    @GetMapping("/refresh/{userId}")
    public ResponseEntity<?> getRefresh(@PathVariable(name="userId") Long userId, @RequestBody TokenRequestDto tokenRequestDto) {
        try{
            TokenResponseDto tokenResponseDto = tokenService.getNewAccessToken(userId, tokenRequestDto);
            return ResponseEntity.ok(tokenResponseDto);
        } catch (ExpiredJwtException | IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}