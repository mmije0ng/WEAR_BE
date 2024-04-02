package com.backend.wear.repository;

import com.backend.wear.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 카테고리와 일치하는 상품 리스트
    // 숨겨진 상품 제외
    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName AND p.isPrivate = false")
    List<Product> findProductsByCategoryName(@Param("categoryName") String categoryName);

    // 카테고리와 일치하는 판매 중인 상품 리스트
    // 숨겨진 상품 제외
    @Modifying
    @Transactional
    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName AND p.postStatus='onSale' AND p.isPrivate = false")
    List<Product> findProductByCategoryNameAndPostStatus(@Param("categoryName") String categoryName);

    //검색어별 상품 조회

    // 사용자 아이디로 상품 조회
    List<Product> findByUserId(Long userId);

    // 판매 상품 중 숨김 상품 조회
    @Modifying
    @Transactional
    @Query("SELECT p FROM Product p WHERE p.user.id = :userId AND p.isPrivate = true")
    List<Product> findByUserIdAndIsPrivateTrue(Long userId);

    //상품 삭제하기
    @Modifying
    @Transactional
    @Query("delete from Product p where p.user.id = :userId and p.id = :productId")
    void deleteByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    // 구매내역
  //  @Query("SELECT p FROM Product p WHERE p.user.id != :userId AND p.productStatus = 'soldOut'")
  //  List<Product> findSoldOutProductsByUserId(Long userId);
}