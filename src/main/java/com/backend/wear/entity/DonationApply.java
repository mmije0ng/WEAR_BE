package com.backend.wear.entity;

import jakarta.persistence.*;
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
@Table(name="donation_apply")
@Entity
public class DonationApply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @ToString.Exclude
    private User user; //기부 신청자

    //기부 단체
    @Column(name="charity_name")
    private Integer charityName;

    //신청자 이름
    private String name;

    //방문 주소
    private String address;

    //휴대 전화
    private String phone;

    //이메일 주소
    private String email;

    //기부 품목
    @Column(name="donation_item")
    private String donationItem;

    //의류 수량
    @Column(name = "clothes_count")
    private Integer clothesCount;

    //잡화 수량
    @Column(name="fashion_count")
    private Integer fashionCount;

    //박스 수량
    @Column(name="box_count")
    private Integer boxCount;
}