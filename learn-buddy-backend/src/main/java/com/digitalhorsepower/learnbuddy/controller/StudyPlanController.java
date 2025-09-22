package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.ApiResponse;
import com.digitalhorsepower.learnbuddy.entity.StudyPlan;
import com.digitalhorsepower.learnbuddy.service.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class StudyPlanController {

    @Autowired
    private StudyPlanService studyPlanService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<StudyPlan>> getPlansByUser(@PathVariable Long userId) {
        List<StudyPlan> plans = studyPlanService.getPlansByUserId(userId);
        return ApiResponse.success("获取学习计划成功", plans);
    }

    @PostMapping("/user/{userId}")
    public ApiResponse<StudyPlan> createPlan(@PathVariable Long userId, @RequestBody StudyPlan plan) {
        try {
            StudyPlan createdPlan = studyPlanService.createPlan(plan, userId);
            return ApiResponse.success("学习计划创建成功", createdPlan);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{planId}")
    public ApiResponse<StudyPlan> updatePlan(@PathVariable Long planId, @RequestBody StudyPlan planDetails) {
        try {
            StudyPlan updatedPlan = studyPlanService.updatePlan(planId, planDetails);
            return ApiResponse.success("学习计划更新成功", updatedPlan);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{planId}")
    public ApiResponse<String> deletePlan(@PathVariable Long planId) {
        try {
            studyPlanService.deletePlan(planId);
            return ApiResponse.success("学习计划删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除学习计划失败");
        }
    }
}