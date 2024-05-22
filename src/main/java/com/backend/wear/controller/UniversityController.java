package com.backend.wear.controller;

import com.backend.wear.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/university")
public class UniversityController {

    private final UniversityService universityService;

    @Autowired
    public UniversityController(UniversityService universityService){
        this.universityService=universityService;
    }

    // 대학 순위
    // 매월 1일 오전 12시에 스케줄링
    // /api/university/rank
    @Scheduled(cron = "0 0 12 1 * *", zone = "Asia/Seoul")
    @GetMapping("/rank")
    public ResponseEntity<?> universityRankingSchedule() {
        try{
            return ResponseEntity.ok().body(universityService.getUniversityRank());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
