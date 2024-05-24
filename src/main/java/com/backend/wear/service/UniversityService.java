package com.backend.wear.service;

import com.backend.wear.dto.ConvertTime;
import com.backend.wear.dto.university.UniversityResponseDto;
import com.backend.wear.entity.University;
import com.backend.wear.entity.User;
import com.backend.wear.repository.DonationApplyRepository;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.repository.UniversityRepository;
import com.backend.wear.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final DonationApplyRepository donationApplyRepository;


    // ObjectMapper 생성
    ObjectMapper objectMapper = new ObjectMapper();

    // JSON 문자열을 String[]으로 변환하는 메서드
    private  String[] convertImageJsonToArray(String productImageJson) {
        if (productImageJson == null) {
            return null;
        }

        try {
            return objectMapper.readValue(productImageJson, String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    @Autowired
    public UniversityService(UniversityRepository universityRepository,
                             UserRepository userRepository,
                             ProductRepository productRepository,
                             DonationApplyRepository donationApplyRepository){
        this.universityRepository=universityRepository;
        this.userRepository=userRepository;
        this.productRepository=productRepository;
        this.donationApplyRepository=donationApplyRepository;
    }

    // 상위 5개 대학 순위 스케줄링
    @Transactional
    public UniversityResponseDto getUniversityRank() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String date = ConvertTime.convertLocalDateTimeToDate(now); //날짜
        String time = ConvertTime.convertLocalDateTimeToTime(now); //시간

        List<University> topUniversityList = universityRepository.findTopUniversity();
        List<University> top5UniversityList = topUniversityList.subList(0, Math.min(5, topUniversityList.size()));

        List<UniversityResponseDto.UniversityInfoDto> universityList = top5UniversityList.stream()
                .map(this::mapToUniversityInfoDto)
                .toList();

        log.info("대학 순위 스케줄링 실행");

        // 1위 대학 매핑
        String firstUniversityName=universityList.get(0).getUniversityName(); //1위 대학 이름
        String firstTotalPoint= String.format("%,d",  top5UniversityList.get(0).getUniversityPoint())+"포인트"; //1위 대학 총 포인트

        List<User> firstUniversityUserList =userRepository.findByUniversity(top5UniversityList.get(0));  // 1위 대학의 모든 유저리스트
        String firstProductCount = Integer.toString(productRepository.findUsersProductCount(firstUniversityUserList))+"번"; //1위 대학 거래횟수
        String firstDonationCount = Integer.toString(
                donationApplyRepository.findUsersDonationApplyCount(firstUniversityUserList))+"번"; //1위 대학 기부횟수

        log.info("거래횟수: "+firstProductCount+", 기부횟수: "+firstDonationCount);

        return UniversityResponseDto.builder()
                .date(date)
                .time(time)
                .universityList(universityList)
                .firstUniversityName(firstUniversityName)
                .firstTotalPoint(firstTotalPoint)
                .firstProductCount(firstProductCount)
                .firstDonationCount(firstDonationCount)
                .build();
    }

    private UniversityResponseDto.UniversityInfoDto mapToUniversityInfoDto(University university){
        String stringUniversityPoint = String.format("%,d", university.getUniversityPoint())+"p";
        log.info("대학 포인트:"+stringUniversityPoint);
        String[] universityImageArray = convertImageJsonToArray(university.getUniversityImage());

        return UniversityResponseDto.UniversityInfoDto.builder()
                .universityName(university.getUniversityName())
                .universityPoint(stringUniversityPoint)
                .universityImage(universityImageArray)
                .build();
    }

    public static String formatIntegerWithCommas(int number) {
        return String.format("%,d", number);
    }
}