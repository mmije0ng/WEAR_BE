package com.backend.wear.controller;

import com.backend.wear.config.jwt.Token;
import com.backend.wear.dto.blockeduser.BlockedUserResponseDto;
import com.backend.wear.dto.donation.DonationApplyResponseDto;
import com.backend.wear.dto.product.ProductRequestDto;
import com.backend.wear.dto.product.ProductResponseDto;
import com.backend.wear.dto.user.UserRequestDto;
import com.backend.wear.dto.user.UserResponseDto;
import com.backend.wear.service.TokenService;
import com.backend.wear.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService){
        this.userService=userService;
        this.tokenService=tokenService;
    }

    // 마이페이지 사용자 정보
    // /api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<?> getMyPageUser(@PathVariable(name="userId") Long userId ,@RequestHeader("Authorization") String authorizationHeader)
            throws Exception {
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try{
            UserResponseDto.MyPageDto myPageDto=userService.getMyPageUserService(userId);
            return ResponseEntity.ok(myPageDto);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 사용자 프로필
    // /api/users/profile/{userId}
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable(name="userId") Long userId, @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        UserResponseDto.ProfileDto profileDto;
        try{
            profileDto =userService.getUserProfileService(userId);

        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.ok(profileDto);
    }

    // 사용자 프로필 수정
    // /api/users/profile/{userId}
    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable(name="userId") Long userId,
                                               @RequestBody UserRequestDto.ProfileDto profileDto, @RequestHeader("Authorization") String authorizationHeader)
    throws Exception
    {
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            userService.updateUserProfile(userId, profileDto);
            return ResponseEntity.ok().body("회원 정보가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 계정 정보
    // /api/users/userInfo/{userId}
    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable(name="userId") Long userId, @RequestHeader("Authorization") String authorizationHeader){
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        UserResponseDto.InfoDto userResponseDto;
        try{
            userResponseDto=userService.getUserInfoService(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.ok(userResponseDto);
    }

    // 계정 정보 저장
    // /api/users/userInfo/update/{userId}
    @PutMapping("/userInfo/update/{userId}")
    public ResponseEntity<?> updateUserInfo(@PathVariable(name="userId") Long userId,@RequestBody UserRequestDto.InfoDto infodto
    ,@RequestHeader("Authorization") String authorizationHeader){
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            userService.updateUserInfoService(userId, infodto);
            return ResponseEntity.ok().body("사용자 이름이 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 비밀번호 변경하기
    // /api/users/password/{userId}
    @PutMapping ("/password/{userId}")
    public ResponseEntity<?> putPassword(@PathVariable(name="userId") Long userId, @RequestBody UserRequestDto.PasswordDto passwordDto,
                                         @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            userService.updatePassword(userId, passwordDto);
            return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 찜한 상품 불러오기
    // /api/users/wishList/{userId}?pageNumber={}
    @GetMapping("/wishList/{userId}")
    public ResponseEntity<?> getWishList(@PathVariable(name="userId") Long userId,
                                         @RequestParam(name="pageNumber") Integer pageNumber ,
                                         @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");
        try {
            Page <ProductResponseDto.ScreenDto> myWishPage = userService.getMyWishPage(userId, pageNumber);
            return ResponseEntity.ok(myWishPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 판매 중 상품 불러오기
    // /api/users/myProducts/onSale/{userId}?pageNumber={}
    @GetMapping("/myProducts/onSale/{userId}")
    public ResponseEntity<?> getMyProductsOnSale(@PathVariable(name="userId") Long userId,
                                                 @RequestParam(name="pageNumber")Integer pageNumber, @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try{
            Page<ProductResponseDto.MyPageScreenDto> myProductsPage =
                    userService.getMyProductsPage(userId,"onSale", pageNumber);
            return ResponseEntity.ok(myProductsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 판매 중인 상품 판매 완료하기
    // /api/users/myProducts/onSale/{userId}
    @PutMapping("/myProducts/onSale/{userId}")
    public ResponseEntity<?> updatePostStatusSoldOut(@PathVariable(name="userId") Long userId,
                                                 @RequestBody @Valid ProductRequestDto requestDto, @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try{
            userService.changePostStatus(userId, requestDto);
            return ResponseEntity.ok().body("상품 판매가 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 판매 완료 상품 불러오기
    // /api/users/myProducts/soldOut/{userId}?pageNumber={pageNumber}
    @GetMapping("/myProducts/soldOut/{userId}")
    public ResponseEntity<?> getMyProductsSoldOut(@PathVariable(name="userId") Long userId,
                                                  @RequestParam(name="pageNumber")Integer pageNumber, @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try{
            Page<ProductResponseDto.MyPageScreenDto> myProductsPage =
                    userService.getMyProductsPage(userId,"soldOut", pageNumber);
            return ResponseEntity.ok(myProductsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 숨김 처리 상품 불러오기
    // /api/users/myProducts/private/{userId}?pageNumber={}
    @GetMapping("/myProducts/private/{userId}")
    public ResponseEntity<?> getMyProductsPrivate(@PathVariable(name="userId") Long userId,
                                                  @RequestParam(name="pageNumber")Integer pageNumber,
                                                  @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try{
            Page<ProductResponseDto.PrivateDto> myPrivateProductsPage
                    = userService.getMyPrivateProductsPage(userId, pageNumber);
            return ResponseEntity.ok(myPrivateProductsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 내 기부 내역 불러오기
    // /api/users/myDonations/{userId}?pageNumber={pageNumber}
    @GetMapping ("/myDonations/{userId}")
    public ResponseEntity<?> getMyDonationApply(@PathVariable(name="userId") Long userId, @RequestParam(name="pageNumber") Integer pageNumber
            , @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            Page <DonationApplyResponseDto> myDonationApplyPage
                    =userService.myDonationApplysPage(userId, pageNumber);
            return ResponseEntity.ok().body(myDonationApplyPage);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 내 기부 내역 중 기부 완료 된 상품만 불러오기
    // /api/users/complete/{userId}?pageNumber={pageNumber}
    @GetMapping ("/myDonations/complete/{userId}")
    public ResponseEntity<?> getMyDonationApplyComplete(@PathVariable(name="userId") Long userId, @RequestParam(name="pageNumber") Integer pageNumber,
                                                        @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            Page <DonationApplyResponseDto>  myDonationApplyCompletePage
                    =userService.myDonationApplysCompletePage(userId, pageNumber);
            return ResponseEntity.ok().body(myDonationApplyCompletePage);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 차단한 사용자 리스트 불러오기
    // /api/users/blockedUsers/{userId}?pageNumber={pageNumber}
    @GetMapping("/blockedUsers/{userId}")
    public ResponseEntity<?> getBlockedUsersList(@PathVariable(name="userId") Long userId, @RequestParam(name="pageNumber") Integer pageNumber,
                                                 @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            Page<BlockedUserResponseDto> blockedUserPage = userService.getBlockedUsersPage(userId, pageNumber);
            return ResponseEntity.ok().body(blockedUserPage);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 차단 사용자 해제하기
    // /api/users/blockedUsers/unBlock/{userId}/{blockedUserId}
    @DeleteMapping("/blockedUsers/unBlock/{userId}/{blockedUserId}")
    public ResponseEntity<?> deleteBlockedUser(@PathVariable(name="userId") Long userId,
                                               @PathVariable(name="blockedUserId")Long blockedUserId,
                                               @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            userService.deleteBlockedUser(userId,blockedUserId);
            return ResponseEntity.ok().body("차단이 해제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 구매내역
//    @GetMapping("/myHistory/{userId}")
//    public ResponseEntity<?> getMyHistory(Long userId){
//        try {;
//            List<ProductResponseDto> list=userService.myHistoryService(userId);
//            return ResponseEntity.ok().body(list);
//
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(e.getMessage());
//        }
//    }
}
