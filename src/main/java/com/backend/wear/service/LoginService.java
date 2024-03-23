package com.backend.wear.service;

import com.backend.wear.dto.LoginDto;
import com.backend.wear.dto.LoginResponseDto;
import com.backend.wear.entity.User;
import com.backend.wear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository){
        this.userRepository=userRepository;

    }

    public LoginResponseDto loginByUser(LoginDto loginDto){
        String userId= loginDto.getUserId();
        User user= userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "사용자를 찾지 못하였습니다."));

        String userPassword=loginDto.getUserPassword();
        String password=user.getUserPassword();

        //비밀번호 입력 안 함
        if(userPassword.isEmpty()){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        //비밀번호 일치X
        if(!userPassword.equals(password))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        else {//로그인 성공, 유저 pk 반환
            LoginResponseDto dto=LoginResponseDto.builder()
                    .id(user.getId())
                    .message("로그인 성공")
                    .build();
            return dto;
        }
    }

}
