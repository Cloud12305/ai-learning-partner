package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.UserCareerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCareerPreferenceRepository extends JpaRepository<UserCareerPreference, Long> {
    Optional<UserCareerPreference> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}