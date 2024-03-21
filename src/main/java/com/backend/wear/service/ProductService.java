package com.backend.wear.service;

import com.backend.wear.dto.ProductPostRequestDto;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.dto.UserResponseDto;
import com.backend.wear.entity.Category;
import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
import com.backend.wear.entity.Wish;
import com.backend.wear.repository.CategoryRepository;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.repository.UserRepository;
import com.backend.wear.repository.WishRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, WishRepository wishRepository, UserRepository userRepository,CategoryRepository categoryRepository){
        this.productRepository=productRepository;
        this.wishRepository=wishRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    //카테고리별, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductsByCategory(String categoryName, Integer pageNumber ){
        Page<Product> productsPage;

        //카테고리가 전체 일 때
        if(categoryName.equals("전체")){
            productsPage = productRepository.findAll(pageRequest(pageNumber));

            return productsPage.map(this::mapToProductResponseDto);
        }

        else{ //카테고리별
            productsPage = productRepository.findByCategory_CategoryName(categoryName,pageRequest(pageNumber));

            return productsPage.map(this::mapToProductResponseDto);
        }
    }


    //카테고리별, 판매중, 최신순
    @Transactional
    public Page<ProductResponseDto> findProductsByCategoryOnSale(String categoryName, String postStatus, Integer pageNumber ){
        Page<Product> productsPage;

        //전체, 판매중, 최신순
        if(categoryName.equals("전체")){
            productsPage=productRepository
                    .findByPostStatus(postStatus,pageRequest(pageNumber));
        }

        //카테고리별 판매중 최신순
        else{
            productsPage =productRepository
                .findByPostStatusAndCategory_CategoryName(postStatus,categoryName,pageRequest(pageNumber));
        }

        return productsPage.map(this::mapToProductResponseDto);
    }

    //상품 상세 조회
    @Transactional
    public ProductResponseDto getProductPost(Long productId){
        Product product  = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "상품을 찾지 못하였습니다."));

        return mapToProductPostResponseDto(product);
    }

    private ProductResponseDto mapToProductResponseDto(Product product) {
        boolean isSelected = wishRepository.findByProductId(product.getId())
                .map(Wish::isSelected) // Optional에 매핑된 isSelected 값을 반환
                .orElse(false); // 기본값으로 false를 반 환

        return ProductResponseDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .postStatus(product.getPostStatus())
                .isSelected(isSelected)
                .productImage(product.getProductImage())
                .build();
    }

    //상세 페이지 상품 dto
    private ProductResponseDto mapToProductPostResponseDto(Product product) {
        User user=product.getUser();
        //판매자
        UserResponseDto seller =mapToUserPostResponseDto(user);

        //상품
        return ProductResponseDto.builder()
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

    //상품 상세 페이지에서 사용자 정보
    private UserResponseDto mapToUserPostResponseDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .profileImage(user.getProfileImage())
                .level(user.getLevel().getLabel())
                .build();
    }

    //상품 번호로 최신순 페이징
    private Pageable pageRequest(int pageNumber){
        return PageRequest.of(pageNumber,12,
                Sort.by("updatedAt").descending());
    }

    //상품 등록하기
    @Transactional
    public void createProductPost(ProductPostRequestDto requestDTO, Long userId) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾지 못하였습니다."));

            // Create a new Product object using data from the provided Product object
            Product newProduct = Product.builder()
                    .productName(requestDTO.getProductName())
                    .price(requestDTO.getPrice())
                    .productImage(requestDTO.getProductImage())
                    .productContent(requestDTO.getProductContent())
                    .productStatus(requestDTO.getProductStatus())
                    .place(requestDTO.getPlace())
                    .user(user)
                    .category(category)
                    .build();

            productRepository.save(newProduct);

    }

}