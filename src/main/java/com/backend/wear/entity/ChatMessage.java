package com.backend.wear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type;

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    //채팅방 ID
//    private String roomId;

    @OneToOne
    @JoinColumn(name="sender_id")
    private User sender;

    //채팅 방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    //내용
    private String message;

    @Column(nullable = false)
    private LocalDateTime sendTime;

//    //메시지 보낸 사람
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="sender_id")
//    private User sender;
//
//    //읽음 여부
//    @Column(name="is_checked", columnDefinition = "boolean default false")
//    private boolean isChecked;
//
//    @Column(name="is_alert", columnDefinition = "boolean default true" )
//    private boolean isAlert;
}
