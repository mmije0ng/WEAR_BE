package com.backend.wear.controller;

import com.backend.wear.config.jwt.JwtUtil;
import com.backend.wear.dto.donation.DonationApplyRequestDto;
import com.backend.wear.service.DonationApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@RestController
@RequestMapping("/api/donations")
public class DonationApplyController {
    private final DonationApplyService donationApplyService;
    private final JwtUtil jwtUtil;

    @Autowired
    public DonationApplyController(DonationApplyService donationApplyService, JwtUtil jwtUtil){
        this.donationApplyService=donationApplyService;
        this.jwtUtil=jwtUtil;
    }

    // 기부 등록하기
    // api/donations/{userId}?charity={charity}
    @PostMapping("/{userId}")
    public ResponseEntity<?> postDonationApply(@PathVariable(name="userId")Long userId,@RequestParam(name="charity") Integer charity,
                                               @RequestBody DonationApplyRequestDto donationApplyRequestDto,
                                               @RequestHeader("Authorization") String authorizationHeader){
        try{
            jwtUtil.validateUserIdWithHeader (authorizationHeader,userId);

            donationApplyService.donationApplyService(userId, charity, donationApplyRequestDto);
            return ResponseEntity.ok().body("기부가 완료되었습니다.");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        }
    }
}
