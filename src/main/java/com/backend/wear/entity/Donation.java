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
    @Column(name="is_donation_complete", columnDefinition = "boolean default false")
    private boolean isDonationComplete;;
}