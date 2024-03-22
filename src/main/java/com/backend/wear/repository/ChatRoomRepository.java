package com.backend.wear.repository;

import com.backend.wear.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
  
    //판매자 또는 구매자
    List<ChatRoom> findBySellerIdOrCustomerId(Long sellerId, Long customerId);

    //채팅방 중복 검사
    Optional<ChatRoom> findByProductIdAndCustomerId(Long productId, Long customerId);

}