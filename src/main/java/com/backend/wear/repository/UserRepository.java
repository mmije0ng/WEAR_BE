package com.backend.wear.repository;

import com.backend.wear.entity.University;
import com.backend.wear.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 //   List<ChatRoom> findChatRoomListByUserId(Long userId);

    Optional<User> findByUserCreatedId(String userCreatedId);

    // 사용자들의 모든 로그인 아이디 반환
    @Query("SELECT u.userCreatedId FROM User u")
    List<String> findUserCreatedIdList();

    // 사용자들의 모든 대학 이메일 반환
    @Query("SELECT u.universityEmail FROM User u")
    List<String> findUserUniversityEmailList();

    // 차단 유저들 불러오기
    // 유저 아이디와 일치아는 유저 리스트 반환
    @Query("SELECT u FROM User u WHERE u.id IN (:blockedUsersIdList)")
    Page <User> findByUserId(@Param("blockedUsersIdList")List<Long> blockedUsersIdList, Pageable pageable);

    // 대학과 일치하는 모든 유저 리스트
    List<User> findByUniversity(University university);

    @Query("SELECT u FROM User u where u.id<=20")
    List<User> findUserPasswordList();
}