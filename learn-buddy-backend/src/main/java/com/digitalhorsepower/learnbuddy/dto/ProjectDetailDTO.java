// src/main/java/com/digitalhorsepower/learnbuddy/dto/ProjectDetailDTO.java
package com.digitalhorsepower.learnbuddy.dto;

import com.digitalhorsepower.learnbuddy.entity.PracticalProject;
import com.digitalhorsepower.learnbuddy.entity.UserProgress;
import lombok.Data;

@Data
public class ProjectDetailDTO {
    private PracticalProject project;
    private UserProgress userProgress;
    private boolean isStarted;

    public ProjectDetailDTO(PracticalProject project, UserProgress userProgress) {
        this.project = project;
        this.userProgress = userProgress;
        this.isStarted = userProgress != null;
    }
}

