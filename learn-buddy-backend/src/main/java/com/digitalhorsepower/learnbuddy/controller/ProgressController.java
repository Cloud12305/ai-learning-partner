package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.ApiResponse;
import com.digitalhorsepower.learnbuddy.entity.LearningProgress; // 添加导入
import com.digitalhorsepower.learnbuddy.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<LearningProgress>> getProgressByUser(@PathVariable Long userId) {
        List<LearningProgress> progressList = progressService.getProgressByUserId(userId);
        return ApiResponse.success("获取学习进度成功", progressList);
    }

    @GetMapping("/user/{userId}/recent")
    public ApiResponse<List<LearningProgress>> getRecentProgress(@PathVariable Long userId) {
        List<LearningProgress> progressList = progressService.getRecentProgressByUserId(userId);
        return ApiResponse.success("获取最近学习进度成功", progressList);
    }

    @PostMapping("/user/{userId}/plan/{planId}")
    public ApiResponse<LearningProgress> recordProgress(
            @PathVariable Long userId,
            @PathVariable Long planId,
            @RequestBody LearningProgress progress) {
        try {
            LearningProgress recordedProgress = progressService.recordProgress(progress, userId, planId);
            return ApiResponse.success("学习进度记录成功", recordedProgress);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{progressId}")
    public ApiResponse<String> deleteProgress(@PathVariable Long progressId) {
        try {
            progressService.deleteProgress(progressId);
            return ApiResponse.success("学习进度删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error("删除学习进度失败");
        }
    }
}