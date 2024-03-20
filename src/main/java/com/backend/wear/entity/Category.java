package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name="category")
@Entity
public class Category extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //카테고리명
    @Column(name = "category_name", columnDefinition = "varchar(255) default '전체'")
    private String categoryName;

    //카테고리별 상품 리스트
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Product> productList;
}