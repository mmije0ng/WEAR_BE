package com.backend.wear.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert //save할 때 null 값을 배제하고 insert
@DynamicUpdate
@Table(name="style")
@Entity
public class Style extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //스타일 태그 이름
    @Column(name="style_name", unique = true)
    private String styleName;

    @OneToMany(mappedBy = "style")
    private List<UserStyle> userStyles = new ArrayList<>();
}