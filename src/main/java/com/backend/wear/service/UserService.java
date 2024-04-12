package com.backend.wear.service;

import com.backend.wear.dto.*;
import com.backend.wear.entity.*;
import com.backend.wear.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final UniversityRepository universityRepository;

    private final DonationApplyRepository donationApplyRepository;

    private final WishRepository wishRepository;

    private final ProductRepository productRepository;

    private final BlockedUserRepository blockedUserRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(UserRepository userRepository, StyleRepository styleRepository,
                       UniversityRepository universityRepository, DonationApplyRepository donationApplyRepository,
                       WishRepository wishRepository, ProductRepository productRepository, BlockedUserRepository blockedUserRepository,
                       ObjectMapper objectMapper){
        this.userRepository=userRepository;
        this.styleRepository=styleRepository;
        this.universityRepository=universityRepository;
        this.donationApplyRepository=donationApplyRepository;
        this.wishRepository=wishRepository;
        this.productRepository=productRepository;
        this.blockedUserRepository=blockedUserRepository;
        this.objectMapper = objectMapper;
    }

    // List<String>를 JSON 문자열로 변환하는 메서드
    private String convertImageListToJson(List<String> imageList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageList);
    }

    // JSON 문자열을 List<String>으로 변환하는 메서드
    private List<String> convertJson6ToImageList(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<String>>() {});
    }

    // 마이페이지 사용자 정보
    @Transactional
    public UserResponseInnerDto.MyPageDto getMyPageUserService(Long userId) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        System.out.println("유저"+user.getUserName());

        return mapToMyPageDto(user, userId);
    }

    // 마이페이지 응답 dto 매핑
    private UserResponseInnerDto.MyPageDto mapToMyPageDto(User user, Long userId) throws Exception{
        String universityName = getUniversityNameByUser(userId); //대학 이름
        List<String> style = getUserStyleList(userId); //스타일 리스트
        String level=getCurrentLevel(userId); //현재 레벨
        String nextLevel=getNextLevel(level); //다음 레벨
        Integer point=getPoint(userId);
        Integer remainLevelPoint= getRemainLevelPoint(point);

        System.out.println("이름: "+user.getUserName());

        String[] array = new String[0];
        try {
            array = objectMapper.readValue(user.getProfileImage(), String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return UserResponseInnerDto.MyPageDto.builder()
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .universityName(universityName)
                .style(style)
                .profileImage(array)
                .level(level)
                .nextLevel(nextLevel)
                .point(point)
                .remainLevelPoint(remainLevelPoint)
                .build();
    }

    // 마이페이지 프로필
    @Transactional
    public UserResponseInnerDto.ProfileDto getUserProfileService(Long userId) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToProfileDto(user, userId);
    }

    // 마이페이지 프로필 응답 dto 매핑
    private UserResponseInnerDto.ProfileDto mapToProfileDto(User user, Long userId) throws Exception {
        List<String> styleList = getUserStyleList(userId); //스타일 리스트

        String[] array = new String[0];
        try {
            array = objectMapper.readValue(user.getProfileImage(), String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return UserResponseInnerDto.ProfileDto.builder()
                .nickName(user.getNickName())
                .profileImage(array)
                .style(styleList)
                .build();
    }

    // 마이페이지 프로필 수정
    @Transactional
    public void updateUserProfile(Long userId, UserRequestDto.ProfileDto profileDto) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

   //     user.setUserName(profileDto.getUserName());
        user.setNickName(profileDto.getNickName());

        String profileImage;

        try {
            // String 배열을 JSON 문자열로 변환
            profileImage = objectMapper.writeValueAsString(profileDto.getProfileImage());
        } catch (JsonProcessingException e) {
            // JsonProcessingException 처리
            throw new RuntimeException("Failed to convert String[] to JSON string", e);
        }

        System.out.println(profileImage); // JSON 형식의 문자열 출력

        user.setProfileImage(profileImage);

        setProfileStyle(userId, user, profileDto.getStyle());
    }

    // 유저 정보 조회
    @Transactional
    public UserResponseInnerDto.InfoDto getUserInfoService(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToInfoDto(user,userId);
    }

    // 사용자 정보 info 응답 dto
    private UserResponseInnerDto.InfoDto mapToInfoDto(User user, Long userId){
        String universityName=getUniversityNameByUser(userId);

        return UserResponseInnerDto.InfoDto.builder()
                .userName(user.getUserName())
                .universityName(universityName)
                .universityEmail(user.getUniversityEmail())
                .build();
    }

    // info 저장
    @Transactional
    public void updateUserInfoService(Long userId, UserRequestDto.InfoDto infoDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserName(infoDto.getUserName());
        //      user.setUniversityEmail(infoDto.getUniversityEmail());
        //      updateUniversityName(userId, infoDto.getUniversityName());
    }

    // 비밀번호 변경
    @Transactional
    public void updatePassword(Long userId, UserRequestDto.PasswordDto passwordDto){
        if (!passwordDto.getNewPassword().equals(passwordDto.getCheckPassword()))
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");

        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserPassword(passwordDto.getCheckPassword());
    }

    // 찜한 상품 불러오기
    @Transactional
    public List<ProductResponseInnerDto.ScreenDto> getWishList(Long userId) throws Exception{
        // 찜 리스트
        List<Wish> wishList = wishRepository.findByUserId(userId);

        if (wishList.isEmpty())
            throw new IllegalArgumentException("현재 찜한 상품이 없습니다.");

        // 찜한 상품 리스트 dto로 변환
        return wishList.stream()
                .map(wish -> {
                    Product product = wish.getProduct();

                    try {
                        String[] array = objectMapper.readValue(product.getProductImage(), String[].class);

                        // 사용자의 상품 찜 여부 확인
                        boolean isSelected = wishRepository.findByUserIdAndProductId(userId, product.getId()).isPresent();

                        // DTO 생성 및 반환
                        return ProductResponseInnerDto.ScreenDto.builder()
                                .id(product.getId())
                                .price(product.getPrice())
                                .productName(product.getProductName())
                                .productStatus(product.getProductStatus())
                                .postStatus(product.getPostStatus())
                                .productImage(array)
                                .isSelected(isSelected)
                                .time(ConvertTime.convertLocaldatetimeToTime(product.getUpdatedAt()))
                                .build();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    // 판매 중, 완료 상품 불러오기
    @Transactional
    public List<ProductResponseInnerDto.MyPageScreenDto> getMyProductsList(Long userId, String postStatus) throws Exception{
        List<ProductResponseInnerDto.MyPageScreenDto> productResponseDtoList =
                mapToMyPageScreenDto(userId,postStatus);

        if(productResponseDtoList.isEmpty()){
            if(postStatus.equals("onSale"))
                throw new IllegalArgumentException("현재 판매중인 상품이 없습니다.");
            else
                throw new IllegalArgumentException("현재 판매 완료한 상품이 없습니다.");
        }

        else
            return productResponseDtoList;
    }

    // 판매 내역 상품 dto 매핑
    private List<ProductResponseInnerDto.MyPageScreenDto> mapToMyPageScreenDto(Long userId, String postStatus) throws Exception {
        // 사용자 판매 상품 조회
        List<Product> productList = productRepository.findByUserId(userId);

        // dto 리스트
        return productList.stream()
                .filter(product -> product.getPostStatus().equals(postStatus))
                .map(product -> {
                    try {
                        String[] array = objectMapper.readValue(product.getProductImage(), String[].class);

                        return ProductResponseInnerDto.MyPageScreenDto.builder()
                                .id(product.getId())
                                .price(product.getPrice())
                                .productName(product.getProductName())
                                .productStatus(product.getProductStatus())
                                .postStatus(product.getPostStatus())
                                .productImage(array)
                                .time(ConvertTime.convertLocaldatetimeToTime(product.getUpdatedAt()))
                                .build();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    // 판매 중 상품 완료로 변경
    @Transactional
    public void changePostStatus(Long userId, ProductRequestDto requestDto){
        // 아이디로 상품 조회
        Product product=productRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("아이디와 일치하는 상품 없음.")  );

        // 상품 상태 변경
        //product.setPostStatus(requestDto.getPostStatus());
         product.setPostStatus("soldOut");
    }

    // 숨김 처리 상품 보기
    @Transactional
    public List<ProductResponseInnerDto.PrivateDto> myProductsPrivateList(Long userId) throws Exception{
        List<ProductResponseInnerDto.PrivateDto> privateProductList = mapToPrivateDto(userId);

        if(privateProductList.isEmpty())
            throw new IllegalArgumentException("현재 숨김 처리한 상품이 없습니다.");
        else
            return privateProductList;
    }

    // 숨김 상품 dto 매핑
    private List<ProductResponseInnerDto.PrivateDto> mapToPrivateDto(Long userId) throws Exception {
        List<Product> productList = productRepository.findByUserIdAndIsPrivateTrue(userId);

        return productList.stream()
                .map(product -> {
                    try {
                        String[] array = objectMapper.readValue(product.getProductImage(), String[].class);
                        return ProductResponseInnerDto.PrivateDto.builder()
                                .id(product.getId())
                                .price(product.getPrice())
                                .productName(product.getProductName())
                                .productStatus(product.getProductStatus())
                                .postStatus(product.getPostStatus())
                                .productImage(array)
                                .isPrivate(product.isPrivate())
                                .time(ConvertTime.convertLocaldatetimeToTime(product.getUpdatedAt()))
                                .build();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    // 사용자 기부 내역
    @Transactional
    public List<DonationApplyResponseDto> myDonationApplyList(Long userId){
        List<DonationApplyResponseDto> donationApplyList
                =mapToDonationApplyResponseDto(userId, false);

        if(donationApplyList.isEmpty())
            throw new IllegalArgumentException("현재 기부한 상품이 없습니다. 기부를 통해 환경을 도와주세요.");
        else
            return donationApplyList;
    }

    // 사용자 기부 완료 내역
    @Transactional
    public List<DonationApplyResponseDto> myDonationApplyCompleteList(Long userId){
        List<DonationApplyResponseDto> responseDtoList
                =  mapToDonationApplyResponseDto(userId, true);
        if(responseDtoList.isEmpty())
            throw new IllegalArgumentException("현재 기부 진행이 완료된 내역이 없습니다.");

        else
            return responseDtoList;
    }

    // 내 기부 내역 응답 dto
    private List<DonationApplyResponseDto> mapToDonationApplyResponseDto(Long userId, boolean donationComplete){
        // 기부 내역 조회
        List<DonationApply> list = donationComplete ?
                donationApplyRepository.findByUserIdAndDonationComplete(userId) :
                donationApplyRepository.findByUserId(userId);

        // dto 리스트로 변환
        return list.stream()
                .map(donationApply -> DonationApplyResponseDto.builder()
                        .id(donationApply.getId())
                        .date(donationApply.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                        .clothesCount(donationApply.getClothesCount())
                        .fashionCount(donationApply.getFashionCount())
                        .isDonationComplete(donationApply.isDonationComplete())
                        .build())
                .collect(Collectors.toList());
    }

    // 차단한 사용자 리스트 불러오기
    @Transactional
    public List<BlockedUserResponseDto> getBlockedUsersList(Long userId){
        // 차단한 사용자 아이디 불러오기
        List<Long> blockedUserIdList = blockedUserRepository.findByUserId(userId);

        if(blockedUserIdList.isEmpty())
            throw new IllegalArgumentException("차단한 사용자가 없습니다.");

        List<User> blockedUserList = userRepository.findByUserId(blockedUserIdList);

        List<BlockedUserResponseDto> dtoList = blockedUserList.stream()
                .flatMap(user -> {
                    try {
                        String[] array = objectMapper.readValue(user.getProfileImage(), String[].class);
                        // 예외가 발생하지 않으면 해당 DTO를 반환
                        return Stream.of(BlockedUserResponseDto.builder()
                                .blockedUserId(user.getId())
                                .blockedUserNickName(user.getNickName())
                                .blockedUserProfileImage(array)
                                .blockedUserLevel(user.getLevel().getLabel())
                                .build());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        return dtoList;
    }

    // 차단 해제
    @Transactional
    public void deleteBlockedUser(Long userId, Long blockedUserId){
        BlockedUser blockedUser = blockedUserRepository.findByUserIdAndBlockedUserId(userId,blockedUserId)
                .orElseThrow( () -> new IllegalArgumentException("차단 해제에서 차단한 사용자 불러오기 실패"));
        // 차단해제
        blockedUserRepository.delete(blockedUser);
    }

    // 스타일 태그 이름으로 Style 저장
    @Transactional
    public void setProfileStyle (Long userId, User user, List<String> styleNameList) {

        // styleNameList에 포함되어 있지 않은 스타일 삭제
        styleRepository.deleteByUserIdAndStyleNameNotIn(userId, styleNameList);

        // styleNameList에 포함되어 있지만 style 테이블에 없는 스타일 삽입
        for (String styleName : styleNameList) {
            Style existingStyle = styleRepository.findStyleByStyleNameAndUserId(styleName, userId);

            if (existingStyle == null) {
                Style newStyle = Style.builder()
                        .styleName(styleName)
                        .user(user)
                        .build();
                styleRepository.save(newStyle);
            }
        }
    }

    //대학교 이름 조회
    private String getUniversityNameByUser(Long userId){
        return userRepository.findById(userId).get().
                getUniversity().getUniversityName();
    }

    //대학교 이름 변경 (일단 인증 절차X)
    private void updateUniversityName(Long userId, String universityName){

        //    University university = userRepository.findById(userId).get().getUniversity();

        //    university.setUniversityName(universityName);
    }

    //스타일 태그 이름만 조회
    private List<String> getUserStyleList(Long userId){
        List<Style> styleList = styleRepository.findByUserId(userId);

        // Style 태그에서 styleName만 추출하여 리스트로 반환
        return styleList.stream()
                .map(Style::getStyleName) // Style 객체를 styleName으로 매핑
                .collect(Collectors.toList()); // 리스트로 변환하여 반환
    }

    //현재 레벨 조회
    private String getCurrentLevel(Long userId){
        return userRepository.findById(userId).get()
                .getLevel().getLabel();
    }

    // 다음 레벨
    private String getNextLevel(String currentLevel){
        if (currentLevel.equals("씨앗"))
            return "새싹";
        else if(currentLevel.equals("새싹"))
            return "목화";
        else if(currentLevel.equals("목화"))
            return "꽃";
        else if(currentLevel.equals("꽃"))
            return "옷";
        else
            return "레벨 달성 완료";
    }

    //현재 포인트
    private Integer getPoint(Long userId){
        return userRepository.findById(userId).get()
                .getPoint();
    }

    //남은 레벨 포인트
    private Integer getRemainLevelPoint(Integer currentPoint){
        if(currentPoint>=0 && currentPoint<100)
            return 100-currentPoint;
        else if(currentPoint>= 100 && currentPoint<200)
            return 200-currentPoint;
        else if(currentPoint>= 200 && currentPoint<300)
            return 300-currentPoint;
        else if(currentPoint>=300 && currentPoint<400)
            return 400-currentPoint;
        else
            return 500-currentPoint;
    }

    //    @Transactional
//    public List<ProductResponseDto> myHistoryService(Long userId){
//        List<ProductResponseDto> list = mapToMyHistory(userId);
//
//        if(list.isEmpty())
//            throw new IllegalArgumentException("현재 구매한 상품이 없습니다.");
//
//        else
//            return list;
//    }


//
//        return myProductList;
//    }
//
//    //구매 내역
//    private List<ProductResponseDto> mapToMyHistory(Long userId){
//        List<Product> list =  productRepository.findSoldOutProductsByUserId(userId);
//        List<ProductResponseDto> myHistoryList=new ArrayList<>();
//
//        for(Product p: list){
//            // JSON 배열 파싱
//            String array;
//            try {
//                array = objectMapper.readValue(p.getProductImage(), String.class);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//            ProductResponseDto dto=ProductResponseDto.builder()
//                    .id(p.getId())
//                    .price(p.getPrice())
//                    .productName(p.getProductName())
//                    .productStatus(p.getProductStatus())
//                    .postStatus(p.getPostStatus())
//                    .productImage(array)
//                    .build();
//
//            myHistoryList.add(dto);
//        }
//
//        return myHistoryList;
//    }
}