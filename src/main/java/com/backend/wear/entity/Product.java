package com.backend.wear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name="product")
@Entity
public class Product extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //상품 이름, 제목
    @NotNull
    @Column(name="product_name")
    private String productName;

    //상품 가격
    @NotNull
    private Integer price;

    //상품 이미지
    @NotNull
    @Column(name="product_image")
    private String productImage;

    //상품 내용, 설명
    @NotNull
    @Column(name="product_content")
    private String productContent;

    //상품 상태
    @NotNull
    @Column(name="product_status")
    private String productStatus;

    //거래 상태
    //null이면 거래 완료, onSale이면 판매 중
    @Column(name="post_status")
    private String postStatus;

    //거래 장소
    @NotNull
    private String place;

    //상품 숨김 여부
    @Column(name="is_private")
    private boolean isPrivate=false;

    //카테고리
    @ManyToOne
    @JoinColumn(name="category_id")
    @ToString.Exclude
    private Category category;

    //찜
    @OneToOne(mappedBy = "product")
    @ToString.Exclude
    private Wish wish;

    //찜 횟수, 조회수
    @OneToOne(mappedBy = "product")
    @ToString.Exclude
    private Count count;

    //채팅방
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ChatRoom> chatRoomList=new ArrayList<>();
}