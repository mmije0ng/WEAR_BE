package com.backend.wear.service;

import com.backend.wear.dto.*;
import com.backend.wear.dto.blockeduser.BlockedUserResponseDto;
import com.backend.wear.dto.donation.DonationApplyResponseDto;
import com.backend.wear.dto.product.ProductRequestDto;
import com.backend.wear.dto.product.ProductResponseDto;
import com.backend.wear.dto.user.UserRequestDto;
import com.backend.wear.dto.user.UserResponseDto;
import com.backend.wear.entity.*;
import com.backend.wear.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserStyleRepository userStyleRepository;

    private final StyleRepository styleRepository;
    private final UniversityRepository universityRepository;

    private final DonationApplyRepository donationApplyRepository;

    private final WishRepository wishRepository;

    private final ProductRepository productRepository;

    private final BlockedUserRepository blockedUserRepository;

    private final ObjectMapper objectMapper;

    private static final int pageSize=12;

    // JSON 문자열을 String[]으로 변환하는 메서드
    private  String[] convertImageJsonToArray(String productImageJson) {
        try {
            return objectMapper.readValue(productImageJson, String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    @Autowired
    public UserService(UserRepository userRepository,
                       UserStyleRepository userStyleRepository,
                       StyleRepository styleRepository,
                       UniversityRepository universityRepository, DonationApplyRepository donationApplyRepository,
                       WishRepository wishRepository, ProductRepository productRepository, BlockedUserRepository blockedUserRepository,
                       ObjectMapper objectMapper){
        this.userRepository=userRepository;
        this.userStyleRepository=userStyleRepository;
        this.styleRepository=styleRepository;
        this.universityRepository=universityRepository;
        this.donationApplyRepository=donationApplyRepository;
        this.wishRepository=wishRepository;
        this.productRepository=productRepository;
        this.blockedUserRepository=blockedUserRepository;
        this.objectMapper = objectMapper;
    }

    // 마이페이지 사용자 정보
    @Transactional
    public UserResponseDto.MyPageDto getMyPageUserService(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        System.out.println("유저"+user.getMyUserName());

        return mapToMyPageDto(user, userId);
    }

    // 마이페이지 응답 dto 매핑
    private UserResponseDto.MyPageDto mapToMyPageDto(User user, Long userId){
        String universityName = getUniversityNameByUser(userId); //대학 이름
        List<String> style = getUserStyleList(userId); //스타일 리스트
        String level=getCurrentLevel(userId); //현재 레벨
        String nextLevel=getNextLevel(level); //다음 레벨
        Integer point=getPoint(userId);
        Integer remainLevelPoint= getRemainLevelPoint(point);

        System.out.println("이름: "+user.getMyUserName());

        // JSON 배열 파싱
        String[] profileImageArray = convertImageJsonToArray(user.getProfileImage());

        return UserResponseDto.MyPageDto.builder()
                .userName(user.getMyUserName())
                .nickName(user.getNickName())
                .universityName(universityName)
                .style(style)
                .profileImage(profileImageArray)
                .level(level)
                .nextLevel(nextLevel)
                .point(point)
                .remainLevelPoint(remainLevelPoint)
                .build();
    }

    // 마이페이지 프로필
    @Transactional
    public UserResponseDto.ProfileDto getUserProfileService(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToProfileDto(user, userId);
    }

    // 마이페이지 프로필 응답 dto 매핑
    private UserResponseDto.ProfileDto mapToProfileDto(User user, Long userId) {
        List<String> styleList = getUserStyleList(userId); //스타일 리스트

        // JSON 배열 파싱
        String[] profileImageArray = convertImageJsonToArray(user.getProfileImage());

        return UserResponseDto.ProfileDto.builder()
                .nickName(user.getNickName())
                .profileImage(profileImageArray)
                .style(styleList)
                .build();
    }

    // 마이페이지 프로필 수정
    @Transactional
    public void updateUserProfile(Long userId, UserRequestDto.ProfileDto profileDto)  {
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
    public UserResponseDto.InfoDto getUserInfoService(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToInfoDto(user,userId);
    }

    // 사용자 정보 info 응답 dto
    private UserResponseDto.InfoDto mapToInfoDto(User user, Long userId){
        String universityName=getUniversityNameByUser(userId);

        return UserResponseDto.InfoDto.builder()
                .userName(user.getMyUserName())
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
    public Page<ProductResponseDto.ScreenDto> getMyWishPage(Long userId, Integer pageNumber) {

        // 찜 리스트
        Page <Wish> myWishPage = wishRepository.findByUserId(userId, pageRequest(pageNumber));

        if (myWishPage.isEmpty())
            throw new IllegalArgumentException("현재 찜한 상품이 없습니다.");

        // 찜한 상품 반환
        return myWishPage.map(wish ->  mapToScreenDto (userId, wish));
    }

    // 찜한 상품 상품 dto 매핑
    private ProductResponseDto.ScreenDto mapToScreenDto(Long userId, Wish wish) {

        Product product = productRepository.findById(wish.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("찜한 상품 찾기 실패"));

        // JSON 배열 파싱
        String[] productImageArray = convertImageJsonToArray(product.getProductImage());

        // 사용자의 상품 찜 여부 확인
        boolean isSelected
                = wishRepository.findByUserIdAndProductId(userId, product.getId()).isPresent();

        // DTO 생성 및 반환
        return ProductResponseDto.ScreenDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .productStatus(product.getProductStatus())
                .postStatus(product.getPostStatus())
                .productImage(productImageArray)
                .isSelected(isSelected)
                .time(ConvertTime.convertLocalDatetimeToTime(product.getCreatedAt()))
                .build();
    }

    // 판매 중, 완료 상품 불러오기
    @Transactional
    public Page<ProductResponseDto.MyPageScreenDto> getMyProductsPage(Long userId, String postStatus, Integer pageNumber) {

        // 사용자 아이디로 판매 상품 페이지네이션
        Page<Product> myProductsPage  = productRepository.findByUserIdAndPostStatus(userId, postStatus, pageRequest(pageNumber));

        if(myProductsPage.isEmpty()){
            if(postStatus.equals("onSale"))
                throw new IllegalArgumentException("현재 판매중인 상품이 없습니다.");
            else
                throw new IllegalArgumentException("현재 판매 완료한 상품이 없습니다.");
        }

        return myProductsPage.map(product -> mapToMyPageScreenDto(postStatus, product));
    }

    // 판매 내역 상품 dto 매핑
    private ProductResponseDto.MyPageScreenDto mapToMyPageScreenDto(String postStatus, Product product) {

        // JSON 배열 파싱
        String[] productImageArray = convertImageJsonToArray(product.getProductImage());

        return ProductResponseDto.MyPageScreenDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .productStatus(product.getProductStatus())
                .postStatus(postStatus)
                .productImage(productImageArray)
                .time(ConvertTime.convertLocalDatetimeToTime(product.getCreatedAt()))
                .build();
    }

    // 상품 상태 변경
    @Transactional
    public void changePostStatus(Long userId, ProductRequestDto requestDto){
        // 아이디로 상품 조회
        Product product=productRepository.findByIdAndUserId(requestDto.getId(), userId)
                .orElseThrow(() -> new IllegalArgumentException("아이디와 일치하는 상품 없음.")  );

        // 상품 상태 변경
        product.setPostStatus(requestDto.getPostStatus());
    }

    // 숨김 처리 상품 보기
    @Transactional
    public Page<ProductResponseDto.PrivateDto> getMyPrivateProductsPage(Long userId, Integer pageNumber) {
        Page <Product> myPrivateProductsPage
                = productRepository.findByUserIdAndIsPrivateTrue(userId, pageRequest(pageNumber));

        if(myPrivateProductsPage .isEmpty())
            throw new IllegalArgumentException("현재 숨김 처리한 상품이 없습니다.");

        return myPrivateProductsPage.map(this::mapToPrivateDto);
    }

    // 숨김 상품 dto 매핑
    private ProductResponseDto.PrivateDto mapToPrivateDto(Product product)  {

        String[] productImageArray = convertImageJsonToArray(product.getProductImage());

        return ProductResponseDto.PrivateDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .productName(product.getProductName())
                .productStatus(product.getProductStatus())
                .postStatus(product.getPostStatus())
                .productImage(productImageArray)
                .isPrivate(product.isPrivate())
                .time(ConvertTime.convertLocalDatetimeToTime(product.getCreatedAt()))
                .build();
    }

    // 사용자 기부 내역
    @Transactional
    public Page <DonationApplyResponseDto> myDonationApplysPage(Long userId, Integer pageNumber){
        Page <DonationApplyResponseDto> myDonationApplysPage
                =mapToDonationApplyResponseDto(userId, pageNumber, false);

        if(myDonationApplysPage.isEmpty())
            throw new IllegalArgumentException("현재 기부한 상품이 없습니다.");
        else
            return myDonationApplysPage;
    }

    // 사용자 기부 완료 내역
    @Transactional
    public Page <DonationApplyResponseDto> myDonationApplysCompletePage(Long userId, Integer pageNumber){
        Page <DonationApplyResponseDto> myDonationApplysCompletePage
                =  mapToDonationApplyResponseDto(userId, pageNumber, true);
        if(myDonationApplysCompletePage.isEmpty())
            throw new IllegalArgumentException("현재 기부 진행이 완료된 내역이 없습니다.");

        else
            return myDonationApplysCompletePage;
    }

    // 기부 완료 여부에 따른 기부 내역 조회 페이지네이션
    private Page<DonationApplyResponseDto> mapToDonationApplyResponseDto(Long userId,Integer pageNumber, boolean donationComplete){
        // 기부 내역 조회
        Page <DonationApply> donationApplyPage = donationComplete ?
                donationApplyRepository.findByUserIdAndDonationComplete(userId, pageRequest(pageNumber)) :
                donationApplyRepository.findByUserId(userId, pageRequest(pageNumber));

        return donationApplyPage.map(this::mapToDonationApplyResponseDto);
    }

    // 내 기부 내역 응답 dto
    private DonationApplyResponseDto mapToDonationApplyResponseDto(DonationApply donationApply){
        return DonationApplyResponseDto.builder()
                .id(donationApply.getId())
                .date(donationApply.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .clothesCount(donationApply.getClothesCount())
                .fashionCount(donationApply.getFashionCount())
                .isDonationComplete(donationApply.isDonationComplete())
                .build();
    }


    // 차단한 사용자 리스트 불러오기
    @Transactional
    public Page <BlockedUserResponseDto> getBlockedUsersPage(Long userId, Integer pageNumber){
        // 차단한 사용자 아이디 불러오기
        List<Long> blockedUsersIdList = blockedUserRepository.findByUserId(userId);

        if(blockedUsersIdList.isEmpty())
            throw new IllegalArgumentException("차단한 사용자가 없습니다.");

        Page <User> blockedUsersPage = userRepository.findByUserId(blockedUsersIdList, pageRequest(pageNumber));

        return blockedUsersPage.map(this::mapToBlockedUserResponseDto);
    }

    private BlockedUserResponseDto mapToBlockedUserResponseDto(User user){
        String[] array = convertImageJsonToArray(user.getProfileImage());
        return BlockedUserResponseDto.builder()
                .blockedUserId(user.getId())
                .blockedUserNickName(user.getNickName())
                .blockedUserProfileImage(array)
                .blockedUserLevel(user.getLevel().getLabel())
                .build();
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
        // userId에 해당하는 모든 UserStyle 조회
        List<UserStyle> allUserStyles = userStyleRepository.findByUserId(userId);

        // Set으로 저장된 styleNameList 생성
        Set<String> styleNamesInList = new HashSet<>(styleNameList);

        // 사용자가 변경할 스타일 태그 리스트 중 포함되지 않는 스타일 태그 삭제
        allUserStyles.stream()
                .filter(userStyle -> !styleNamesInList.contains(userStyle.getStyle().getStyleName()))
                .forEach(userStyleRepository::delete);

        // 새로운 스타일 추가
        styleNameList.stream()
                .filter(styleName -> allUserStyles.stream()
                        .noneMatch(userStyle -> userStyle.getStyle().getStyleName().equals(styleName)))
                .forEach(styleName -> {
                    Style style = styleRepository.findByStyleName(styleName)
                            .orElseThrow(() -> new IllegalArgumentException("없는 스타일 태그 이름입니다."));

                    UserStyle newUserStyle = UserStyle.builder()
                            .user(user)
                            .style(style)
                            .build();
                    userStyleRepository.save(newUserStyle);
                });
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

    // 상품 12개씩 최신순으로 정렬
    private Pageable pageRequest(Integer pageNumber){
        return
                PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
    }

    //스타일 태그 이름으로 조회
    private List<String> getUserStyleList(Long userId){

        List<UserStyle> userStyleList = userStyleRepository.findByUserIdOrderByIdStyleId(userId);
        // Style 태그에서 styleName만 추출하여 리스트로 반환
        return userStyleList.stream()
                .map(userStyle -> userStyle.getStyle().getStyleName()) // Style 객체의 name 필드를 추출하여 매핑
                .collect(Collectors.toList()); // 리스트로 변환하여 반환
    }

    //현재 레벨 조회
    private String getCurrentLevel(Long userId){
        return userRepository.findById(userId).get()
                .getLevel().getLabel();
    }

    // 다음 레벨
    private String getNextLevel(String currentLevel){
        return switch (currentLevel) {
            case "씨앗" -> "새싹";
            case "새싹" -> "목화";
            case "목화" -> "꽃";
            case "꽃" -> "옷";
            default -> "레벨 달성 완료";
        };
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