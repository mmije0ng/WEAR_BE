package com.backend.wear.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name="university")
@Entity
public class University extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //대학교 학생들
    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<User> userList = new ArrayList<>();

    //대학교 환경 점수, 기부 횟수, 등..
}
