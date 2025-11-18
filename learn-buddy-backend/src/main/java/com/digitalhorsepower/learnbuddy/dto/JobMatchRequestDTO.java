package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class JobMatchRequestDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    private List<String> userSkills;
    private String expectedPosition;
    private String expectedIndustry;
    private String expectedLocation;

    private List<SkillTagDTO> skillTags;
}