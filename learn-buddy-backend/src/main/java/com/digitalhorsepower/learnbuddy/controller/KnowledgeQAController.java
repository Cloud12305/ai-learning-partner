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

            // 添加调试信息
            System.out.println(">>> 返回响应数据:");
            System.out.println(">>> - success: " + response.isSuccess());
            System.out.println(">>> - answer length: " + (response.getAnswer() != null ? response.getAnswer().length() : 0));
            System.out.println(">>> - graph nodes: " + (response.getKnowledgeGraph() != null ? response.getKnowledgeGraph().size() : 0));

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