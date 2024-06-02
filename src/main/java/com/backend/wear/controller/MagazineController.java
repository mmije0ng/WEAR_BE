package com.backend.wear.controller;

import com.backend.wear.service.MagazineService;
import com.backend.wear.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/magazine")
public class MagazineController {

    private final MagazineService magazineService;
    private final TokenService tokenService;

    @Autowired
    public MagazineController(MagazineService magazineService, TokenService tokenService){
        this.magazineService=magazineService;
        this.tokenService=tokenService;
    }

    //퀴즈 정답만큼 환경 점수 올리기
    //api/magazine/{userId}?score=score
    @PostMapping("/{userId}")
    public ResponseEntity<?> quizPoint(@PathVariable("userId")Long userId,
                                       @RequestParam Integer score,
                                       @RequestHeader("Authorization") String authorizationHeader)
    {
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            magazineService.updatePoint(userId, score);
            return ResponseEntity.ok().body("포인트 제공 완료.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

}
