package com.backend.wear.controller;

import com.backend.wear.dto.DonationApplyResponseDto;
import com.backend.wear.dto.UserPasswordDto;
import com.backend.wear.dto.UserRequestDto;
import com.backend.wear.dto.UserResponseDto;
import com.backend.wear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    //마이페이지 사용자 정보
    //api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<?> getMyPageUser(@PathVariable Long userId) {
        UserResponseDto userResponseDto;

        try{
            userResponseDto=userService.getMyPageUserService(userId);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(userResponseDto);
    }

    //사용자 프로필
    //api/users/profile/{userId}
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId){
        UserResponseDto userResponseDto;

        try{
            userResponseDto=userService.getUserProfileService(userId);

        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.ok(userResponseDto);
    }

    //사용자 프로필 수정
    //api/users/profile/{userId}
    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId,
                                               @RequestBody UserRequestDto userRequestDto)
    {
        try {
            userService.updateUserProfile(userId, userRequestDto);
            return ResponseEntity.ok().body("회원 정보가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //계정 정보
    // api/users/userInfo/{userId}
    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable Long userId){
        UserResponseDto userResponseDto;

        try{
            userResponseDto=userService.getUserInfo(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.ok(userResponseDto);
    }

    //계정 정보 저장
    // api/users/userInfo/{userId}
    @PutMapping("/userInfo/{userId}")
    public ResponseEntity<?> putUserInfo(@PathVariable Long userId,@RequestBody UserRequestDto userRequestDto){
        try {
            userService.updateUserInfo(userId, userRequestDto);
            return ResponseEntity.ok().body("사용자 이름이 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //비밀번호 변경하기
    // api/users/password/{userId}
    @PutMapping ("/password/{userId}")
    public ResponseEntity<?> putPassword(@PathVariable Long userId, @RequestBody UserPasswordDto userPasswordDto){
        try {
            userService.updatePassword(userId, userPasswordDto);
            return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 내 기부 내역 불러오기
    // api/users/myDonations/{userId}
    @GetMapping ("/myDonations/{userId}")
    public ResponseEntity<?> getMyDonationApply(@PathVariable Long userId){
        try {;
            List <DonationApplyResponseDto> responseDtoList
                    =userService.getMyDonationApplyService(userId);
            return ResponseEntity.ok().body(responseDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //내 기부 내역 중 기부 완료 된 상품만 불러오기
    // api/users/complete/{userId}
    @GetMapping ("/myDonations/complete/{userId}")
    public ResponseEntity<?> getMyDonationCompleteApplyComplete(@PathVariable Long userId){
        try {;
            List <DonationApplyResponseDto> responseDtoList
                    =userService.getMyDonationApplyCompleteService(userId);
            return ResponseEntity.ok().body(responseDtoList);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //문의
    @GetMapping("/help")
    public ResponseEntity<?> helpPage(){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("문의 페이지");
    }

    //서비스
    @GetMapping("/service")
    public ResponseEntity<?> servicePage(){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("서비스 페이지");
    }
}
