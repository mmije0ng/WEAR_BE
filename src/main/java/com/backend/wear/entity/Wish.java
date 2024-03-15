package com.backend.wear.entity;

import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name="wish_list")
@Entity
public class Wish extends Serializers.Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //아이디

    //찜 여부
    @Column(name="is_selected")
    private boolean isSelected=false;

    //사용자
    @ManyToOne
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user;

    //상품
    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;
}