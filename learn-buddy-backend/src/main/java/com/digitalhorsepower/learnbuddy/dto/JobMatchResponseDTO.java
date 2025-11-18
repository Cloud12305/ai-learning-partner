package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class JobMatchResponseDTO {
    private Long userId;
    private List<MatchedJobDTO> matchedJobs;
    private Map<String, Double> skillGapAnalysis;
    private String recommendation;
    private UserProfileDTO userProfile;
}