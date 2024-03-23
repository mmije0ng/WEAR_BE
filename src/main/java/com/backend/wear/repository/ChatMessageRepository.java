package com.backend.wear.repository;

import com.backend.wear.entity.ChatMessage;
import com.backend.wear.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>  {
}