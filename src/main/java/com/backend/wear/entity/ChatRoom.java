package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="chatRoom")
@Entity
public class ChatRoom extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //채팅 방 번호
    @Column(name="chat_room_number")
    private Integer chatRoomNumber;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @ToString.Exclude
    private User seller;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private User customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    private Product product;

    //채팅 메시지
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messageList = new ArrayList<>();
}
