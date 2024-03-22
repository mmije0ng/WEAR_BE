//package com.backend.wear.controller;
//
//import com.backend.wear.dto.UnivCertRequestDto;
//import com.backend.wear.service.UnivCert;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/university")
//public class UnivCertController {
//
//    @Value("${8664efc6-ab71-47b9-8cb0-5aa20dccc59c}")
//    private String API_KEY;
//
//    @PostMapping("/certify")
//    public ResponseEntity<?> certifyUniversity
//            (@RequestBody UnivCertRequestDto dto) throws IOException {
//
//        Map<String, Object> response =
//                UnivCert.certify(API_KEY, dto.getUniversityEmail(), dto.getUniversityName(), dto.isCheck());
//
//        return ResponseEntity.ok(response);
//
//    }
//
//}
