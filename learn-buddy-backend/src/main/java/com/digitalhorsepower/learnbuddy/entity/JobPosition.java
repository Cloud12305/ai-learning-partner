package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_positions")
@Data
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "location")
    private String location;

    @Column(name = "required_skills", columnDefinition = "JSON")
    private String requiredSkills;

    @Column(name = "job_description", columnDefinition = "TEXT")
    private String jobDescription;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "experience_level")
    private String experienceLevel;

    @Column(name = "education_requirements")
    private String educationRequirements;

    @Column(name = "match_score")
    private Double matchScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}