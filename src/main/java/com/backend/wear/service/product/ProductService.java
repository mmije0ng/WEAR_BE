package com.backend.wear.service.product;

import com.backend.wear.dto.ProductPostResponseDto;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
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
    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, WishRepository wishRepository,
                          UserRepository userRepository){
        this.productRepository=productRepository;
        this.wishRepository=wishRepository;
        this.userRepository=userRepository;
    }

    //전체, 최신순
    @Transactional
    public Page<ProductResponseDto> findAllProducts(Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());

        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(this::mapToProductResponseDto);
    }

    //카테고리별, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductsByCategory(String categoryName, String postStatus, Integer pageNumber ){

        //최신순 페이징
        Pageable pageable= PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());

        Page<Product> productsPage= productRepository.findByCategory_CategoryName(categoryName,pageable);
        return productsPage.map(this::mapToProductResponseDto);
    }

    //카테고리별, 판매중, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductsByCategoryOnSale(String categoryName, String postStatus, Integer pageNumber ){

        //최신순 페이징
        Pageable pageable= PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());

        Page<Product> productsPage= productRepository.findByPostStatusAndCategory_CategoryName(postStatus,categoryName,pageable);
        return productsPage.map(this::mapToProductResponseDto);
    }

    //상품 상세 조회
//    @Transactional
//    public Page<ProductPostResponseDto> getProductPost(Integer productId){
//        User seller = productRepository.findByUserId()
//    }

    private ProductResponseDto mapToProductResponseDto(Product product) {
        boolean isSelected=wishRepository.findByProductId(product.getId()).get().isSelected();

        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .postStatus(product.getPostStatus())
                .isSelected(isSelected)
                .productImage(product.getProductImage())
                .build();
    }
}