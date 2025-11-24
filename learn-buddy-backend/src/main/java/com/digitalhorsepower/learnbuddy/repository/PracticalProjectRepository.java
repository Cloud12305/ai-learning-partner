package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.PracticalProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticalProjectRepository extends JpaRepository<PracticalProject, Long> {

    List<PracticalProject> findByType(PracticalProject.ProjectType type);

    List<PracticalProject> findByDifficulty(PracticalProject.DifficultyLevel difficulty);

    List<PracticalProject> findByTypeAndDifficulty(PracticalProject.ProjectType type,
                                                   PracticalProject.DifficultyLevel difficulty);

    @Query("SELECT p FROM PracticalProject p WHERE " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:difficulty IS NULL OR p.difficulty = :difficulty) AND " +
            "(:maxDays IS NULL OR p.estimatedDays <= :maxDays)")
    List<PracticalProject> findByFilters(@Param("type") PracticalProject.ProjectType type,
                                         @Param("difficulty") PracticalProject.DifficultyLevel difficulty,
                                         @Param("maxDays") Integer maxDays);

    List<PracticalProject> findByOrderByCurrentLearnersDesc();
}