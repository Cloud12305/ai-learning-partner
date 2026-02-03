package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUserId(Long userId);

    @Query("SELECT us.skillName FROM UserSkill us WHERE us.userId = :userId")
    List<String> findSkillNamesByUserId(@Param("userId") Long userId);
}