package com.backend.wear.controller;

import com.backend.wear.dto.university.UniversityResponseDto;
import com.backend.wear.service.UniversityService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/university")
public class UniversityController {

    private final UniversityService universityService;
    private CompletableFuture<ResponseEntity<?>> universityRankFuture;

    @Autowired
    public UniversityController(UniversityService universityService){
        this.universityService=universityService;
    }

    // 매월 1일 12시에 스케줄링된 작업 실행
    @Scheduled(cron = "0 0 12 1 * *", zone = "Asia/Seoul")
 //   @Scheduled(cron = "* * * * * *", zone = "Asia/Seoul")
    @Async
    public void universityRankSchedule() {
        try {
            UniversityResponseDto universityRank = universityService.getUniversityRank();
            universityRankFuture = CompletableFuture.completedFuture(ResponseEntity.ok().body(universityRank));
        } catch (Exception e) {
            universityRankFuture = CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
        }
    }

    // 대학 순위
    // /api/university/rank
    @GetMapping("/rank")
    public ResponseEntity<?> getUniversityRank() throws Exception {
        if (universityRankFuture != null && universityRankFuture.isDone()) {
            try {
                return ResponseEntity.ok().body(universityRankFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대학 순위 스케줄링 실패");
            }
        } else {
            return ResponseEntity.ok().body(universityService.getUniversityRank());
        }
    }
}
