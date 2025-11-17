// KnowledgeQAController.java
package com.digitalhorsepower.learnbuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.digitalhorsepower.learnbuddy.dto.QARequest;
import com.digitalhorsepower.learnbuddy.dto.QAResponse;
import com.digitalhorsepower.learnbuddy.service.KnowledgeGraphService;
import java.util.Map;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeQAController {

    @Autowired
    private KnowledgeGraphService knowledgeGraphService;

    @PostMapping("/qa")
    public ResponseEntity<?> answerQuestion(@RequestBody QARequest request) {
        System.out.println(">>> /api/knowledge/qa called with question = " + request.getQuestion());

        try {
            QAResponse response = knowledgeGraphService.processQuestion(
                    request.getQuestion(),
                    request.getUserId(),
                    request.getContextKp()
            );

            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "data", response
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "msg", "知识问答服务暂时不可用，请稍后重试"
            ));
        }
    }
}