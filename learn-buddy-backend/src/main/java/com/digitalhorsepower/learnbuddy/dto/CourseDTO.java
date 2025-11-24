package com.digitalhorsepower.learnbuddy.dto;
import lombok.Data;

@Data
public class CourseDTO {
    private String title;
    private String university;
    private String instructor;
    private String imageUrl;
    private String courseLink;
    private String platform;
    private String category;
    private String difficulty;
}