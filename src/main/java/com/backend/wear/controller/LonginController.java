package com.backend.wear.controller;

import com.backend.wear.dto.jwt.TokenRequestDto;
import com.backend.wear.dto.login.*;
import com.backend.wear.service.LoginService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class LonginController {

    private final LoginService loginService;

    @Autowired
    public LonginController (LoginService loginService){
        this.loginService=loginService;
    }

    // 로그인
    // api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        try {
            LoginResponseDto loginResponseDto = loginService.login(loginRequestDto);
            log.info("로그인");

            // 헤더를 생성하고 토큰을 추가합니다.
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", loginResponseDto.getAccessToken());
            headers.add("Authorization-Refresh", loginResponseDto.getRefreshToken());

            // 헤더와 userId를 포함한 응답을 반환합니다.
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(Collections.singletonMap("userId", loginResponseDto.getUserId()));
        } catch (IllegalArgumentException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 로그아웃
    // api/auth/logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRequestDto logoutRequestDto) throws Exception{
        try {
            loginService.logout(logoutRequestDto);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 회원가입
    // api/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid  SignUpRequestDto signUpRequestDto) throws Exception{
        try {
            SignUpResponseDto signUpResponseDto = loginService.userSignUp(signUpRequestDto);
            return ResponseEntity.ok(signUpResponseDto);
        } catch (IllegalArgumentException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //대학 인증 메일 발송
    @PostMapping("/certify")
    public ResponseEntity<?> certifyUniversity(@RequestBody UniversityCertifyRequestDto.CertifyDto certifyDto) {
        try {
            return ResponseEntity.ok().body(loginService.certifyUniversity(certifyDto));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //대학 인증 코드 입력
    @PostMapping("/certify/code")
    public ResponseEntity<?> certifyCode(@RequestBody UniversityCertifyRequestDto.CertifyCodeDto certifyCodeDto) {
        try {
            return ResponseEntity.ok().body(loginService.certifyCode(certifyCodeDto));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
