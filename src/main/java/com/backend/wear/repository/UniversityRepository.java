package com.backend.wear.repository;

import com.backend.wear.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    Optional <University> findByUniversityName(String universityName);
}