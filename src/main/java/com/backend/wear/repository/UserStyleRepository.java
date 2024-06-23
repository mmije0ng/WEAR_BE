package com.backend.wear.repository;

import com.backend.wear.entity.UserStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserStyleRepository  extends JpaRepository<UserStyle, Long> {

    List<UserStyle> findByUserId(Long userId);

    @Query("SELECT us FROM UserStyle us WHERE us.user.id = :userId order by us.style.id")
    List<UserStyle> findByUserIdOrderByIdStyleId(@Param(value="userId")Long userId);

    @Query("SELECT us FROM UserStyle us WHERE us.user.id = :userId AND us.style.id=:styleId")
    Optional<UserStyle> findByUserIdAndStyleId(@Param(value="userId") Long userId, @Param(value="styleId") Long styleId);

    @Query("SELECT us FROM UserStyle us WHERE us.user.id = :userId")
    List<UserStyle> findAllByUserId(@Param(value="userId") Long userId);

    // 유저 아이디와 스타일 태그 이름으로 삭제
    @Modifying
    @Transactional
    @Query("delete FROM UserStyle us WHERE us.user.id = :userId and us.style.styleName = :styleName")
    void deleteUsersStyleByUserIdAndStyleName(@Param(value="userId") Long userId,@Param(value="styleName") String styleName);
}
