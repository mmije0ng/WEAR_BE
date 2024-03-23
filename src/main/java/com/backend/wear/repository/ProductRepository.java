package com.backend.wear.repository;

import com.backend.wear.entity.Product;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // isPrivate가 false인 상품들만 반환
    Page<Product> findByIsPrivateFalse(Pageable pageable);

    // 카테고리별로 isPrivate가 false인 상품 조회
    Page<Product> findByCategory_CategoryNameAndIsPrivateFalse(String categoryName, Pageable pageable);

    // 판매 상태와 카테고리별로 isPrivate가 false인 상품 조회
    Page<Product> findByPostStatusAndCategory_CategoryNameAndIsPrivateFalse(String postStatus, String categoryName, Pageable pageable);

    //판매중인 상품만 조회
    Page<Product> findByPostStatusAndIsPrivateFalse(String postStatus, Pageable pageable);


    //검색어별 상품 조회

    //사용자 아이디로 판매자 조회
    User findUserById(Long productId);

    List<Product> findByUserId(Long userId);

    // User 클래스의 id와 Product 클래스의 isPrivate 필드가 true
    List<Product> findByUser_IdAndIsPrivateTrue(Long userId);

    //상품 삭제하기
    @Modifying
    @Transactional
    @Query("delete from Product p where p.user.id = :userId and p.id = :productId")
    void deleteByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

}