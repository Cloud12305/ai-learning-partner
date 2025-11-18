package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "user_skills")
@Data
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(name = "proficiency_level")
    private String proficiencyLevel;

    @Column(name = "experience_months")
    private Integer experienceMonths;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
}