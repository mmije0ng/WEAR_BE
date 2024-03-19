package com.backend.wear.controller;

import com.backend.wear.dto.UserRequestDto;
import com.backend.wear.dto.UserResponseDto;
import com.backend.wear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
