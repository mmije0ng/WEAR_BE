package com.backend.wear.controller;

import com.backend.wear.config.jwt.JwtUtil;
import com.backend.wear.dto.blockeduser.BlockedUserResponseDto;
import com.backend.wear.dto.donation.DonationApplyResponseDto;
import com.backend.wear.dto.product.ProductRequestDto;
import com.backend.wear.dto.product.ProductResponseDto;
import com.backend.wear.dto.user.UserRequestDto;
import com.backend.wear.dto.user.UserResponseDto;
import com.backend.wear.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    // 마이페이지 사용자 정보
    // /api/users/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<?> getMyPageUser( @RequestHeader("Authorization") String authorizationHeader,
                                            @PathVariable(name="userId") Long userId)
            throws Exception {
        try{
            UserResponseDto.MyPageDto myPageDto=userService.getMyPageUserService(userId);

            return ResponseEntity.ok(myPageDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 사용자 프로필
    // /api/users/profile/{userId}
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getUserProfile( @RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable(name="userId") Long userId)
            throws Exception{
        try{
            UserResponseDto.ProfileDto profileDto =userService.getUserProfileService(userId);

            return ResponseEntity.ok(profileDto);

        } catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 사용자 프로필 수정
    // /api/users/profile/{userId}
    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable(name="userId") Long userId,
                                               @RequestBody UserRequestDto.ProfileDto profileDto) throws Exception {
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
    public ResponseEntity<?> getUserInfo( @RequestHeader("Authorization") String authorizationHeader,
                                          @PathVariable(name="userId") Long userId) throws Exception {
        try{
            UserResponseDto.InfoDto userResponseDto =userService.getUserInfoService(userId);
            return ResponseEntity.ok(userResponseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 계정 정보 저장
    // /api/users/userInfo/update/{userId}
    @PutMapping("/userInfo/update/{userId}")
    public ResponseEntity<?> updateUserInfo( @RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable(name="userId") Long userId,
                                            @RequestBody UserRequestDto.InfoDto infodto) throws Exception {
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
    public ResponseEntity<?> putPassword(@RequestHeader("Authorization") String authorizationHeader,
                                         @PathVariable(name="userId") Long userId,
                                         @RequestBody UserRequestDto.PasswordDto passwordDto) throws Exception{
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
    public ResponseEntity<?> getWishList(@RequestHeader("Authorization") String authorizationHeader,
                                         @PathVariable(name="userId") Long userId,
                                         @RequestParam(name="pageNumber") Integer pageNumber) throws Exception{
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
    public ResponseEntity<?> getMyProductsOnSale(@RequestHeader("Authorization") String authorizationHeader,
                                                 @PathVariable(name="userId") Long userId,
                                                 @RequestParam(name="pageNumber")Integer pageNumber) throws Exception{
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
    public ResponseEntity<?> updatePostStatusSoldOut(@RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable(name="userId") Long userId,
                                                 @RequestBody @Valid ProductRequestDto requestDto) throws Exception{
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
    public ResponseEntity<?> getMyProductsSoldOut( @RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable(name="userId") Long userId,
                                                  @RequestParam(name="pageNumber")Integer pageNumber) throws Exception{
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
    public ResponseEntity<?> getMyProductsPrivate( @RequestHeader("Authorization") String authorizationHeader,
                                                   @PathVariable(name="userId") Long userId,
                                                  @RequestParam(name="pageNumber")Integer pageNumber) throws Exception{
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
    public ResponseEntity<?> getMyDonationApply( @RequestHeader("Authorization") String authorizationHeader,
                                                 @PathVariable(name="userId") Long userId,
                                                 @RequestParam(name="pageNumber") Integer pageNumber) throws Exception{
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
    public ResponseEntity<?> getMyDonationApplyComplete( @RequestHeader("Authorization") String authorizationHeader,
                                                         @PathVariable(name="userId") Long userId,
                                                         @RequestParam(name="pageNumber") Integer pageNumber) throws Exception{
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
    public ResponseEntity<?> getBlockedUsersList( @RequestHeader("Authorization") String authorizationHeader,
                                                  @PathVariable(name="userId") Long userId,
                                                 @RequestParam(name="pageNumber") Integer pageNumber) throws Exception{
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
    public ResponseEntity<?> deleteBlockedUser(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable(name="userId") Long userId,
                                               @PathVariable(name="blockedUserId")Long blockedUserId) throws Exception{
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
