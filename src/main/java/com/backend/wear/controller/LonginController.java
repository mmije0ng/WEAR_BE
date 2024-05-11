package com.backend.wear.controller;

import com.backend.wear.dto.login.SignUpRequestDto;
import com.backend.wear.dto.login.SignUpResponseDto;
import com.backend.wear.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class LonginController {

    private final LoginService loginService;

    @Autowired
    public LonginController (LoginService loginService){

        this.loginService=loginService;
    }

    // api/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        try {
            SignUpResponseDto signUpResponseDto = loginService.userSignUp(signUpRequestDto);
            return ResponseEntity.ok(signUpResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
