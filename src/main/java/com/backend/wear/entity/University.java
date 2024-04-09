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
    @Column(name="university_name")
    private String universityName;

    // 대학별 환경 점수
    @Column(name="univeristy_point",columnDefinition = "integer default 0")
    private Integer universityPoint;

    @OneToMany(mappedBy = "university", fetch = FetchType.LAZY)
    private List<User> userList = new ArrayList<>();
}

