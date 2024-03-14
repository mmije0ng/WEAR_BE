package com.backend.wear.domain.donation;


import com.backend.wear.domain.BaseEntity;
import com.backend.wear.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Table(name="donation")
@Entity
public class Donation extends BaseEntity {

    //아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //기부한 사람
    @ManyToOne
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user;

    //기부 번호
    private Integer donationNumber;

    //기부 장소
    private String donationPlace;

    //기부 상태, 완료/미완료
    private boolean donationStatus;

    //기부 인증 이미지
    private String donationImage;
}