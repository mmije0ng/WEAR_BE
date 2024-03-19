package com.backend.wear.service;

import com.backend.wear.dto.UserRequestDto;
import com.backend.wear.dto.UserResponseDto;
import com.backend.wear.entity.Style;
import com.backend.wear.entity.User;
import com.backend.wear.repository.StyleRepository;
import com.backend.wear.repository.UniversityRepository;
import com.backend.wear.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final StyleRepository styleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UniversityRepository universityRepository,
                       StyleRepository styleRepository){
        this.userRepository=userRepository;
        this.universityRepository=universityRepository;
        this.styleRepository=styleRepository;
    }

    //마이페이지 사용자 정보
    @Transactional
    public UserResponseDto getMyPageUserService(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return mapToUserResponseDtoProfile(user, userId);
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
  //      user.setStyle(userRequestDto.getStyle());

        try {
            userRepository.save(user); // 변경된 엔티티를 저장
        } catch (DataAccessException e) {
            throw new IllegalStateException("회원 정보 변경에 실패했습니다.");
        }
    }

    private UserResponseDto mapToUserResponseDtoMyPage(User user, Long userId) {
        String universityName = getUniversityNameByUser(userId); //대학 이름
        List<Style> styleList = getUserStyleList(userId); //스타일 리스트

        String level=getCurrentLevel(userId); //현재 레벨
        String nextLevel=getNextLevel(level); //다음 레벨
        Integer point=getPoint(userId);
        Integer remainLevelPoint= getRemainLevelPoint(point);

        return UserResponseDto.builder()
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .universityName(universityName)
                .style(styleList)
                .profileImage(user.getProfileImage())
                .level(level)
                .nextLevel(nextLevel)
                .point(point)
                .remainLevelPoint(remainLevelPoint)
                .build();
    }

    private UserResponseDto mapToUserResponseDtoProfile(User user, Long userId) {
        List<Style> styleList = getUserStyleList(userId); //스타일 리스트

        return UserResponseDto.builder()
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .style(styleList)
                .profileImage(user.getProfileImage())
                .build();
    }

//    private

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

  //  다음 레벨
    private String getNextLevel(String currentLevel){
        if(currentLevel.equals("씨앗"))
            return "새싹";
        else if(currentLevel.equals("새싹"))
            return "목화";
        else if(currentLevel.equals("목화"))
            return "천";
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
        if(currentPoint>=0 && currentPoint<25)
            return 25-currentPoint;
        else if(currentPoint>= 25 && currentPoint<50)
            return 50-currentPoint;
        else if(currentPoint>= 50 && currentPoint<75)
            return 75-currentPoint;
        else
            return 100-currentPoint;
    }
}