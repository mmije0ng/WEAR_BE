package com.backend.wear.domain.product;

import com.backend.wear.domain.BaseEntity;
import com.backend.wear.domain.category.Category;
import com.backend.wear.domain.chat.ChatRoom;
import com.backend.wear.domain.count.Count;
import com.backend.wear.domain.wish.Wish;
import jakarta.persistence.*;
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
    private String productName;

    //상품 가격
    private Integer price;

    //상품 이미지, 5개까지
    private String productImage1;
    private String productImage2;
    private String productImage3;
    private String productImage4;
    private String productImage5;

    //상품 내용, 설명
    private String productContent;

    //상품 상태
    private String productStatus;

    //거래 상태
    private boolean postStatus;

    //거래 장소
    private String place;

    //상품 숨김 여부
    private boolean isPrivate;

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

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<ChatRoom> chatRoomList=new ArrayList<>();
}
