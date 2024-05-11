package com.backend.wear.service;


import com.backend.wear.dto.login.SignUpRequestDto;
import com.backend.wear.dto.login.SignUpResponseDto;
import com.backend.wear.entity.*;
import com.backend.wear.repository.StyleRepository;
import com.backend.wear.repository.UniversityRepository;
import com.backend.wear.repository.UserRepository;
import com.backend.wear.repository.UserStyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final UserStyleRepository userStyleRepository;
    private final StyleRepository styleRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    public LoginService(UserRepository userRepository,
                        UserStyleRepository userStyleRepository,
                        StyleRepository styleRepository,
                        UniversityRepository universityRepository){
        this.userRepository=userRepository;
        this.userStyleRepository=userStyleRepository;
        this.styleRepository=styleRepository;
        this.universityRepository=universityRepository;
    }

    public SignUpResponseDto userSignUp(SignUpRequestDto signUpRequestDto) {
        if (!signUpRequestDto.getUserPassword().equals(signUpRequestDto.getUserCheckPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        University university = universityRepository.findByUniversityName(signUpRequestDto.getUniversityName())
                .orElseGet(() -> {
                    University newUniversity = University.builder()
                            .universityName(signUpRequestDto.getUniversityName())
                            .build();
                    return universityRepository.save(newUniversity);
                });

        User newUser = User.builder()
                .userCreatedId(signUpRequestDto.getUserCreatedId())
                .userPassword(signUpRequestDto.getUserPassword())
                .userName(signUpRequestDto.getUserName())
                .nickName(signUpRequestDto.getNickName())
                .university(university)
                .universityEmail(signUpRequestDto.getUniversityEmail())
                .build();

        // 유저 저장
        userRepository.save(newUser);

        // 유저 스타일 저장
        List<UserStyle> userStyles = signUpRequestDto.getStyleNameList().stream()
                .map(styleName -> styleRepository.findByStyleName(styleName)
                        .orElseThrow(() -> new IllegalArgumentException("없는 스타일 태그 이름입니다.")))
                .map(style -> mapToProfileStyle(newUser, style))
                .collect(Collectors.toList());

        // UserStyle 저장
        userStyleRepository.saveAll(userStyles);

        return SignUpResponseDto.builder()
                .signUpSuccess(true)
                .userId(newUser.getId())
                .build();
    }

    // 스타일 태그 이름으로 Style 저장
    public UserStyle mapToProfileStyle(User user, Style style) {
        return UserStyle.builder()
                .user(user)
                .style(style)
                .build();
    }
}
