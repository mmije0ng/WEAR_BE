package com.backend.wear.controller;

import com.backend.wear.dto.*;
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
    @PutMapping("/userInfo/update/{userId}")
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

    //찜한 상품 리스트 보기
    // api/users/wishList/{userId}
    @GetMapping("/wishList/{userId}")
    public ResponseEntity<?> getWishList(@PathVariable Long userId){
        try {
            List<ProductResponseDto> wishList = userService.getWishListService(userId);
            return ResponseEntity.ok(wishList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //판매 중 상품 불러오기
    // api/users/myProducts/onSale/{userId}
    @GetMapping("/myProducts/onSale/{userId}")
    public ResponseEntity<?> getMyProductsOnSale(@PathVariable Long userId){
        try{
            List<ProductResponseDto> myProductList = userService.myProductsService(userId,"onSale");
            return ResponseEntity.ok(myProductList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //판매 중인 상품 판매 완료하기
    // api/users/myProducts/onSale/{userId}
    @PutMapping("/myProducts/onSale/{userId}")
    public ResponseEntity<?> postMyProductStatus(@PathVariable Long userId,
                                                 @RequestBody ProductRequestDto productRequestDto){
        try{
            userService.postMyProductStatusService(userId,productRequestDto);
            return ResponseEntity.ok().body("상품 판매가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //판매 완료 상품 불러오기
    // api/users/myProducts/soldOut/{userId}
    @GetMapping("/myProducts/soldOut/{userId}")
    public ResponseEntity<?> getMyProductsSoldOut(@PathVariable Long userId){
        try{
            List<ProductResponseDto> myProductList = userService.myProductsService(userId,"soldOut");
            return ResponseEntity.ok(myProductList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //숨김 처리 상품 불러오기
    // api/users/myProducts/private/{userId}
    @GetMapping("/myProducts/private/{userId}")
    public ResponseEntity<?> getMyProductsPrivate(@PathVariable Long userId){

        try{
            List<ProductResponseDto> privateList = userService.getMyProductsPrivateService(userId);
            return ResponseEntity.ok(privateList);
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
