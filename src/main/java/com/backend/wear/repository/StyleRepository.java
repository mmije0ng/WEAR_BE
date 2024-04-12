package com.backend.wear.repository;

import com.backend.wear.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StyleRepository extends JpaRepository<Style, Long> {
    // 사용자 아이디와 일치하는 Style 목록 가져오기
    List<Style> findByUserId(Long userId);

    // 변경할 스타일 리스트에는 없지만 스타일 테이블에는 존재하는 스타일 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Style s WHERE s.user.id = :userId AND s.styleName NOT IN :styleNameList")
    void deleteByUserIdAndStyleNameNotIn(@Param("userId") Long userId, @Param("styleNameList") List<String> styleNameList);

    Style findStyleByStyleNameAndUserId(String styleName, Long userId);
}