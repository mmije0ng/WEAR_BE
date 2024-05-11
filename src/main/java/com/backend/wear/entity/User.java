package com.backend.wear.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
@Table(name="users")
@Entity
public class User extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //사용자 로그인 아이디
    @NotNull
    @Column(name="user_created_id", unique = true)
    private String userCreatedId;;

    //사용자 패스워드
    @NotNull
    @Column(name="user_password", unique = true)
    private String userPassword;

    //이름
    @NotNull
    @Column(name="user_name")
    private String userName;

    //닉네임
    @NotNull
    @Column(name="nick_name", unique = true)
    private String nickName;

    //대학교 이메일
    @Column(name="university_email", unique = true)
    private String universityEmail;

    //환경 점수
    @Min(value = 0)
    @Max(value = 500)
    @Column(name="point",columnDefinition = "integer default 0")
    private Integer point;

    //환경 레벨
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="level", columnDefinition = "varchar(10) default 'SEED'")
    private EnvironmentLevel level;

    //스타일
    @OneToMany(mappedBy = "user")
    private List<UserStyle> userStyles = new ArrayList<>();

    //프로필 이미지
    @Column(name="profile_image")
    private String profileImage;

    //대학교
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="university_id")
    University university;

    //판매 내역
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval  = true)
    private List<Product> productList = new ArrayList<>();

    //기부 내역
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval  = true)
    private List<Donation> donationList=new ArrayList<>();

    //기부 신청 내역
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval  = true)
    private List<DonationApply> donationApplyList=new ArrayList<>();

    //찜목록
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval  = true)
    private List<Wish> wishList=new ArrayList<>();

    public String getMyUserName(){
        return userName;
    }
}