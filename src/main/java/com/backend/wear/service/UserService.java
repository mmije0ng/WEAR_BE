package com.backend.wear.service;

import com.backend.wear.dto.*;
import com.backend.wear.entity.*;
import com.backend.wear.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final UniversityRepository universityRepository;

    private final DonationApplyRepository donationApplyRepository;

    private final WishRepository wishRepository;

    private final ProductRepository productRepository;



    @Autowired
    public UserService(UserRepository userRepository,StyleRepository styleRepository,
                       UniversityRepository universityRepository, DonationApplyRepository donationApplyRepository,
                       WishRepository wishRepository, ProductRepository productRepository){
        this.userRepository=userRepository;
        this.styleRepository=styleRepository;
        this.universityRepository=universityRepository;
        this.donationApplyRepository=donationApplyRepository;
        this.wishRepository=wishRepository;
        this.productRepository=productRepository;
    }


    // ObjectMapper 생성
    ObjectMapper objectMapper = new ObjectMapper();

    //마이페이지 사용자 정보
    @Transactional
    public UserResponseDto getMyPageUserService(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        System.out.println("유저"+user.getUserName());

        return mapToUserResponseDtoMyPage(user, userId);
    }

    //마이페이지 프로필
    @Transactional
    public UserResponseDto getUserProfileService(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToUserResponseDtoProfile(user, userId);
    }

    //마이페이지 프로필 수정
    @Transactional
    public void updateUserProfile(Long userId, UserRequestDto userRequestDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserName(userRequestDto.getUserName());
        user.setNickName(userRequestDto.getNickName());
        user.setProfileImage(userRequestDto.getProfileImage());

        setProfileStyle(user,userRequestDto.getStyle());
    }

    //유저 정보 조회
    @Transactional
    public UserResponseDto getUserInfo(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToUserResponseDtoInfo(user,userId);
    }

    //정보 저장
    @Transactional
    public void updateUserInfo(Long userId, UserRequestDto dto){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserName(dto.getUserName());
        user.setUniversityEmail(dto.getUniversityEmail());
        updateUniversityName(userId, dto.getUniversityName());
    }

    //비밀번호 변경
    @Transactional
    public void updatePassword(Long userId, UserPasswordDto dto){
        if (!dto.getNewPassword().equals(dto.getCheckPassword()))
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");


        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUserPassword(dto.getCheckPassword());
    //    userRepository.save(user);
    }

    //찜한 상품 불러오기
    @Transactional
    public List<ProductResponseDto> getWishListService(Long userId){
       List<ProductResponseDto> wishList=  mapToProductResponseWishDto(userId);

       if(wishList.isEmpty())
           throw new IllegalArgumentException("현재 찜한 상품이 없습니다.");
       else
           return wishList;
    }

    //판매 중, 완료 상품 불러오기
    @Transactional
    public List<ProductResponseDto> myProductsService(Long userId, String postStatus){
        List<ProductResponseDto> productResponseDtoList = mapToProductResponseDtoPostStatus(userId,postStatus);

        if(productResponseDtoList.isEmpty()){
            if(postStatus.equals("onSale"))
                throw new IllegalArgumentException("현재 판매중인 상품이 없습니다.");
            else
                throw new IllegalArgumentException("현재 판매 완료한 상품이 없습니다.");
        }

        else
            return productResponseDtoList;
    }

    @Transactional
    public List<ProductResponseDto> myHistoryService(Long userId){
        List<ProductResponseDto> list = mapToMyHistory(userId);

        if(list.isEmpty())
            throw new IllegalArgumentException("현재 구매한 상품이 없습니다.");

        else
            return list;
    }

    //판매 중 상품 완료로 변경
    @Transactional
    public void postMyProductStatusService(Long userId, ProductRequestDto dto){
        Product product=productRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("상품 상태를 변경하는데 실패하였습니다.")  );

        product.setPostStatus(dto.getPostStatus());
  //      productRepository.save(product);
    }

    //숨김 처리 상품 보기
    @Transactional
    public List<ProductResponseDto> getMyProductsPrivateService(Long userId){
        List<ProductResponseDto> privateList = mapToProductPostResponseDtoPrivate(userId);

        if(privateList.isEmpty())
            throw new IllegalArgumentException("현재 숨김 처리한 상품이 없습니다.");
        else
            return privateList;
    }

    //내 기부 내역
    @Transactional
    public List<DonationApplyResponseDto> getMyDonationApplyService(Long userId){
        List<DonationApplyResponseDto> responseDtoList =mapToDonationApplyResponseDto(userId);

        if(responseDtoList.isEmpty())
            throw new IllegalArgumentException("현재 기부한 상품이 없습니다. 기부를 통해 환경을 도와주세요.");
        else
            return responseDtoList;
    }

    @Transactional
    public List<DonationApplyResponseDto> getMyDonationApplyCompleteService(Long userId){
        List<DonationApplyResponseDto> responseDtoList
                = mapToDonationApplyResponseDtoComplete(userId);
        if(responseDtoList.isEmpty()){
            throw new IllegalArgumentException("현재 기부 진행이 완료된 내역이 없습니다.");
        }

        else
            return responseDtoList;
    }

    //마이페이지 응답 dto
    private UserResponseDto mapToUserResponseDtoMyPage(User user, Long userId) {
        String universityName = getUniversityNameByUser(userId); //대학 이름
        List<String> style = getUserStyleList(userId); //스타일 리스트

        String level=getCurrentLevel(userId); //현재 레벨
        String nextLevel=getNextLevel(level); //다음 레벨
        Integer point=getPoint(userId);
        Integer remainLevelPoint= getRemainLevelPoint(point);

        System.out.println("이름: "+user.getUserName());

        return UserResponseDto.builder()
                .userName(user.getNickName())
                .nickName(user.getNickName())
                .universityName(universityName)
                .profileImage(user.getProfileImage())
                .level(level)
                .nextLevel(nextLevel)
                .point(point)
                .remainLevelPoint(remainLevelPoint)
                .style(style)
                .build();
    }

    //사용자 프로필 응답 dto
    private UserResponseDto mapToUserResponseDtoProfile(User user, Long userId) {
        List<String> styleList = getUserStyleList(userId); //스타일 리스트

        return UserResponseDto.builder()
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .profileImage(user.getProfileImage())
                .style(styleList)
                .build();
    }

    //사용자 정보 info 응답 dto
    private UserResponseDto mapToUserResponseDtoInfo(User user, Long userId){
        String universityName=getUniversityNameByUser(userId);

        return UserResponseDto.builder()
                .userName(user.getUserName())
                .universityName(universityName)
                .universityEmail(user.getUniversityEmail())
                .build();
    }

    //찜한 상품
    private List<ProductResponseDto> mapToProductResponseWishDto(Long userId){



        //Wish 클래스 반환
        List<Wish> wishList = wishRepository.findByUserId(userId);
        List<ProductResponseDto> mywishList = new ArrayList<>();

        for(Wish w: wishList){
            if(w.isSelected()){

                Product p=w.getProduct();
                // JSON 배열 파싱
                String[] array = new String[0];
                try {
                    array = objectMapper.readValue(p.getProductImage(), String[].class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                ProductResponseDto dto=ProductResponseDto.builder()
                        .id(p.getId())
                        .price(p.getPrice())
                        .productName(p.getProductName())
                        .productStatus(p.getProductStatus())
                        .postStatus(p.getPostStatus())
                        .productImage(array)
                        .isSelected(p.getWish().isSelected())
                        .build();
                mywishList.add(dto);
            }
        }

        return mywishList;
    }

    //판매 내역
    private List<ProductResponseDto> mapToProductResponseDtoPostStatus(Long userId, String postStatus){
        List<Product> productList= productRepository.findByUserId(userId);
        List<ProductResponseDto> myProductList = new ArrayList<>();

        for(Product p: productList){
            //상품 판매 상태가 요청과 같은 상품 리스트만 반환

            if(!p.getPostStatus().equals(postStatus))
                continue;
            // JSON 배열 파싱
            String[] array = new String[0];
            try {
                array = objectMapper.readValue(p.getProductImage(), String[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            ProductResponseDto dto=ProductResponseDto.builder()
                    .id(p.getId())
                    .price(p.getPrice())
                    .productName(p.getProductName())
                    .productStatus(p.getProductStatus())
                    .postStatus(p.getPostStatus())
                    .productImage(array)
                    .build();

            myProductList.add(dto);
        }

        return myProductList;
    }

    //구매 내역
    private List<ProductResponseDto> mapToMyHistory(Long userId){
        List<Product> list =  productRepository.findSoldOutProductsByUserId(userId);
        List<ProductResponseDto> myHistoryList=new ArrayList<>();



        for(Product p: list){
            // JSON 배열 파싱
            String[] array = new String[0];
            try {
                array = objectMapper.readValue(p.getProductImage(), String[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            ProductResponseDto dto=ProductResponseDto.builder()
                    .id(p.getId())
                    .price(p.getPrice())
                    .productName(p.getProductName())
                    .productStatus(p.getProductStatus())
                    .postStatus(p.getPostStatus())
                    .productImage(array)
                    .build();

            myHistoryList.add(dto);
        }

        return myHistoryList;
    }

    //숨김내역
    private List<ProductResponseDto> mapToProductPostResponseDtoPrivate(Long userId){
        List<Product> productList = productRepository.findByUser_IdAndIsPrivateTrue(userId);
        List<ProductResponseDto> privateProductList= new ArrayList<>();

        for(Product p: productList){
            //상품 판매 상태가 요청과 같은 상품 리스트만 반환
            // JSON 배열 파싱
            String[] array = new String[0];
            try {
                array = objectMapper.readValue(p.getProductImage(), String[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            ProductResponseDto dto=ProductResponseDto.builder()
                    .id(p.getId())
                    .price(p.getPrice())
                    .productName(p.getProductName())
                    .productStatus(p.getProductStatus())
                    .postStatus(p.getPostStatus())
                    .productImage(array)
                    .isPrivate(p.isPrivate())
                    .build();

            privateProductList.add(dto);
        }

        return privateProductList;
    }

    //내 기부 내역 응답 dto
    private List<DonationApplyResponseDto> mapToDonationApplyResponseDto(Long userId){
        List<DonationApply> donationApplyList=donationApplyRepository.findByUserId(userId);
        List<DonationApplyResponseDto> responseDtoList = new ArrayList<>();

        for(int i=0;i<donationApplyList.size();i++){
            DonationApply donationApply = donationApplyList.get(i);
            String date= donationApply.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

            DonationApplyResponseDto dto= DonationApplyResponseDto.builder()
                    .id(donationApply.getId())
                    .date(date)
                    .clothesCount(donationApply.getClothesCount())
                    .fashionCount(donationApply.getFashionCount())
                    .isDonationComplete(donationApply.isDonationComplete())
                    .build();

            responseDtoList.add(dto);
        }

        return responseDtoList;
    }


    //기부 내역 중 기부 완료만 보기
    private List<DonationApplyResponseDto> mapToDonationApplyResponseDtoComplete(Long userId){
        List<DonationApply> donationApplyList=donationApplyRepository.findByUserId(userId);
        List<DonationApplyResponseDto> responseDtoList = new ArrayList<>();

        for(int i=0;i<donationApplyList.size();i++){
            DonationApply donationApply = donationApplyList.get(i);
            if(!donationApply.isDonationComplete())
                continue;
            String date= donationApply.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

            DonationApplyResponseDto dto= DonationApplyResponseDto.builder()
                    .id(donationApply.getId())
                    .date(date)
                    .clothesCount(donationApply.getClothesCount())
                    .fashionCount(donationApply.getFashionCount())
                    .isDonationComplete(donationApply.isDonationComplete())
                    .build();

            responseDtoList.add(dto);
        }

        return responseDtoList;
    }

    //대학교 이름 조회
    private String getUniversityNameByUser(Long id){
        return userRepository.findById(id).get().
                getUniversity().getUniversityName();
    }

    //대학교 이름 변경 (일단 인증 절차X)
    private void updateUniversityName(Long userId, String universityName){
        University university = userRepository.findById(userId).get().getUniversity();
        university.setUniversityName(universityName);
    }

    //스타일 태그 이름으로 Style 저장
    @Transactional
    public void setProfileStyle (User user, List<String> style){
        List<Style> newStyles = new ArrayList<>();

        styleRepository.deleteAllByUserId(user.getId());

        for(String s: style){
            Style styleTag =new Style();
            styleTag.setUser(user);
            styleTag.setStyleName(s);

            newStyles.add(styleTag);
        }

        user.setStyle(newStyles);
    }

    //스타일 태그 이름만 조회
    private List<String> getUserStyleList(Long userId){
        List<Style> allStyle = styleRepository.findByUserId(userId);

        //Style 태그에서 styleName만 추출
        List<String> style = new ArrayList<>();

        for(Style s: allStyle){
            style.add(s.getStyleName());
        }

        return style;
    }

    //현재 레벨 조회
    private String getCurrentLevel(Long userId){
        return userRepository.findById(userId).get()
                .getLevel().getLabel();
    }

  //  다음 레벨
    private String getNextLevel(String currentLevel){
        if(currentLevel.equals("씨앗"))
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

}