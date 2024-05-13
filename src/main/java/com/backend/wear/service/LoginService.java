package com.backend.wear.service;


import com.backend.wear.dto.login.SignUpRequestDto;
import com.backend.wear.dto.login.SignUpResponseDto;
import com.backend.wear.dto.login.UniversityCertifyRequestDto;
import com.backend.wear.entity.*;
import com.backend.wear.repository.StyleRepository;
import com.backend.wear.repository.UniversityRepository;
import com.backend.wear.repository.UserRepository;
import com.backend.wear.repository.UserStyleRepository;
import com.univcert.api.UnivCert;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoginService {

  //  private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;
    private final UserStyleRepository userStyleRepository;
    private final StyleRepository styleRepository;
    private final UniversityRepository universityRepository;

    @Value("${api.key}")
    private String API_KEY;

    @Autowired
    public LoginService(/*BCryptPasswordEncoder bCryptPasswordEncoder,*/
                        UserRepository userRepository,
                        UserStyleRepository userStyleRepository,
                        StyleRepository styleRepository,
                        UniversityRepository universityRepository){
      //  this.bCryptPasswordEncoder=bCryptPasswordEncoder;
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
                            .universityImage(null)
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

    // 대학교 인증 메일 발송
    public Object certifyUniversity(UniversityCertifyRequestDto.CertifyDto certifyDto) throws IOException {
        // 인증된 유저 리스트
        UnivCert.list(API_KEY);

  //      UnivCert.clear(API_KEY);

        // 인증 여부
        Map<String, Object> statusMap = UnivCert.status(API_KEY, certifyDto.getEmail());
        // 이미 인증된 사용자일 경우
        if(statusMap.get("success").equals(true)){
            statusMap.put("code", 400);
            statusMap.put("success", false);
            statusMap.put("already_certified",true); //이미 인증된 이메일 여부
            statusMap.put("message","이미 인증된 이메일입니다.");
            return  statusMap;
        }

        Map<String, Object> certifyMap = UnivCert.certify(API_KEY, certifyDto.getEmail(), certifyDto.getUniversityName(), true);
        if(certifyMap.get("success").equals(true)){
            certifyMap.put("message","인증 메일 발송 완료.");
        }

        else if(certifyMap.get("message").equals("서버에 존재하지 않는 대학명입니다. univ_check 값을 false로 바꿔서 진행해주세요.")){
            certifyMap.put("message","존재하지 않는 대학명입니다. 대학명을 정확히 입력해주세요.");
        }

        certifyMap.put("already_certified",false);

        return certifyMap;
    }

    // 대학교 인증 코드 입력
    public Object certifyCode(UniversityCertifyRequestDto.CertifyCodeDto certifyCodeDto) throws IOException{
        Map<String, Object> certifyCodeMap = UnivCert.certifyCode(API_KEY,
                certifyCodeDto.getEmail(), certifyCodeDto.getUniversityName(),certifyCodeDto.getCode());

        certifyCodeMap.put("university_name", certifyCodeMap.get("univName"));

        return certifyCodeMap;
    }

    // 스타일 태그 이름으로 Style 저장
    public UserStyle mapToProfileStyle(User user, Style style) {
        return UserStyle.builder()
                .user(user)
                .style(style)
                .build();
    }
}
