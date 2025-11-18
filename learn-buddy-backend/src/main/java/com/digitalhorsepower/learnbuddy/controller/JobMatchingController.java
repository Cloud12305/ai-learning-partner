package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.CareerPreferenceDTO;
import com.digitalhorsepower.learnbuddy.dto.JobMatchRequestDTO;
import com.digitalhorsepower.learnbuddy.dto.JobMatchResponseDTO;
import com.digitalhorsepower.learnbuddy.service.CareerPreferenceService;
import com.digitalhorsepower.learnbuddy.service.JobMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/job-matching")
@RequiredArgsConstructor
public class JobMatchingController {

    private final JobMatchingService jobMatchingService;
    private final CareerPreferenceService careerPreferenceService;

    @PostMapping("/match")
    public ResponseEntity<JobMatchResponseDTO> matchJobs(@Valid @RequestBody JobMatchRequestDTO request) {
        JobMatchResponseDTO response = jobMatchingService.matchJobs(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<JobMatchResponseDTO> getJobMatchesByUser(@PathVariable Long userId) {
        JobMatchRequestDTO request = new JobMatchRequestDTO();
        request.setUserId(userId);

        JobMatchResponseDTO response = jobMatchingService.matchJobs(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/career-preference/{userId}")
    public ResponseEntity<String> updateCareerPreference(
            @PathVariable Long userId,
            @Valid @RequestBody CareerPreferenceDTO preferenceDTO) {
        careerPreferenceService.saveOrUpdateCareerPreference(userId, preferenceDTO);
        return ResponseEntity.ok("求职意向更新成功");
    }

    @GetMapping("/career-preference/{userId}")
    public ResponseEntity<CareerPreferenceDTO> getCareerPreference(@PathVariable Long userId) {
        CareerPreferenceDTO preference = careerPreferenceService.getCareerPreference(userId);
        return ResponseEntity.ok(preference);
    }
}