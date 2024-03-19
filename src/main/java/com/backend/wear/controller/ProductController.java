package com.backend.wear.controller;

import com.backend.wear.dto.ProductPostResponseDto;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    //카테고리별 최신순 조회

    @GetMapping("/category")
    public ResponseEntity<?> findAllProductsPage(@RequestParam String categoryName,@RequestParam Integer pageNumber)
    {
        System.out.println(categoryName);
        Page page = productService.findProductsByCategory(categoryName, pageNumber);

        //페이지에 요소가 있는 경우
        if(!page.isEmpty()){
            return ResponseEntity.ok(page);
        }

        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("카테고리와 일치하는 상품이 없습니다.");
        }
    }

    //카테고리별 최신순, 판매 상태

    @GetMapping("/category/sale")
    public ResponseEntity<?> findProductsByCategoryName(@RequestParam String categoryName,
                                                        @RequestParam String postStatus, @RequestParam Integer pageNumber ){
        Page page=productService.findProductsByCategoryOnSale(categoryName,postStatus,pageNumber);

        //페이지에 요소가 있는 경우
        if(!page.isEmpty()){
            return ResponseEntity.ok(page);
        }

        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("카테고리와 일치하는 판매중인 상품이 없습니다.");
        }
    }

    //상품 상세 페이지 불러오기
    // api/products/{productId}
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductPost(@PathVariable("productId") Long productId){
        ProductPostResponseDto productPost;
        try {
            productPost = productService.getProductPost(productId);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ex.getMessage());
        }
        return ResponseEntity.ok(productPost);
    }
}