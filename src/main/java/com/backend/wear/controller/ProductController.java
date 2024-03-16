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
    public List<ProductResponseDto> findAllProductsPage(@RequestParam Integer pageNumber){
        Page page = productService.findAllProducts(pageNumber);
        List<ProductResponseDto> productList=page.getContent();

        return productList;
    }

    @GetMapping("/category")
    public List<ProductResponseDto> findProductsByCategoryName(@RequestParam String categoryName,
                                                               @RequestParam String postStatus, @RequestParam Integer pageNumber ){
        List<ProductResponseDto> productList=new ArrayList<>();
        Page page;

        if(postStatus.equals("onale")){
            page=productService.findProductsByCategoryOnSale(categoryName,postStatus,pageNumber);
        }

        else{
            page=productService.findProductsByCategory(categoryName, postStatus,pageNumber);
        }

        productList=page.getContent();

        return productList;
    }
}
