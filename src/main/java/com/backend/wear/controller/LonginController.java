package com.backend.wear.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LonginController {
//
//    private final LoginService loginService;
//
//    private String myUniversityName;
//    private String myUniversityEmail;
//
//    @Value("${api.key}")
//    private String API_KEY;
//
//    @Autowired
//    public LonginController (LoginService loginService){
//
//        this.loginService=loginService;
//    }

    // api/login
//    @PostMapping("/login")
//    public ResponseEntity<?> myLogin(@RequestBody LoginDto loginDto){
//        try{
//            LoginResponseDto dto=loginService.loginByUser(loginDto);
//             return ResponseEntity.ok(dto);
//        }
//
//        catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(e.getMessage());
//        }
//    }

    // api/signup
//    @PostMapping("/signup")
//    public ResponseEntity<?> signUp(@RequestBody SignUpDto signupDto){
//        try{
//            loginService.userSignUp(signupDto, myUniversityName,
//                    myUniversityEmail);
//            return ResponseEntity.ok(signupDto);
//        }
//
//        catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(e.getMessage());
//        }
//    }
//
//    //대학 인증 메일 발송
//    @PostMapping("/university/certify")
//    public ResponseEntity<?> certifyUniversity(@RequestBody UnivCertRequestDto dto) throws IOException {
//        Map<String, Object> response =
//                UnivCert.certify("7703b0e5-a8d1-4bb1-8463-76f0b422199a", dto.getUniversityEmail(), dto.getUniversityName(), dto.isCheck());
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
//                UnivCert.certifyCode("8664efc6-ab71-47b9-8cb0-5aa20dccc59c", dto.getUniversityEmail(),
//                        dto.getUniversityName(), dto.getCode());
//
//        Boolean success = (Boolean) response.get("success");
//        if (success) { //인증 성공
//            String universityName = (String) response.get("univName");
//            String universityEmail = (String) response.get("certified_email");
//
//            UnivCertResponseDto responseDto = new UnivCertResponseDto(universityEmail, universityName, success);
//            myUniversityName=responseDto.getUniversityName();
//            myUniversityEmail=responseDto.getUniversityEmail();
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
