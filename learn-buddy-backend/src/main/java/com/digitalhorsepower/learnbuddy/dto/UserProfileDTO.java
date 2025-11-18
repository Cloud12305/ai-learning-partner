package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileDTO {
    private List<SkillTagDTO> skillTags;
    private CareerPreferenceDTO careerPreference;
}
