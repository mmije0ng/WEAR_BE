package com.backend.wear.domain.category;

import com.backend.wear.domain.BaseEntity;
import com.backend.wear.domain.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name="category")
@Entity
public class Category extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //카테고리명
    @Column(name="category-name")
    private String categoryName;

    //카테고리별 상품 리스트
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Product> productList;
}