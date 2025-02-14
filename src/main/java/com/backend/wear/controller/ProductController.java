package com.backend.wear.controller;

import com.backend.wear.config.jwt.JwtUtil;
import com.backend.wear.dto.product.ProductPostRequestDto;
import com.backend.wear.dto.product.ProductRequestDto;
import com.backend.wear.dto.product.ProductResponseDto;
import com.backend.wear.dto.product.SearchResponseDto;
import com.backend.wear.dto.university.UniversityResponseDto;
import com.backend.wear.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private CompletableFuture< ResponseEntity<SearchResponseDto.RankDto> > searchNameRankFuture;

    @Autowired
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    // 카테고리별 최신순 조회
    // api/products/category?categoryName={}&userId={}&pageNumber={pageNumber}
    @GetMapping("/category")
    public ResponseEntity<?> findProductsByCategory(@RequestHeader("Authorization") String authorizationHeader, @RequestParam(name="categoryName") String categoryName, @RequestParam(name="userId") Long userId,
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
    public ResponseEntity<?> findProductsByCategoryOnSale(@RequestHeader("Authorization") String authorizationHeader,
                                                          @RequestParam(name="categoryName") String categoryName,
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
    public ResponseEntity<?> searchProductsByCategory(@RequestHeader("Authorization") String authorizationHeader,
                                                      @RequestParam(name="searchName") String searchName, @RequestParam(name="categoryName")String categoryName,
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
    public ResponseEntity<?> searchProductsByCategoryOnSale(@RequestHeader("Authorization") String authorizationHeader,
                                                            @RequestParam(name="searchName") String searchName, @RequestParam(name="categoryName")String categoryName,
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
    // api/products/{userId}/{productId}
    @GetMapping("/{userId}/{productId}")
    public ResponseEntity<?> getProductPost( @RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable("userId") Long userId,
                                            @PathVariable("productId") Long productId) throws Exception {
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
    public ResponseEntity<?> postProductPost(@RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable(name="userId") Long userId ,
                                             @RequestBody ProductPostRequestDto requestDTO) throws Exception{
        try {
            productService.createProductPost(requestDTO,userId);
            return ResponseEntity.ok(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 상품 상세/상품 정보 수정하기로 이동
    @GetMapping("/edit/{userId}/{productId}")
    public ResponseEntity<?> getProductPostToUpdate( @RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable(name="userId") Long userId,
                                                    @PathVariable(name="productId") Long productId) throws Exception{
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
    public ResponseEntity<?> updateProductPost(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable(name="userId") Long userId,
                                               @PathVariable(name="productId") Long productId ,
                                               @RequestBody ProductPostRequestDto requestDTO) throws Exception{
        try {
            productService.updateProductPost(requestDTO,userId,productId);
            return ResponseEntity.ok(HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //상품 상세/ 상품 판매 상태 변경하기
    //판매완료로 변경
    @PutMapping("/soldOut/{userId}")
    public ResponseEntity<?> updateProductPostStatusSoldOut(@RequestHeader("Authorization") String authorizationHeader,
                                                            @PathVariable(name="userId") Long userId,
                                                            @RequestBody ProductRequestDto requestDto) throws Exception{
        try {
            productService.updateProductPostStatus(requestDto,userId);
            return ResponseEntity.ok(HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //상품 상세/ 상품 판매 상태 변경하기
    //판매중으로 변경
    @PutMapping("/onSale/{userId}")
    public ResponseEntity<?> updateProductPostStatusOnSale(@RequestHeader("Authorization") String authorizationHeader,
                                                           @PathVariable(name="userId") Long userId,
                                                           @RequestBody ProductRequestDto requestDto) throws Exception{
        try {
            productService.updateProductPostStatus(requestDto,userId);
            return ResponseEntity.ok(HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //상품 상세/ 상품 글 숨기기 || 숨김 해제하기
    @PutMapping("/private/{userId}/{productId}")
    public ResponseEntity<?> updateProductPostPrivate(@RequestHeader("Authorization") String authorizationHeader,
                                                      @PathVariable(name="userId") Long userId,
                                                      @PathVariable(name="productId") Long productId) throws Exception{
        try {
            productService.updateProductPostPrivate(userId,productId);
            return ResponseEntity.ok(HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    //상품 상세/ 상품 삭제하기
    @DeleteMapping("/delete/{userId}/{productId}")
    public ResponseEntity<?> deleteProductPost(@RequestHeader("Authorization") String authorizationHeader,
                                               @PathVariable(name="userId") Long userId,
                                               @PathVariable(name="productId") Long productId) throws Exception{
        try {
            productService.deleteProductPost(userId,productId);
            return ResponseEntity.ok(HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 상품 찜하기
    @PostMapping("/select/{userId}/{productId}")
    public ResponseEntity<?> selectProduct(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable(name="userId") Long userId,
                                           @PathVariable(name="productId") Long productId) throws Exception{
        try {
            productService.selectProduct(userId,productId);
            return ResponseEntity.ok().body("상품 찜하기 성공.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 상품 찜 해제
    @DeleteMapping("/deselect/{userId}/{productId}")
    public ResponseEntity<?> deselectProduct(@RequestHeader("Authorization") String authorizationHeader,
                                             @PathVariable(name="userId") Long userId,
                                             @PathVariable(name="productId") Long productId) throws Exception{
        try {
            productService.deselectProduct(userId,productId);
            return ResponseEntity.ok().body("상품 찜 해제");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 사용자 차단하기
    // /api/products/block/{userId}/{blockedUserId}
    @PostMapping("/block/{userId}/{blockedUserId}")
    public ResponseEntity<?> blockedUser(@RequestHeader("Authorization") String authorizationHeader,
                                         @PathVariable(name="userId") Long userId,
                                         @PathVariable(name="blockedUserId") Long blockedUserId)
            throws Exception{

        try {
            productService.blockedUser(userId,blockedUserId);
            return ResponseEntity.ok().body("사용자 차단 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 사용자 최근 검색어 저장
    // /api/products/search/save?userId={userId}&searchName={searchName}
    @PostMapping("/search/save")
    public ResponseEntity<?> saveRecentSearchLog(@RequestHeader("Authorization") String authorizationHeader,
                                                 @RequestParam(name="userId") Long userId,
                                                 @RequestParam(name="searchName") String searchName) throws Exception{
        try {
            productService.saveRecentSearchLog(userId,searchName);
            return ResponseEntity.ok().body("최근 검색어 저장 완료");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 최근 검색어 조회
    // /api/products/search/list?userId={userId}
    @GetMapping("/search/list")
    public ResponseEntity<?> findRecentSearchLogs(@RequestHeader("Authorization") String authorizationHeader,
                                                  @RequestParam(name="userId") Long userId) throws Exception{
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
    public ResponseEntity<?> deleteSearchName( @RequestHeader("Authorization") String authorizationHeader,
                                               @RequestParam(name="userId") Long userId,
                                              @RequestParam(name="searchName") String searchName) throws Exception{
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
    public ResponseEntity<?> deleteAllSearchNameByUser(@RequestHeader("Authorization") String authorizationHeader,
                                                       @RequestParam(name="userId") Long userId) throws Exception{
        try {
            productService.deleteAllSearchNameByUser(userId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("최근 검색어 전체 삭제 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // 매일 정각에 스케줄링된 작업, 인기 검색어 리스틉 불러오기 실행
    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    @Async("customAsyncExecutor")
    public void searchNameRankListSchedule() {
        try {
            SearchResponseDto.RankDto rankDto = productService.getSearchNameRank();
            searchNameRankFuture = CompletableFuture.completedFuture(ResponseEntity.ok().body(rankDto));
        } catch (Exception e) {
            System.err.println("Error during scheduled task: " + e.getMessage());
            searchNameRankFuture = null;
        }
    }

    // 인기 검색어 조회
    // /api/products/search/rank
    @GetMapping("/search/rank")
    public ResponseEntity<?> getSearchNameRank() {
        if (searchNameRankFuture != null && searchNameRankFuture.isDone()) {
            try {
                return ResponseEntity.ok().body(searchNameRankFuture.get().getBody());
            } catch (InterruptedException | ExecutionException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인기 검색어 스케줄링 실패");
            }
        } else {
            return ResponseEntity.ok().body(productService.getSearchNameRank());
        }
    }
}