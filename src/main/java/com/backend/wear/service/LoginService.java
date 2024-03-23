package com.backend.wear.service;

import com.backend.wear.dto.LoginDto;
import com.backend.wear.dto.LoginResponseDto;
import com.backend.wear.entity.User;
import com.backend.wear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public LoginResponseDto loginByUser(LoginDto loginDto){
        String userId= loginDto.getUserId();
        User user= userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "사용자를 찾지 못하였습니다."));

        String userPassword=loginDto.getUserPassword();
        String password=user.getUserPassword();

        //비밀번호 입력 안 함
        if(userPassword.isEmpty()){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //비밀번호 일치X
        if(!userPassword.equals(password))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        else {//로그인 성공, 유저 pk 반환
            LoginResponseDto dto=LoginResponseDto.builder()
                    .id(user.getId())
                    .message("로그인 성공")
                    .build();
            return dto;
        }
    }

    //대학 인증 메일 발송
//    @PostMapping("/university/certify")
//    public ResponseEntity<?> certifyUniversity
//    (@RequestBody UnivCertRequestDto dto) throws IOException {
//
//        Map<String, Object> response =
//                UnivCert.certify(API_KEY, dto.getUniversityEmail(), dto.getUniversityName(), dto.isCheck());
//
//        return ResponseEntity.ok(response);
//    }
//
//    //메일로 발송된 인증 코드 입력
//    @PostMapping("/university/certifycode")
//    public ResponseEntity<?> certifyUserCode(
//            @RequestBody UnivCertRequestDto dto) throws IOException {
//
//        Map<String, Object> response =
//                UnivCert.certifyCode(API_KEY, dto.getUniversityEmail(),
//                        dto.getUniversityName(), dto.getCode());
//
//        Boolean success = (Boolean) response.get("success");
//        if (success) { //인증 성공
//            String universityName = (String) response.get("univName");
//            String universityEmail = (String) response.get("certified_email");
//
//            UnivCertResponseDto responseDto = new UnivCertResponseDto(universityEmail, universityName, success);
//
//            return ResponseEntity.ok(responseDto);
//        } else { //인증 실패
//            Integer code = (Integer) response.get("code");
//            String message = (String) response.get("message");
//
//            UnivCertFail responseDto = new UnivCertFail(code, success, message);
//            return ResponseEntity.ok(responseDto);
//        }
//    }

}
