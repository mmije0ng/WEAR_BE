package com.backend.wear.controller;

import com.backend.wear.dto.UserResponseDto;
import com.backend.wear.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            userResponseDto=userService.getMyPageUserResponseDto(userId);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(userResponseDto);
    }
}
