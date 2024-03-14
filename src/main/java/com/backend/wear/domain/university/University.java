package com.backend.wear.domain.university;

import com.backend.wear.domain.BaseEntity;
import com.backend.wear.domain.user.User;
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
    @OneToMany(mappedBy = "university")
    @ToString.Exclude
    private List<User> userList = new ArrayList<>();
}
