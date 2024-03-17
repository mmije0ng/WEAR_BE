package com.backend.wear.repository;

import com.backend.wear.entity.Product;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //카테고리별 조회
    Page<Product> findByCategory_CategoryName(String categoryName, Pageable pageable);

    //카테고리별 판매중 조회
    Page<Product> findByPostStatusAndCategory_CategoryName(String postStatus, String categoryName, Pageable pageable);

    //사용자 아이디로 판매자 조회
    User findByUserId(String userId);
}