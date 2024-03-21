package com.backend.wear.repository;

import com.backend.wear.entity.Category;
import com.backend.wear.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //카테고리 이름 (categoryName)으로 해당 카테고리 조회
    Category findByCategoryName(String categoryName);
}