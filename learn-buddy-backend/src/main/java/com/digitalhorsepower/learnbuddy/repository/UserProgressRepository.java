// src/main/java/com/digitalhorsepower/learnbuddy/repository/UserProgressRepository.java
package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    List<UserProgress> findByUserId(Long userId);

    Optional<UserProgress> findByUserIdAndProjectId(Long userId, Long projectId);

    List<UserProgress> findByUserIdAndStatus(Long userId, UserProgress.ProgressStatus status);
}