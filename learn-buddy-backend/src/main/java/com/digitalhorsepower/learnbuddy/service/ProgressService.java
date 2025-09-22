package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.LearningProgress;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.entity.StudyPlan;
import com.digitalhorsepower.learnbuddy.repository.LearningProgressRepository;
import com.digitalhorsepower.learnbuddy.repository.UserRepository;
import com.digitalhorsepower.learnbuddy.repository.StudyPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressService {

    @Autowired
    private LearningProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    public List<LearningProgress> getProgressByUserId(Long userId) {
        return progressRepository.findByUserId(userId);
    }

    public List<LearningProgress> getRecentProgressByUserId(Long userId) {
        return progressRepository.findRecentProgressByUserId(userId);
    }

    public LearningProgress recordProgress(LearningProgress progress, Long userId, Long planId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<StudyPlan> plan = studyPlanRepository.findById(planId);

        if (user.isPresent() && plan.isPresent()) {
            progress.setUser(user.get());
            progress.setStudyPlan(plan.get());
            return progressRepository.save(progress);
        }
        throw new RuntimeException("用户或学习计划不存在");
    }

    public void deleteProgress(Long progressId) {
        progressRepository.deleteById(progressId);
    }
}