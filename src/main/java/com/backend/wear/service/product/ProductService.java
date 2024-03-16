package com.backend.wear.service.product;

import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.entity.Product;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.repository.WishRepository;
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
    private final WishRepository wishRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, WishRepository wishRepository){
        this.productRepository=productRepository;
        this.wishRepository=wishRepository;
    }

    //전체, 최신순
    @Transactional
    public Page<ProductResponseDto> findAllProducts(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());

        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(this::mapToProductResponseDto);
    }

    private ProductResponseDto mapToProductResponseDto(Product product) {
        boolean isSelected=wishRepository.findByProductId(product.getId()).get().isSelected();
        System.out.println("선택: "+isSelected);
        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .postStatus(product.getPostStatus())
                .isSelected(isSelected)
                .productImage(product.getProductImage())
                .build();
    }

    //카테고리별, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductsByCategory(String categoryName, String postStatus, Integer pageNumber ){

        //최신순 페이징
        Pageable pageable= PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());

        return productRepository.findByCategory_CategoryName(categoryName,pageable);
    }

    //카테고리별, 판매중, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductsByCategoryOnSale(String categoryName, String postStatus, Integer pageNumber ){

        //최신순 페이징
        Pageable pageable= PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());

        return productRepository.findByPostStatusAndCategory_CategoryName(postStatus,categoryName,pageable);
    }

    //상품 등록하기
}