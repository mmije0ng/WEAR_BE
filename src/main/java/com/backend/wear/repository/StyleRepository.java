package com.backend.wear.repository;

import com.backend.wear.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {
    // 사용자 아이디와 일치하는 Style 목록 가져오기
    List<Style> findByUserId(Long userId);

    Optional<Style> findByStyleNameAndUserId(String styleName, Long userId);
}