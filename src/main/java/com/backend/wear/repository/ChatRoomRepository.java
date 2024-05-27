package com.backend.wear.repository;

import com.backend.wear.entity.ChatRoom;
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
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    //채팅방 중복 검사
    Optional<ChatRoom> findByProductIdAndCustomerIdAndSellerId(Long productId, Long customerId, Long sellerId);

    // 로그인한 사용자 아이디로 채팅방 조회
    // 차단한 사용자, 내가 차단당한 경우 제외
    @Query("SELECT c FROM ChatRoom c WHERE (c.customer.id = :userId OR c.seller.id = :userId) " +
            "AND (:blockedUserIdList IS NULL OR (c.seller.id NOT IN (:blockedUserIdList) AND c.customer.id NOT IN (:blockedUserIdList) ) )" +
            "AND (:userIdListBlocked IS NULL OR (c.seller.id NOT IN (:userIdListBlocked) AND c.customer.id NOT IN (:userIdListBlocked) ) )" )
    Page<ChatRoom> findByUserId(@Param("userId") Long userId,
                                @Param("blockedUserIdList") List<Long> blockedUserIdList,
                                @Param("userIdListBlocked") List<Long> userIdListBlocked,
                                Pageable pageable);

    // 나와 채팅중인 판매자 조회
    // 내가 구매자일경우
    @Query("SELECT c.seller FROM ChatRoom c WHERE c.id=:chatRoomId AND c.customer.id=:userId")
    Optional <User> findByChatRoomIdAndCustomerIdBySeller(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);

    // 나와 채팅중인 구매자 조회
    // 내가 판매자일경우
    @Query("SELECT c.customer FROM ChatRoom c WHERE c.id=:chatRoomId AND c.seller.id=:userId")
    Optional <User> findByChatRoomIdAndSellerIdByCustomer(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);
}