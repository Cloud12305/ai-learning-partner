package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "course_id", length = 20)
    private String courseId;

    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;

    @Column(name = "course_name_en", length = 100)
    private String courseNameEn;

    @Column(name = "credits", nullable = false, precision = 3, scale = 1)
    private BigDecimal credits;

    @Column(name = "department", nullable = false, length = 50)
    private String department;

    @Column(name = "course_category", length = 30)
    private String courseCategory;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel = 3;

    @Column(name = "total_hours")
    private Integer totalHours;

    @Column(name = "theory_hours")
    private Integer theoryHours;

    @Column(name = "practice_hours")
    private Integer practiceHours;

    @Column(name = "syllabus_text", columnDefinition = "TEXT")
    private String syllabusText;

    @Column(name = "course_description", columnDefinition = "TEXT")
    private String courseDescription;

    @Column(name = "assessment_method", length = 50)
    private String assessmentMethod;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}