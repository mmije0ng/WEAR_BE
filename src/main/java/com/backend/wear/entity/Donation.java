package com.backend.wear.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Column(name="donation_number")
    private Integer donationNumber;

    //기부 장소
    @NotNull
    @Column(name="donation_place")
    private String donationPlace;

    //기부 인증 이미지
    @NotNull
    @Column(name="donation_image")
    private String donationImage;

    //기부 상태, 완료/미완료
    @Column(name="donation_status")
    private boolean donationStatus=false;
}