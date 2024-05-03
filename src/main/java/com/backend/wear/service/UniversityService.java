package com.backend.wear.service;

import com.backend.wear.dto.university.UniversityRequestDto;
import com.univcert.api.UnivCert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class UniversityService {
    @Value("${api.key}")
    private String API_KEY;

    // 대학교 인증 메일 발송
    public Object certifyUniversity(UniversityRequestDto.CertifyDto certifyDto) throws IOException{
        // 인증된 유저 리스트
        UnivCert.list(API_KEY);

        // 인증 여부
        Map<String, Object> statusMap = UnivCert.status(API_KEY, certifyDto.getEmail());
        // 이미 인증된 사용자일 경우
        if(statusMap.get("success").equals(true)){
            statusMap.put("success", false);
            statusMap.put("message","이미 인증된 이메일입니다.");
            return  statusMap;
        }

        Map<String, Object> certifyMap = UnivCert.certify(API_KEY, certifyDto.getEmail(), certifyDto.getUniversityName(), true);
        if(certifyMap.get("success").equals(false))
            certifyMap.put("message","대학교명을 잘 못 입력하였습니다. 올바른 대학교명을 입력해주세요.");

        return certifyMap;

   //     UnivCert.clear(API_KEY);
  //      UnivCert.clear(API_KEY, univCertDto.getEmail());
    }

    // 대학교 인증 코드 입력
    public Object certifyCode(UniversityRequestDto.CertifyCodeDto certifyCodeDto) throws IOException{

        Map<String, Object> certifyCodeMap = UnivCert.certifyCode(API_KEY,
                certifyCodeDto.getEmail(), certifyCodeDto.getUniversityName(),certifyCodeDto.getCode());

        return certifyCodeMap;
    }
}
