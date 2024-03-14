package com.backend.wear.domain.wish;

import com.backend.wear.domain.product.Product;
import com.backend.wear.domain.user.User;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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