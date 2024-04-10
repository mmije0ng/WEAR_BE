package com.backend.wear.repository;

import com.backend.wear.entity.User;
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

    // 유저 아이디와 일칳아는 유저 리스트 반환
    @Query("SELECT u FROM User u WHERE u.id IN (:blockedUserIdList)")
    List<User> findByUserId(@Param("blockedUserIdList")List<Long> blockedUserIdList);
}