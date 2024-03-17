package com.backend.wear.controller;

import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

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
            page=productService.findProductsByCategory(categoryName, postStatus,pageNumber);
        }

        return page;
    }
}
