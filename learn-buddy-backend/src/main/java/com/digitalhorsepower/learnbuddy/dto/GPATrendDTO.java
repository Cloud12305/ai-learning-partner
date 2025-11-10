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
public class GPATrendDTO {
    private List<String> labels;
    private List<Double> data;
}