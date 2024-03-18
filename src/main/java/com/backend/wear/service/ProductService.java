package com.backend.wear.service;

import com.backend.wear.dto.ProductPostResponseDto;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.dto.UserPostResponseDto;
import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
import com.backend.wear.entity.Wish;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.repository.UserRepository;
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
        Page<Product> productsPage = productRepository.findAll(pageRequest(pageNumber));

        return productsPage.map(this::mapToProductResponseDto);
    }

    //카테고리별, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductsByCategory(String categoryName, Integer pageNumber ){
        Page<Product> productsPage= productRepository.findByCategory_CategoryName(categoryName,pageRequest(pageNumber));

        return productsPage.map(this::mapToProductResponseDto);
    }

    //카테고리별, 판매중, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductsByCategoryOnSale(String categoryName, String postStatus, Integer pageNumber ){
        Page<Product> productsPage= productRepository.findByPostStatusAndCategory_CategoryName(postStatus,categoryName,pageRequest(pageNumber));

        return productsPage.map(this::mapToProductResponseDto);
    }

    //상품 상세 조회
    @Transactional
    public ProductPostResponseDto getProductPost(Long productId){
        // Long id = Long.valueOf(productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다: " + productId));

       return mapToProductPostResponseDto(product);
    }

    private ProductResponseDto mapToProductResponseDto(Product product) {
        boolean isSelected = wishRepository.findByProductId(product.getId())
                .map(Wish::isSelected) // Optional에 매핑된 isSelected 값을 반환합니다.
                .orElse(false); // 기본값으로 false를 반환합니다.

        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .postStatus(product.getPostStatus())
                .isSelected(isSelected)
                .productImage(product.getProductImage())
                .build();
    }

    private ProductPostResponseDto mapToProductPostResponseDto(Product product) {
        User user = productRepository.findUserById(product.getId()).get();
        UserPostResponseDto seller =mapToUserPostResponseDto(user);

        return ProductPostResponseDto.builder()
                .id(product.getId())
                .seller(seller)
                .price(product.getPrice())
                .productName(product.getProductName())
                .postStatus(product.getPostStatus())
                .productContent(product.getProductContent())
                .productImage(product.getProductImage())
                .place(product.getPlace())
                .build();
    }

    private UserPostResponseDto mapToUserPostResponseDto(User user){
        return UserPostResponseDto.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .profileImage(user.getProfileImage())
                .level(user.getLevel())
                .build();
    }

    //상품 번호로 최신순 페이징
    private Pageable pageRequest(int pageNumber){
        return PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());
    }
}