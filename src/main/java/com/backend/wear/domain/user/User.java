package com.backend.wear.domain.user;


import com.backend.wear.domain.BaseEntity;
import com.backend.wear.domain.donation.Donation;
import com.backend.wear.domain.university.University;
import com.backend.wear.domain.wish.Wish;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name="users")
@Entity
public class User extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //사용자 아이디
    @NotNull
    @Column(name="user_id",unique = true)
    private Integer userId;

    //사용자 패스워드
    @NotNull
    @Column(name="user_password",unique = true)
    private Integer userPassword;

    //이름
    @NotNull
    @Column(unique = true)
    private String name;

    //닉네임
    @NotNull
    @Column(name="nick_name",unique = true)
    private String nickName;

    //대학교 이메일
    @NotNull
    @Column(name="university_email",unique = true)
    private String universityEmail;

    //환경 점수
    @NotNull
    @Column(name="environment_point")
    private Integer environmentPoint;

    //환경 레벨
    @NotNull
    @Column(name="environment_level")
    private String environmentLevel;

    //프로필 이미지
    @NotNull
    @Column(name="profile_image")
    private String profileImage;

    //대학교
    @ManyToOne
    @JoinColumn(name="university_id")
    @ToString.Exclude
    University university;

    //기부 내역
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Donation> donationList=new ArrayList<>();

    //찜목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Wish> wishList;

    //거래 횟수
    //기부 횟수
}