package com.backend.wear.domain.user;

import com.backend.wear.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name="blocked_users")
@Entity
public class BlockedUser extends BaseEntity { //차단된 사용자

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //닉네임
    private String nickName;

    //환경 레벨
    private String environmentLevel;

    //프로필 이미지
    private String profileImage;
}
