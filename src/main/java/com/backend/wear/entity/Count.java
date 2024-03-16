package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Table(name="count")
@Entity
public class Count extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //찜 개수
    @Column(name="wish_count", columnDefinition = "integer default 0")
    private Integer wishCount;

    //조회수
    @Column(name="post_count", columnDefinition = "integer default 0")
    private Integer postCount;

    //상품
    @OneToOne(fetch = FetchType.LAZY )
    @JoinColumn(name="product_id")
    Product product;
}