package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅 방 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    // 메시지 내용
    private String content;

    // 메시지 내용 (이미지)
    @Column(name="content_image", columnDefinition = "json")
    private String contentImage;

    // 보낸사람 pk
    private Long senderId;

    // 보낸사람 타입
    // seller, customer
    private String userType;

    // 보낸 시간
    private String timestamp;

    // 보낸 시간을 LocalDateTime 으로 변경
    private LocalDateTime sendTime;
}
