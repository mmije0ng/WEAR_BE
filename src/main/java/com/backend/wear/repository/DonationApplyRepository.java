package com.backend.wear.repository;

import com.backend.wear.entity.DonationApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationApplyRepository extends JpaRepository<DonationApply, Long> {
}
