package com.backend.wear.controller;

import com.backend.wear.dto.ProductPostResponseDto;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    //예외 처리 필요

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    //전체 상품 최신순 조회
    @GetMapping()
    public Page<ProductResponseDto> findAllProductsPage(@RequestParam Integer pageNumber){
        Page page = productService.findAllProducts(pageNumber);

        return page;
    }

    //카테고리별 최신순, 판매 상태
    @GetMapping("/category")
    public Page<ProductResponseDto> findProductsByCategoryName(@RequestParam String categoryName,
                                                               @RequestParam String postStatus, @RequestParam Integer pageNumber ){
        Page page;

        if(postStatus.equals("onSale")){ //판매 중
            page=productService.findProductsByCategoryOnSale(categoryName,postStatus,pageNumber);
        }

        else{ //판애 완료
            page=productService.findProductsByCategory(categoryName, pageNumber);
        }

        return page;
    }

    //상품 상세 페이지 불러오기
    @GetMapping("/{productId}")
    public ProductPostResponseDto getProductPost(@PathVariable("productId") Long productId){
        ProductPostResponseDto produtPost=productService.getProductPost(productId);

        return  produtPost;
    }
}