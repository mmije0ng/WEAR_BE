package com.backend.wear.entity;

import jakarta.persistence.*;
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

    //차단한 사용자 pk
    @NotNull
    @Column(name="blocked_user_id")
    private Long blockedUserId;

    // 차단하기 버튼을 눌러 다른 사용자를 차단한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Builder
    public BlockedUser(User user, Long blockedUserId){
        this.user = user;
        this.blockedUserId=blockedUserId;
    }
}