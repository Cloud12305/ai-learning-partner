package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.PracticalProject;
import com.digitalhorsepower.learnbuddy.repository.PracticalProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PracticalProjectService {

    private final PracticalProjectRepository projectRepository;

    public List<PracticalProject> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<PracticalProject> getProjectById(Long id) {
        // 增加当前学习人数
        Optional<PracticalProject> projectOpt = projectRepository.findById(id);
        projectOpt.ifPresent(project -> {
            project.setCurrentLearners(project.getCurrentLearners() + 1);
            projectRepository.save(project);
        });
        return projectOpt;
    }

    public List<PracticalProject> getProjectsByType(PracticalProject.ProjectType type) {
        return projectRepository.findByType(type);
    }

    public List<PracticalProject> getProjectsByDifficulty(PracticalProject.DifficultyLevel difficulty) {
        return projectRepository.findByDifficulty(difficulty);
    }

    public List<PracticalProject> getFilteredProjects(PracticalProject.ProjectType type,
                                                      PracticalProject.DifficultyLevel difficulty,
                                                      Integer maxDays) {
        return projectRepository.findByFilters(type, difficulty, maxDays);
    }

    public List<PracticalProject> getRecommendedProjects() {
        return projectRepository.findByOrderByCurrentLearnersDesc();
    }

    public PracticalProject saveProject(PracticalProject project) {
        return projectRepository.save(project);
    }
}