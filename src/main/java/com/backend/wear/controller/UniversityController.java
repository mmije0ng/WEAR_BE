package com.backend.wear.controller;

import com.backend.wear.dto.university.UniversityRequestDto;
import com.backend.wear.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/university")
public class UniversityController {

    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService){
        this.universityService=universityService;
    }

    //대학 인증 메일 발송
    @PostMapping("/certify")
    public ResponseEntity<?> certifyUniversity(@RequestBody UniversityRequestDto.CertifyDto certifyDto) {
        try {
            return ResponseEntity.ok().body(universityService.certifyUniversity(certifyDto));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //대학 인증 코드 입력
    @PostMapping("/certify/code")
    public ResponseEntity<?> certifyCode(@RequestBody UniversityRequestDto.CertifyCodeDto certifyCodeDto) {
        try {
            return ResponseEntity.ok().body(universityService.certifyCode(certifyCodeDto));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
