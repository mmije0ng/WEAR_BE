package com.backend.wear.config.jwt;

import com.backend.wear.entity.User;
import com.backend.wear.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// JWT의 유효성을 검증한 이후
// JWT에서 추출한 userId와 일치하는 User가 데이터베이스에 존재하는지 여부를 판단
// 존재하면 Spring Security에서 내부적으로 사용되는 Auth 객체(UserPasswordAuthenticationToken)를
// 만들때 필요한 UserDetails 객체로 반환

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository; // 사용자 레포지토리
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        // 엔티티 유저
        User user = userRepository.findById(Long.parseLong(id))
                .orElseThrow( ()-> new UsernameNotFoundException("해당하는 유저가 없습니다.") );

      //  CustomUserInfoDto dto =  mapper.map(user, CustomUserInfoDto.class);

        CustomUserInfoDto dto = CustomUserInfoDto.builder()
                .userId(user.getId())
                .role(user.getRole().getRole())
                .email(user.getUniversityEmail())
                .password(user.getUserPassword())
                .nickName(user.getNickName())
                .build();

        return new CustomUserDetails(dto);
    }
}
