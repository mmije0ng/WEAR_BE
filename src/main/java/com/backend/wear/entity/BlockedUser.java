package com.backend.wear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
@Table(name="blocked_users")
@Entity
public class BlockedUser extends BaseEntity { //차단된 사용자

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //차단한 사용자 로그인 아이디
    @NotNull
    @Column(name="user_created_id")
    private String userCreatedId;;

    //닉네임
    @NotNull
    @Column(name="nick_name",unique = true)
    private String nickName;

    //환경 레벨
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="level")
    private EnvironmentLevel level;

    //환경 점수
    @Min(value = 0)
    @Column(name="point")
    private Integer point;

    //프로필 이미지
    @Column(name="profile_image")
    private String profileImage;

    // 차단한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
}
