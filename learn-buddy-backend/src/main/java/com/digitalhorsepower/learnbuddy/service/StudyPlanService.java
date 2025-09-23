package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.StudyPlan;
import com.digitalhorsepower.learnbuddy.entity.User;
import com.digitalhorsepower.learnbuddy.repository.StudyPlanRepository;
import com.digitalhorsepower.learnbuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudyPlanService {

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @Autowired
    private UserRepository userRepository;

    public List<StudyPlan> getPlansByUserId(Long userId) {
        return studyPlanRepository.findByUserId(userId);
    }

    public StudyPlan createPlan(StudyPlan plan, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            plan.setUser(user.get());
            return studyPlanRepository.save(plan);
        }
        throw new RuntimeException("用户不存在");
    }

    public StudyPlan updatePlan(Long planId, StudyPlan planDetails) {
        return studyPlanRepository.findById(planId).map(plan -> {
            plan.setPlanName(planDetails.getPlanName());
            plan.setDescription(planDetails.getDescription());
            plan.setCourseName(planDetails.getCourseName());
            plan.setStartDate(planDetails.getStartDate());
            plan.setEndDate(planDetails.getEndDate());
            plan.setStatus(planDetails.getStatus());
            return studyPlanRepository.save(plan);
        }).orElseThrow(() -> new RuntimeException("学习计划不存在"));
    }

    public void deletePlan(Long planId) {
        studyPlanRepository.deleteById(planId);
    }
}