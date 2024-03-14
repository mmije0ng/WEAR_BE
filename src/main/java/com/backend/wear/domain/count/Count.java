package com.backend.wear.domain.count;

import com.backend.wear.domain.BaseEntity;
import com.backend.wear.domain.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name="count")
@Entity
public class Count extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //찜 개수
    @Column(name="wish_count")
    private Integer wishCount;

    //조회수
    @Column(name="post_count")
    private Integer postCount;

    //상품
    @OneToOne
    @JoinColumn(name="product_id")
    Product product;
}