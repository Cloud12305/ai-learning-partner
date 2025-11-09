package com.digitalhorsepower.learnbuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.digitalhorsepower.learnbuddy.dto.PsychologyRequest;
import com.digitalhorsepower.learnbuddy.dto.PsychologyResponse;
import com.digitalhorsepower.learnbuddy.service.PsychologyService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/psychology")
@CrossOrigin(origins = "*") // 允许前端跨域访问
public class PsychologyController {

    @Autowired
    private PsychologyService psychologyService;

    @PostMapping
    public ResponseEntity<?> analyzeEmotion(@RequestBody PsychologyRequest request) {
        try {
            PsychologyResponse response = psychologyService.processEmotionAnalysis(
                    request.getText(),
                    request.getUserId()
            );

            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "data", response
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "msg", "情绪分析服务暂时不可用，请稍后重试"
            ));
        }
    }
}