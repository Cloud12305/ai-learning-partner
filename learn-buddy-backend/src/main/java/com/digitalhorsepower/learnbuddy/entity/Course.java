package com.digitalhorsepower.learnbuddy.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Data
public class Course {
    @Id
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(nullable = false, precision = 3, scale = 1)
    private Double credits;

    @Column(nullable = false)
    private String department;

    @Column(name = "course_category")
    private String courseCategory;

    @Column(name = "total_hours")
    private Integer totalHours;

    @Column(name = "course_description", columnDefinition = "TEXT")
    private String courseDescription;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
