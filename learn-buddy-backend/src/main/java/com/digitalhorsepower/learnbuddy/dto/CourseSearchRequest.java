// CourseSearchRequest.java
package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;

@Data
public class CourseSearchRequest {
    private String keyword;
    private String category;
    private String difficulty;
    private String university;
}