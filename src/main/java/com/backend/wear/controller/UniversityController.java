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
    public ResponseEntity<?> certifyUniversity(@RequestBody UniversityRequestDto.UnivCertDto univCertDto) {
        try {
            // 인증
            universityService.certifyUniversity(univCertDto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        // 응답값
        return ResponseEntity.ok().body("인증 메일 발송 완료");
    }
}
