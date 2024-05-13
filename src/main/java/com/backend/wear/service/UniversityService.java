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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    public UniversityResponseDto universityRankingSchedule() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        String date = ConvertTime.convertLocalDateTimeToDate(now); //날짜
        String time = ConvertTime.convertLocalDateTimeToTime(now); //시간

        List<University> topUniversityList = universityRepository.findTopUniversity();
        List<University> top5UniversityList = topUniversityList.subList(0, Math.min(5, topUniversityList.size()));

        List<UniversityResponseDto.UniversityInfoDto> universityList = top5UniversityList.stream()
                .map(this::mapToUniversityInfoDto)
                .toList();

        // 1위 대학 매핑
        String firstUniversityName=universityList.get(0).getUniversityName(); //1위 대학 이름
        String firstTotalPoint=universityList.get(0).getUniversityPoint(); //1위 대학 총 포인트

        List<User> firstUniversityUserList =userRepository.findByUniversity(top5UniversityList.get(0));  // 1위 대학의 모든 유저리스트
        Integer firstProductCount = productRepository.findUsersProductCount(firstUniversityUserList); //1위 대학 거래횟수
        Integer firstDonationCount = donationApplyRepository.findUsersDonationApplyCount(firstUniversityUserList); //1위 대학 기부횟수

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

    public UniversityResponseDto.UniversityInfoDto mapToUniversityInfoDto(University university){
        String stringUniversityPoint = formatIntegerWithCommas(university.getUniversityPoint());
        String[] universityImageArray = convertImageJsonToArray(university.getUniversityImage());

        return UniversityResponseDto.UniversityInfoDto.builder()
                .universityName(university.getUniversityName())
                .universityPoint(stringUniversityPoint)
                .universityImage(universityImageArray)
                .build();

    }

    public void setFirstUniversityPoint(University firstUniversity, Integer firstProductCount, Integer firstDonationCount){

    }

    public static String formatIntegerWithCommas(int number) {
        return String.format("%,d", number);
    }
}