package com.backend.wear.service.product;

import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    //최신순
   @Transactional
   public Page<ProductResponseDto> selectAllProduct(Integer pageNumber){
       Pageable pageable= PageRequest.of(pageNumber,12,
               Sort.by("updatedAt").descending());

       return productRepository.findAllProducts(pageable);
   }

    //카테고리별, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductByCategory(String categoryName, String postStatus, Integer pageNumber ){

        //최신순 페이징
        Pageable pageable= PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());

        return productRepository.findByCategory_CategoryName(categoryName,pageable);
    }

    //카테고리별, 판매중, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductByCategoryNotNull(String categoryName, String postStatus, Integer pageNumber ){

        //최신순 페이징
        Pageable pageable= PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());

        return productRepository.findByPostStatusAndCategory_CategoryName(postStatus,categoryName,pageable);
    }

    //상품 등록하기

}