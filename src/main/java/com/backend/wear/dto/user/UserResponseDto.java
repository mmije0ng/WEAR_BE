package com.backend.wear.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserResponseDto {
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class MyPageDto{
        private String userName; //사용자 이름
        private String nickName;
        private String universityName;
        private List<String> style; //스타일 태그 이름 리스트
        private String[] profileImage;
        private String level;
        private String nextLevel;
        private Integer point;
        private Integer remainLevelPoint;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProfileDto{
  //      private String userName; //사용자 이름
        private String nickName;
        private List<String> style; //스타일 태그 이름 리스트
        private String[] profileImage;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class InfoDto{
        private String userName;
        private String universityName;
        private String universityEmail;
    }

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SellerDto{
        private Long id; // pk
        private String nickName;
        private String[] profileImage;
        private String level;
    }

    // 채팅 판매자 정보
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SellerInfo{
        private Long sellerId;
        private String sellerNickName;
        private String[] sellerProfileImage;
        private String sellerLevel;

        // 메시지, 시간
        private Map<String, String> sellerMessageMap;
    }

    // 채팅 구매자 정보
    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CustomerInfo{
        private Long customerId;
        private String customerNickName;
        private String[] customerProfileImage;
        private String customerLevel;

        // 메시지, 시간
        private Map<String, String> customerMessageMap;
    }

}
