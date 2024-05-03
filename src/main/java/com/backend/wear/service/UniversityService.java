package com.backend.wear.service;

import com.backend.wear.dto.university.UniversityRequestDto;
import com.univcert.api.UnivCert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UniversityService {
    @Value("${api.key}")
    private String API_KEY;

    public Object certifyUniversity(UniversityRequestDto.UnivCertDto univCertDto) throws IOException{
        UnivCert.status(API_KEY, univCertDto.getEmail());
        UnivCert.list(API_KEY);
        return UnivCert.certify(API_KEY, univCertDto.getEmail(), univCertDto.getUniversityName(), true);


   //     UnivCert.clear(API_KEY);
  //      UnivCert.clear(API_KEY, univCertDto.getEmail());
    }
}
