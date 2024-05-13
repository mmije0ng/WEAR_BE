package com.backend.wear.repository;

import com.backend.wear.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    Optional <University> findByUniversityName(String universityName);

    @Query("SELECT u FROM University u JOIN u.userList user WHERE user.id = :userId")
    Optional<University> findByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM University u ORDER BY u.universityPoint DESC, u.id ASC")
    List<University> findTopUniversity();
}