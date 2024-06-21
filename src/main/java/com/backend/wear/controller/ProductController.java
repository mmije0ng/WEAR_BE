package com.backend.wear.controller;

import com.backend.wear.dto.product.ProductPostRequestDto;
import com.backend.wear.dto.product.ProductRequestDto;
import com.backend.wear.dto.product.ProductResponseDto;
import com.backend.wear.service.ProductService;
import com.backend.wear.service.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final TokenService tokenService;
    private CompletableFuture<Object> searchNameRankFuture;


    @Autowired
    public ProductController(ProductService productService, TokenService tokenService){
        this.productService=productService;
        this.tokenService=tokenService;
    }

    // 카테고리별 최신순 조회
    // api/products/category?categoryName={}&userId={}&pageNumber={pageNumber}
    @GetMapping("/category")
    public ResponseEntity<?> findProductsByCategory(@RequestParam(name="categoryName") String categoryName, @RequestParam(name="userId") Long userId,
                                                    @RequestParam(name="pageNumber") Integer pageNumber) throws Exception {
        try {
            Page <ProductResponseDto.ScreenDto> productsPage = productService.findProductsByCategory(categoryName, userId, pageNumber);
            return ResponseEntity.ok(productsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 카테고리별 최신순, 판매 상태
    // api/products/category/sale?categoryName={}&userId={}&pageNumber={pageNumber}
    @GetMapping("/category/sale")
    public ResponseEntity<?> findProductsByCategoryOnSale(@RequestParam(name="categoryName") String categoryName,
                                                          @RequestParam(name="userId") Long userId,
                                                          @RequestParam(name="pageNumber") Integer pageNumber) throws Exception
    {
        try {
            Page <ProductResponseDto.ScreenDto> productsPage =
                    productService.findProductsByCategoryOnSale(categoryName, userId, pageNumber);
            return ResponseEntity.ok(productsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //상품 리스트 검색어별, 카테고리별 , 최신순(default)으로 조회하기
    //  /products/search/category?searchName={searchName}&userId={userId}&pageNumber={pageNumber}
    @GetMapping("/search/category")
    public ResponseEntity<?> searchProductsByCategory(@RequestParam(name="searchName") String searchName, @RequestParam(name="categoryName")String categoryName,
                                                      @RequestParam(name="userId") Long userId,
                                                      @RequestParam(name="pageNumber")Integer pageNumber) throws Exception {

        try {
            Page <ProductResponseDto.ScreenDto> productsPage
                    = productService.searchProductByProductNameAndCategory(searchName, categoryName, userId, pageNumber);
            return ResponseEntity.ok(productsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    // 검색어별, 카테고리별, 판매중인 상품 최신순(default)으로 조회하기
    //   /products/search/category/sale?searchName={}&categoryName={}&userId=1{}&pageNumber={}
    @GetMapping("/search/category/sale")
    public ResponseEntity<?> searchProductsByCategoryOnSale(@RequestParam(name="searchName") String searchName, @RequestParam(name="categoryName")String categoryName,
                                                            @RequestParam(name="userId") Long userId,
                                                            @RequestParam(name="pageNumber")Integer pageNumber) throws Exception {

        try {
            Page <ProductResponseDto.ScreenDto> productsPage
                    = productService. searchProductByProductNameAndCategoryOnSale(searchName, categoryName, userId, pageNumber);
            return ResponseEntity.ok(productsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    //상품 상세 페이지 불러오기
    // api/products/{productId}
    @GetMapping("/{userId}/{productId}")
    public ResponseEntity<?> getProductPost(@PathVariable("userId") Long userId,@PathVariable("productId") Long productId,
                                            @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            ProductResponseDto.DetailDto productPost = productService.getProductPost(userId, productId);
            return ResponseEntity.ok(productPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 상품 등록
    @PostMapping("/new/{userId}")
    public ResponseEntity<?> postProductPost(@PathVariable(name="userId") Long userId , @RequestBody @Valid ProductPostRequestDto requestDTO,
                                             @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.createProductPost(requestDTO,userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    // 상품 상세/상품 정보 수정하기로 이동
    @GetMapping("/edit/{userId}/{productId}")
    public ResponseEntity<?> getProductPostToUpdate(@PathVariable(name="userId") Long userId , @PathVariable(name="productId") Long productId,
                                                    @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            ProductResponseDto.EditDto editDto
                    = productService.getProductPostToUpdate(userId, productId);
            return ResponseEntity.ok(editDto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //상품상세/ 상품 정보 수정하기
    @PutMapping("/edit/{userId}/{productId}")
    public ResponseEntity<?> updateProductPost(@PathVariable(name="userId") Long userId , @PathVariable(name="productId") Long productId ,
                                               @RequestBody @Valid ProductPostRequestDto requestDTO,@RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        productService.updateProductPost(requestDTO,userId,productId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    //상품 상세/ 상품 판매 상태 변경하기
    //판매완료로 변경
    @PutMapping("/soldOut/{userId}")
    public ResponseEntity<?> updateProductPostStatusSoldOut(@PathVariable(name="userId") Long userId,@RequestBody @Valid ProductRequestDto requestDto
            , @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.updateProductPostStatus(requestDto,userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    //상품 상세/ 상품 판매 상태 변경하기
    //판매중으로 변경
    @PutMapping("/onSale/{userId}")
    public ResponseEntity<?> updateProductPostStatusOnSale(@PathVariable(name="userId") Long userId,@RequestBody @Valid ProductRequestDto requestDto
            , @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.updateProductPostStatus(requestDto,userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    //상품 상세/ 상품 글 숨기기 || 숨김 해제하기
    @PutMapping("/private/{userId}/{productId}")
    public ResponseEntity<?> updateProductPostPrivate(@PathVariable(name="userId") Long userId , @PathVariable(name="productId") Long productId,
                                                      @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.updateProductPostPrivate(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    //상품 상세/ 상품 삭제하기
    @DeleteMapping("/delete/{userId}/{productId}")
    public ResponseEntity<?> deleteProductPost(@PathVariable(name="userId") Long userId ,@PathVariable(name="productId") Long productId,
                                               @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.deleteProductPost(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    // 상품 찜하기
    @PostMapping("/select/{userId}/{productId}")
    public ResponseEntity<?> selectProduct(@PathVariable(name="userId") Long userId ,@PathVariable(name="productId") Long productId,
                                           @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.selectProduct(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok().body("상품 찜하기 성공.");
    }

    // 상품 찜 해제
    @DeleteMapping("/deselect/{userId}/{productId}")
    public ResponseEntity<?> deselectProduct(@PathVariable(name="userId") Long userId ,@PathVariable(name="productId") Long productId,  @RequestHeader("Authorization") String authorizationHeader)
            throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.deselectProduct(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok().body("상품 찜 해제");
    }

    // 사용자 차단하기
    // /api/products/block/{userId}/{blockedUserId}
    @PostMapping("/block/{userId}/{blockedUserId}")
    public ResponseEntity<?> blockedUser(@PathVariable(name="userId") Long userId
            ,@PathVariable(name="blockedUserId") Long blockedUserId, @RequestHeader("Authorization") String authorizationHeader)
            throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.blockedUser(userId,blockedUserId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok().body("사용자 차단 완료");
    }

    // 사용자 최근 검색어 저장
    // /api/products/search/save?userId={userId}&searchName={searchName}
    @PostMapping("/search/save")
    public ResponseEntity<?> saveRecentSearchLog(@RequestParam(name="userId") Long userId,
                                                 @RequestParam(name="searchName") String searchName ,
                                                 @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.saveRecentSearchLog(userId,searchName);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok().body("최근 검색어 저장 완료");
    }

    // 최근 검색어 조회
    // /api/products/search/list?userId={userId}
    @GetMapping("/search/list")
    public ResponseEntity<?> findRecentSearchLogs(@RequestParam(name="userId") Long userId
            , @RequestHeader("Authorization") String authorizationHeader)
            throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            return ResponseEntity.ok(productService.findRecentSearchLogs(userId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 최근 검색어 삭제
    // /api/products/search/delete?userId={userId}&searchName={searchName}
    @DeleteMapping("/search/delete")
    public ResponseEntity<?> deleteSearchName(@RequestParam(name="userId") Long userId,
                                              @RequestParam(name="searchName") String searchName, @RequestHeader("Authorization") String authorizationHeader) throws Exception{
        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            Long count = productService.deleteRecentSearchLog(userId,searchName);
            if (count==1)
                return ResponseEntity.ok().body("최근 검색어 삭제 완료");
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("최근 검색어 삭제 실패");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 최근 검색어 전체 삭제
    // /api/products/search/delete/all?userId={userId}
    @DeleteMapping("/search/delete/all")
    public ResponseEntity<?> deleteAllSearchNameByUser(@RequestParam(name="userId") Long userId,
                                                       @RequestHeader("Authorization") String authorizationHeader) throws Exception{

        if(!tokenService.isEqualsUserIdJWT(authorizationHeader,userId))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("accessToken의 userId 불일치");

        try {
            productService.deleteAllSearchNameByUser(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("최근 검색어 전체 삭제 완료");
    }

    // 매일 정각에 스케줄링된 작업 실행
    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul") // 매일 정각에 실행
 //   @Scheduled(cron = "* * * * * *", zone = "Asia/Seoul") // 매일 정각에 실행
    @Async
    public void searchNameRankListSchedule() {
        try {
            searchNameRankFuture = CompletableFuture.completedFuture(productService.getSearchNameRank());
        } catch (Exception e) {
            System.err.println("Error during scheduled task: " + e.getMessage());
            searchNameRankFuture = CompletableFuture.completedFuture(e.getMessage());
        }
    }

    // 인기 검색어 조회
    // /api/products/search/rank
    @GetMapping("/search/rank")
    public ResponseEntity<?> getSearchNameRank() {
        if (searchNameRankFuture != null && searchNameRankFuture.isDone()) {
            try {
                return ResponseEntity.ok().body(searchNameRankFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인기 검색어 스케줄링 실패");
            }
        } else {
            return ResponseEntity.ok().body(productService.getSearchNameRank());
        }
    }
}