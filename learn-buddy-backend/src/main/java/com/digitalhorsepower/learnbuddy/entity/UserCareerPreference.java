package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "user_career_preferences")
@Data
public class UserCareerPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expected_position")
    private String expectedPosition;

    @Column(name = "expected_industry")
    private String expectedIndustry;

    @Column(name = "expected_location")
    private String expectedLocation;

    @Column(name = "expected_salary")
    private String expectedSalary;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}