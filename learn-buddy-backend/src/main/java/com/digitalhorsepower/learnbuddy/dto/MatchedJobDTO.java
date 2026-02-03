package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;
import java.util.List;

@Data
public class MatchedJobDTO {
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private Double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String salaryRange;
    private String jobDescription;
    private List<SkillTagDTO> requiredSkillTags;

    // 新增字段
    private boolean positionMatch;
    private boolean industryMatch;
    private String industry; // 行业信息
}