package com.backend.wear.repository;

import com.backend.wear.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    University findByUniversityName(String universityName);
}