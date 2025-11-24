// src/main/java/com/digitalhorsepower/learnbuddy/controller/PracticalTrainingController.java
package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.entity.PracticalProject;
import com.digitalhorsepower.learnbuddy.service.PracticalProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/practical-training")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PracticalTrainingController {

    private final PracticalProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<List<PracticalProject>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<PracticalProject> getProjectById(@PathVariable Long id) {
        Optional<PracticalProject> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/projects/recommended")
    public ResponseEntity<List<PracticalProject>> getRecommendedProjects() {
        return ResponseEntity.ok(projectService.getRecommendedProjects());
    }

    @GetMapping("/projects/filter")
    public ResponseEntity<List<PracticalProject>> getFilteredProjects(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer maxDays) {

        // 将中文参数转换为枚举值
        PracticalProject.ProjectType typeEnum = convertTypeToEnum(type);
        PracticalProject.DifficultyLevel difficultyEnum = convertDifficultyToEnum(difficulty);

        return ResponseEntity.ok(projectService.getFilteredProjects(typeEnum, difficultyEnum, maxDays));
    }

    @PostMapping("/projects")
    public ResponseEntity<PracticalProject> createProject(@RequestBody PracticalProject project) {
        return ResponseEntity.ok(projectService.saveProject(project));
    }

    // 辅助方法：将中文类型转换为枚举
    private PracticalProject.ProjectType convertTypeToEnum(String type) {
        if (type == null) return null;
        switch (type) {
            case "前端开发": return PracticalProject.ProjectType.FRONTEND;
            case "后端开发": return PracticalProject.ProjectType.BACKEND;
            case "全栈开发": return PracticalProject.ProjectType.FULLSTACK;
            case "移动开发": return PracticalProject.ProjectType.MOBILE;
            case "数据库": return PracticalProject.ProjectType.DATABASE;
            case "运维开发": return PracticalProject.ProjectType.DEVOPS;
            default: return null;
        }
    }

    // 辅助方法：将中文难度转换为枚举
    private PracticalProject.DifficultyLevel convertDifficultyToEnum(String difficulty) {
        if (difficulty == null) return null;
        switch (difficulty) {
            case "入门": return PracticalProject.DifficultyLevel.BEGINNER;
            case "中级": return PracticalProject.DifficultyLevel.INTERMEDIATE;
            case "高级": return PracticalProject.DifficultyLevel.ADVANCED;
            default: return null;
        }
    }
}