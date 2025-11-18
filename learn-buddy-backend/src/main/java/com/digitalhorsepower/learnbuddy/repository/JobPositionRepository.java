package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.JobPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {

    @Query("SELECT jp FROM JobPosition jp WHERE " +
            "LOWER(jp.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(jp.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(jp.jobDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<JobPosition> findByKeyword(@Param("keyword") String keyword);

    List<JobPosition> findByJobTitleContainingIgnoreCase(String jobTitle);

    List<JobPosition> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT jp FROM JobPosition jp WHERE jp.experienceLevel = :experienceLevel")
    List<JobPosition> findByExperienceLevel(@Param("experienceLevel") String experienceLevel);

    @Query("SELECT jp FROM JobPosition jp WHERE jp.matchScore >= :minScore ORDER BY jp.matchScore DESC")
    List<JobPosition> findByMatchScoreGreaterThanEqual(@Param("minScore") Double minScore);
}