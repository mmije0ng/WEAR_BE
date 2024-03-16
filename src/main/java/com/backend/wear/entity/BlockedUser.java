package com.backend.wear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    //닉네임
    @NotNull
    @Column(name="nick_name",unique = true)
    private String nickName;

    //환경 레벨
    @NotNull
    @Column(name="environment_level", columnDefinition = "varchar(255) default '새싹'")
    private String environmentLevel;

    //프로필 이미지
    @NotNull
    @Column(name="profile_image")
    private String profileImage;
}
