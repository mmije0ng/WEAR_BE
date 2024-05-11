package com.backend.wear.service;


import com.backend.wear.dto.donation.DonationApplyResponseDto;
import com.backend.wear.dto.login.SignUpRequestDto;
import com.backend.wear.dto.login.SigunUpResponseDto;
import com.backend.wear.entity.EnvironmentLevel;
import com.backend.wear.entity.Style;
import com.backend.wear.entity.University;
import com.backend.wear.entity.User;
import com.backend.wear.repository.StyleRepository;
import com.backend.wear.repository.UniversityRepository;
import com.backend.wear.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    public LoginService(UserRepository userRepository,
                        StyleRepository styleRepository,
                        UniversityRepository universityRepository){
        this.userRepository=userRepository;
        this.styleRepository=styleRepository;
        this.universityRepository=universityRepository;
    }

//    public SigunUpResponseDto userSignUp(SignUpRequestDto signUpRequestDto){
//        if(signUpRequestDto.getUserPassword().equals(signUpRequestDto.getUserCheckPassword()))
//            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//
//        University university = null;
//
//        // db에 저장되지 않은 처음 인증 요청된 대학이라면
//        if (!universityRepository.findByUniversityName(signUpRequestDto.getUniversityName()).isPresent()){ //새로운 대학
//            university=University.builder()
//                    .universityName(signUpRequestDto.getUniversityName())
//                    .build();
//        }
//
//        else
//            universityRepository.findByUniversityName(signUpRequestDto.getUniversityName());
//
//        User newUser = User.builder()
//                        .userCreatedId(signUpRequestDto.getUserCreatedId())
//                        .userPassword(signUpRequestDto.getUserPassword())
//                        .userName(signUpRequestDto.getUserName())
//                        .nickName(signUpRequestDto.getNickName())
//                        .university(university)
//                        .universityEmail(signUpRequestDto.getUniversityEmail())
//                        .build();
//        // 유저 저장
//        userRepository.save(newUser);
//
//        // 유저 스타일 저장
//
//    }

    //스타일 태그 이름으로 Style 저장
//    public Style mapToProfileStyle (User user, String styleName){
//        return Style.builder()
//                .user(user)
//                .styleName(styleName)
//                .build();
//    }
}
