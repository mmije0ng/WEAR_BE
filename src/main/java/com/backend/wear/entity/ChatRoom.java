package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name="chat_room")
@Entity
public class ChatRoom extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //채팅 방 번호 (인덱스)
    @Column(name="chat_room_number",columnDefinition = "integer default 0")
    private Integer chatRoomNumber;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @ToString.Exclude
    private User seller;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private User customer;

    @OneToMany(mappedBy = "chat_room", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Product> productList=new ArrayList<>();

    //채팅 메시지
    @OneToMany(mappedBy = "chat_room", cascade = CascadeType.ALL)
    private List<ChatMessage> messageList = new ArrayList<>();
}
