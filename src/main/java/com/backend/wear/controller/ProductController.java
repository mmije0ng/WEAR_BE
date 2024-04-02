package com.backend.wear.controller;

import com.backend.wear.dto.ProductPostRequestDto;
import com.backend.wear.dto.ProductRequestDto;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    // 카테고리별 최신순 조회
    // api/products/category?categoryName={}&userId={}
    @GetMapping("/category")
    public ResponseEntity<?> findAllProductsPage(@RequestParam String categoryName, @RequestParam Long userId)
            throws RuntimeException
    {
        List<ProductResponseDto.ScreenDto> list = productService.findProductsByCategory(categoryName, userId);

        // 카테고리별 상품이 있는 경우
        if(!list.isEmpty()){
            return ResponseEntity.ok(list);
        }

        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("카테고리와 일치하는 상품이 없습니다.");
        }
    }

//    카테고리별 최신순, 판매 상태
//    api/products/category/sale?categoryName={}&userId={}
    @GetMapping("/category/sale")
    public ResponseEntity<?> findProductsByCategoryName(@RequestParam String categoryName,
                                                        @RequestParam Long userId) throws Exception
    {
        List<ProductResponseDto.ScreenDto> list =
                productService.findProductsByCategoryOnSale(categoryName, userId);

        //페이지에 요소가 있는 경우
        if(!list.isEmpty()){
            return ResponseEntity.ok(list);
        }

        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("카테고리와 일치하는 판매중인 상품이 없습니다.");
        }
    }

    // 상품 상세 페이지 불러오기
    // api/products/{productId}
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductPost(@PathVariable("productId") Long productId) throws Exception {
        ProductResponseDto.DetailDto productPost;
        try {
            productPost = productService.getProductPost(productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(productPost);
    }

    // 상품 등록
    @PostMapping("/new/{userId}")
    public ResponseEntity<?> postProductPost(@PathVariable Long userId , @RequestBody @Valid ProductPostRequestDto requestDTO, Errors errors)
            throws Exception{
        try {
            productService.createProductPost(requestDTO,userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    //상품상세/ 상품 정보 수정하기
    @PutMapping("/edit/{userId}/{productId}")
    public ResponseEntity<?> updateProductPost(@PathVariable Long userId , @PathVariable Long productId , @RequestBody @Valid ProductPostRequestDto requestDTO, Errors errors) throws Exception{
            productService.updateProductPost(requestDTO,userId,productId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    //상품 상세/ 상품 판매 상태 변경하기
    @PutMapping("/soldOut/{userId}")
    public ResponseEntity<?> updateProductPostStatus(@PathVariable Long userId,@RequestBody @Valid ProductRequestDto requestDto ,Errors errors) throws Exception{
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
    public ResponseEntity<?> updateProductPostPrivate(@PathVariable Long userId , @PathVariable Long productId) throws Exception{
        try {
            System.out.println(userId);
            System.out.println(productId);
            productService.updateProductPostPrivate(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    //상품 상세/ 상품 삭제하기
    @DeleteMapping("/delete/{userId}/{productId}")
    public ResponseEntity<?> deleteProductPost(@PathVariable Long userId ,@PathVariable Long productId) throws Exception{
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
    public ResponseEntity<?> selectProduct(@PathVariable Long userId ,@PathVariable Long productId) throws Exception{
        try {
            productService.selectProduct(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    // 상품 찜 해제
    @DeleteMapping("/deselect/{userId}/{productId}")
    public ResponseEntity<?> deselectProduct(@PathVariable Long userId ,@PathVariable Long productId) throws Exception{
        try {
            productService.deselectProduct(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}