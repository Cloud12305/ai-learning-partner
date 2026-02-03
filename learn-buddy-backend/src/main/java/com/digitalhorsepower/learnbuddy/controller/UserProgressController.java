// src/main/java/com/digitalhorsepower/learnbuddy/controller/UserProgressController.java
package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.entity.UserProgress;
import com.digitalhorsepower.learnbuddy.service.UserProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-progress")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserProgressController {

    private final UserProgressService progressService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserProgress>> getUserProgress(@PathVariable Long userId) {
        return ResponseEntity.ok(progressService.getUserProgress(userId));
    }

    @GetMapping("/user/{userId}/project/{projectId}")
    public ResponseEntity<UserProgress> getProjectProgress(
            @PathVariable Long userId,
            @PathVariable Long projectId) {

        Optional<UserProgress> progress = progressService.getProjectProgress(userId, projectId);
        return progress.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/start")
    public ResponseEntity<UserProgress> startProject(
            @RequestBody Map<String, Object> request) {

        Long userId = Long.valueOf(request.get("userId").toString());
        Long projectId = Long.valueOf(request.get("projectId").toString());
        Integer totalModules = Integer.valueOf(request.get("totalModules").toString());

        return ResponseEntity.ok(progressService.startProject(userId, projectId, totalModules));
    }

    @PutMapping("/update")
    public ResponseEntity<UserProgress> updateProgress(
            @RequestBody Map<String, Object> request) {

        Long userId = Long.valueOf(request.get("userId").toString());
        Long projectId = Long.valueOf(request.get("projectId").toString());
        Integer completedModules = Integer.valueOf(request.get("completedModules").toString());
        Integer submissionCount = Integer.valueOf(request.get("submissionCount").toString());

        UserProgress progress = progressService.updateProgress(userId, projectId, completedModules, submissionCount);
        return progress != null ? ResponseEntity.ok(progress) : ResponseEntity.notFound().build();
    }
}