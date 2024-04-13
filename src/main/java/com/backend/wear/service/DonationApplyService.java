package com.backend.wear.service;

import com.backend.wear.dto.donation.DonationApplyRequestDto;
import com.backend.wear.entity.DonationApply;
import com.backend.wear.entity.EnvironmentLevel;
import com.backend.wear.entity.User;
import com.backend.wear.repository.DonationApplyRepository;
import com.backend.wear.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationApplyService {

    private final UserRepository userRepository;
    private final DonationApplyRepository donationApplyRepository;

    private final Logger log = LoggerFactory.getLogger(DonationApplyService.class);

    @Autowired
    public DonationApplyService(DonationApplyRepository donationApplyRepository,
                                UserRepository userRepository){
        this.donationApplyRepository=donationApplyRepository;
        this.userRepository=userRepository;
    }

    // 기부 신청
    @Transactional
    public void donationApplyService(Long userId, Integer charityNumber, DonationApplyRequestDto dto){
        // 아이디로 사용자 조회 (기부 신청자)
        User applyUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        DonationApply donationApply= DonationApply.builder()
                .user(applyUser)
                .charityNumber(charityNumber)
                .userName(dto.getUserName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .donationItem(dto.getDonationItem())
                .clothesCount(dto.getClothesCount())
                .fashionCount(dto.getFashionCount())
                .boxCount(dto.getBoxCount())
                .build();

        donationApplyRepository.save(donationApply);

        // 환경 점수 추가
        applyUser.setPoint(applyUser.getPoint()+5);
        log.info("업데이트된 포인트: "+applyUser.getPoint());

        checkUserPointLevel(applyUser, applyUser.getPoint());
        log.info("기부 완료후 레벨: "+applyUser.getLevel().getLabel());
    }

    private void checkUserPointLevel(User user, Integer point){
        if(point%100!=0) return;

        switch(point/100){
            case 100:
                user.setLevel( EnvironmentLevel.SAPLING);
                break;
            case 200:
                user.setLevel(EnvironmentLevel.COTTON);
                break;
            case 300:
                user.setLevel(EnvironmentLevel.FLOWER);
                break;
            case 400:
                user.setLevel(EnvironmentLevel.CLOTHES);
                break;
        }
    }

}
