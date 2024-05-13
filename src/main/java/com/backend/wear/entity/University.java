package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name="university")
@Entity
public class University extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 대학 이름
    @Column(name="university_name", unique = true)
    private String universityName;

    // 대학 이미지
    @Column(name="university_image", columnDefinition = "json")
    private String universityImage;

    @Column(name="university_point",columnDefinition = "integer default 0")
    private Integer universityPoint;

    @OneToMany(mappedBy = "university")
    private List<User> userList = new ArrayList<>();
}

