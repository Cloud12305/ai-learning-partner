package com.digitalhorsepower.learnbuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String date;
    private Integer members;
}