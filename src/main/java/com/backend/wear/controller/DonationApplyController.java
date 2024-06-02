package com.backend.wear.controller;

import com.backend.wear.dto.donation.DonationApplyRequestDto;
import com.backend.wear.service.DonationApplyService;
import com.backend.wear.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/donations")
public class DonationApplyController {
    private final DonationApplyService donationApplyService;
    private final TokenService tokenService;

    @Autowired
    public DonationApplyController(DonationApplyService donationApplyService, TokenService tokenService){
        this.donationApplyService=donationApplyService;
        this.tokenService=tokenService;
    }

    // 기부 등록하기
    // api/donations/{userId}?charity={charity}
    @PostMapping("/{userId}")
    public ResponseEntity<?> postDonationApply(@PathVariable(name="userId")Long userId,@RequestParam(name="charity") Integer charity,
                                               @RequestBody DonationApplyRequestDto donationApplyRequestDto, @RequestHeader("Authorization") String authorizationHeader){
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try{
            donationApplyService.donationApplyService(userId, charity, donationApplyRequestDto);
            return ResponseEntity.ok().body("기부가 완료되었습니다.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
