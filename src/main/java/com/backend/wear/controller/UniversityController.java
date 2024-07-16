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
    private CompletableFuture<ResponseEntity<UniversityResponseDto>> universityRankFuture;

    @Autowired
    public UniversityController(UniversityService universityService){
        this.universityService=universityService;
    }

    // 매월 1일 12시에 스케줄링된 작업, 대학 순위 불러오기 실행
    @Scheduled(cron = "0 0 12 1 * *", zone = "Asia/Seoul")
    @Async("customAsyncExecutor")
    public void universityRankSchedule() {
        try {
            UniversityResponseDto universityRank = universityService.getUniversityRank();
            universityRankFuture = CompletableFuture.completedFuture(ResponseEntity.ok().body(universityRank));
        } catch (Exception e) {
            universityRankFuture = null;
        }
    }

    // 대학 순위
    // 완료된 작업이 있다면 해당 작업(순위)을, 그렇지 않으면 사용자가 접속했을 때의 순위를 가져옴
    // /api/university/rank
    @GetMapping("/rank")
    public ResponseEntity<?> getUniversityRank() throws Exception {
        // 이미 정해진 시간에 스케줄링 되어 완료된 작업이 있다면
        if (universityRankFuture != null && universityRankFuture.isDone()) {
            try {
                return ResponseEntity.ok().body(universityRankFuture.get().getBody());
            } catch (InterruptedException | ExecutionException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("대학 순위 스케줄링 실패");
            }
        }

        else {
            return ResponseEntity.ok().body(universityService.getUniversityRank());
        }
    }
}
