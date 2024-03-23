package com.backend.wear.controller;

//import com.backend.wear.dto.LoginDto;
import com.backend.wear.dto.UnivCertFail;
import com.backend.wear.dto.UnivCertResponseDto;
import com.backend.wear.dto.UnivCertRequestDto;
import com.univcert.api.UnivCert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LonginController {

    @Value("${api.key}")
    private String API_KEY;

//    @PostMapping("/signup")
//    public ResponseEntity<?> signUp(@RequestBody LoginDto)

    //대학 인증 메일 발송
    @PostMapping("/university/certify")
    public ResponseEntity<?> certifyUniversity
            (@RequestBody UnivCertRequestDto dto) throws IOException {

        Map<String, Object> response =
                UnivCert.certify(API_KEY, dto.getUniversityEmail(), dto.getUniversityName(), dto.isCheck());

        return ResponseEntity.ok(response);
    }

    //인증 코드 입력
    @PostMapping("/university/certifycode")
    public ResponseEntity<?> certifyUserCode(
        @RequestBody UnivCertRequestDto dto) throws IOException {

        Map<String, Object> response =
                UnivCert.certifyCode(API_KEY, dto.getUniversityEmail(),
                        dto.getUniversityName(), dto.getCode());

        Boolean success = (Boolean) response.get("success");
        if (success) { //인증 성공
            String universityName = (String) response.get("univName");
            String universityEmail = (String) response.get("certified_email");

            UnivCertResponseDto responseDto = new UnivCertResponseDto(universityEmail,universityName,success);

            return ResponseEntity.ok(responseDto);
        }

        else { //인증 실패
            Integer code = (Integer) response.get("code");
            String message = (String) response.get("message");

            UnivCertFail responseDto = new UnivCertFail(code,success,message);
            return ResponseEntity.ok(responseDto);
        }
    }
}
