package com.backend.wear.domain.chat;

import com.backend.wear.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@Entity
@Table(name = "chatMessage")
public class ChatMessage {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //채팅 메시지
    private String message;

    //메시지 보낸 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sender_id")
    private User sender;

    //받는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver_id")
    private User receiver;

    //채팅 방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    //읽음 여부
    private boolean isChecked;
}
