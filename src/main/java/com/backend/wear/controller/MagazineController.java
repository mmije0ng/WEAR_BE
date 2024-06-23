package com.backend.wear.controller;

import com.backend.wear.config.jwt.JwtUtil;
import com.backend.wear.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@RestController
@RequestMapping("/api/magazine")
public class MagazineController {

    private final MagazineService magazineService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MagazineController(MagazineService magazineService, JwtUtil jwtUtil){
        this.magazineService=magazineService;
        this.jwtUtil=jwtUtil;
    }

    //퀴즈 정답만큼 환경 점수 올리기
    //api/magazine/{userId}?score=score
    @PostMapping("/{userId}")
    public ResponseEntity<?> quizPoint(@PathVariable("userId")Long userId,
                                       @RequestParam Integer score,
                                       @RequestHeader("Authorization") String authorizationHeader) {

        try {
            jwtUtil.validateUserIdWithHeader (authorizationHeader,userId);
            
            magazineService.updatePoint(userId, score);
            return ResponseEntity.ok().body("포인트 제공 완료.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }

    }

}
