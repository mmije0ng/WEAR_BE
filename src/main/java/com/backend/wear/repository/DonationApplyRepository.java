package com.backend.wear.repository;

import com.backend.wear.entity.DonationApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationApplyRepository extends JpaRepository<DonationApply, Long> {

    //사용자 아이디로 기부
    List <DonationApply> findByUserId(Long userId);
}