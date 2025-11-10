package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.LearningProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LearningProgressRepository extends JpaRepository<LearningProgress, Long> {
    List<LearningProgress> findByUserId(Long userId);

    @Query("SELECT lp FROM LearningProgress lp WHERE lp.user.id = :userId ORDER BY lp.studyDate DESC")
    List<LearningProgress> findRecentProgressByUserId(Long userId);
}