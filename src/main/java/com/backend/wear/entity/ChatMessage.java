package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    //채팅방 ID
//    private String roomId;

//    @OneToOne
//    @JoinColumn(name="sender_id")
//    private User sender;
//
//    //채팅 방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    //내용
    private String message;

    //유저 ID
    private Long userId; //유저 pk

    private String userType; // SELLER, CUSTOMER

    //생성시간
    @CreatedDate
    private LocalDateTime sendTime;
}
