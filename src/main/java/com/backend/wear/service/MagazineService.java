package com.backend.wear.service;

import com.backend.wear.entity.User;
import com.backend.wear.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MagazineService {

    private final UserRepository userRepository;
    @Autowired
    public MagazineService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Transactional
    public void updatePoint(Long userId, Integer score){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "사용자를 찾지 못하였습니다."));

        Integer point= user.getPoint()+score;
        user.setPoint(point);
    }
}