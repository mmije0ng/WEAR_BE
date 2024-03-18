package com.backend.wear.service;

import com.backend.wear.dto.ProductPostResponseDto;
import com.backend.wear.dto.UserPostResponseDto;
import com.backend.wear.dto.UserResponseDto;
import com.backend.wear.entity.Product;
import com.backend.wear.entity.Style;
import com.backend.wear.entity.User;
import com.backend.wear.repository.StyleRepository;
import com.backend.wear.repository.UniversityRepository;
import com.backend.wear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private UniversityRepository universityRepository;
    private StyleRepository styleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UniversityRepository universityRepository,
                       StyleRepository styleRepository){
        this.userRepository=userRepository;
        this.universityRepository=universityRepository;
    }

//    public UserResponseDto getMyPageUserResponseDto(Long userId){
//        User user=userRepository.findById(userId).get();
//
//    }

//    private UserResponseDto mapToUserPostResponseDto(User user, Long userId) {
//        String universityName = getUniversityNameByUser(user); //대학 이름
//        List<Style> styleList = getUserStyleList(userId); //스타일 리스트
//
//        String level=userRepository.findById(userId).
//
//
//        return UserResponseDto.builder()
//                .userName(user.getUserName())
//                .nickName(user.getNickName())
//                .universityName(universityName)
//                .style(styleList)
//                .profileImage(user.getProfileImage())
//                .level(user.getLevel().getLabel())
//                .nextLevel(product.getProductImage())
//                .point(user.getPoint())
//                .build();
//    }

    //대학교 이름 조회
    private String getUniversityNameByUser(Long id){
        return userRepository.findById(id).get().
                getUniversity().getUniversityName();
    }

    //스타일 조회
    private List getUserStyleList(Long userId){
        return styleRepository.findAllByUserId(userId);
    }

    //현재 레벨 조회
    private String getCurrentLevel(Long userId){
        return userRepository.findById(userId).get()
                .getLevel().getLabel();
    }

    //다음 레벨
  //  private String getNextLevel(String currentLevel){

 //   }

    //현재 포인트
    private Integer getPoint(Long userId){
        return userRepository.findById(userId).get()
                .getPoint();
    }

    //남은 레벨 포인트
 //   private Integer remainLevelPoint(Long userId){

//    }
}