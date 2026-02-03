// src/main/java/com/digitalhorsepower/learnbuddy/service/UserProgressService.java
package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.UserProgress;
import com.digitalhorsepower.learnbuddy.repository.UserProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProgressService {

    private final UserProgressRepository progressRepository;

    public List<UserProgress> getUserProgress(Long userId) {
        return progressRepository.findByUserId(userId);
    }

    public Optional<UserProgress> getProjectProgress(Long userId, Long projectId) {
        return progressRepository.findByUserIdAndProjectId(userId, projectId);
    }

    public UserProgress startProject(Long userId, Long projectId, Integer totalModules) {
        Optional<UserProgress> existingProgress = progressRepository.findByUserIdAndProjectId(userId, projectId);

        if (existingProgress.isPresent()) {
            return existingProgress.get();
        }

        UserProgress progress = new UserProgress();
        progress.setUserId(userId);
        progress.setProjectId(projectId);
        progress.setCompletedModules(0);
        progress.setTotalModules(totalModules);
        progress.setProgressPercentage(0.0);
        progress.setStartDate(LocalDateTime.now());
        progress.setExpectedCompletionDate(LocalDateTime.now().plusDays(30)); // 默认30天
        progress.setSubmissionCount(0);
        progress.setStatus(UserProgress.ProgressStatus.IN_PROGRESS);

        return progressRepository.save(progress);
    }

    public UserProgress updateProgress(Long userId, Long projectId, Integer completedModules, Integer submissionCount) {
        Optional<UserProgress> progressOpt = progressRepository.findByUserIdAndProjectId(userId, projectId);

        if (progressOpt.isPresent()) {
            UserProgress progress = progressOpt.get();
            progress.setCompletedModules(completedModules);
            progress.setSubmissionCount(submissionCount);

            double percentage = (double) completedModules / progress.getTotalModules() * 100;
            progress.setProgressPercentage(percentage);

            if (completedModules >= progress.getTotalModules()) {
                progress.setStatus(UserProgress.ProgressStatus.COMPLETED);
            }

            return progressRepository.save(progress);
        }

        return null;
    }
}