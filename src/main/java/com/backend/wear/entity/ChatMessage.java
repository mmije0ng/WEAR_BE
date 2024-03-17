package com.backend.wear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Getter
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

    //채팅 메시지
    @NotNull
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
    @Column(name="is_checked", columnDefinition = "boolean default false")
    private boolean isChecked;

    @Column(name="is_alert", columnDefinition = "boolean default true" )
    private boolean isAlert;
}
