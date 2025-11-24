// src/main/java/com/digitalhorsepower/learnbuddy/controller/InterviewController.java
package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.entity.InterviewQuestion;
import com.digitalhorsepower.learnbuddy.service.InterviewQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InterviewController {

    private final InterviewQuestionService questionService;

    @GetMapping("/questions/random")
    public ResponseEntity<List<InterviewQuestion>> getRandomQuestions(
            @RequestParam(defaultValue = "20") int count) {
        return ResponseEntity.ok(questionService.getRandomQuestions(count));
    }

    @GetMapping("/questions/category")
    public ResponseEntity<List<InterviewQuestion>> getQuestionsByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "20") int count) {
        // 将中文类别转换为枚举值
        InterviewQuestion.QuestionCategory categoryEnum = convertCategoryToEnum(category);
        return ResponseEntity.ok(questionService.getQuestionsByCategory(categoryEnum, count));
    }

    @GetMapping("/questions/language")
    public ResponseEntity<List<InterviewQuestion>> getQuestionsByLanguage(
            @RequestParam String language,
            @RequestParam(defaultValue = "20") int count) {
        // 将中文语言转换为枚举值
        InterviewQuestion.ProgrammingLanguage languageEnum = convertLanguageToEnum(language);
        return ResponseEntity.ok(questionService.getQuestionsByLanguage(languageEnum, count));
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateAnswers(@RequestBody Map<Long, String> userAnswers) {
        return ResponseEntity.ok(questionService.validateAnswers(userAnswers));
    }

    @PostMapping("/questions")
    public ResponseEntity<InterviewQuestion> createQuestion(@RequestBody InterviewQuestion question) {
        return ResponseEntity.ok(questionService.saveQuestion(question));
    }

    // 辅助方法：将中文类别转换为枚举
    private InterviewQuestion.QuestionCategory convertCategoryToEnum(String category) {
        switch (category) {
            case "前端": return InterviewQuestion.QuestionCategory.FRONTEND;
            case "后端": return InterviewQuestion.QuestionCategory.BACKEND;
            case "数据库": return InterviewQuestion.QuestionCategory.DATABASE;
            case "算法": return InterviewQuestion.QuestionCategory.ALGORITHM;
            case "系统设计": return InterviewQuestion.QuestionCategory.SYSTEM_DESIGN;
            case "面向对象": return InterviewQuestion.QuestionCategory.OOP;
            case "网络": return InterviewQuestion.QuestionCategory.NETWORK;
            case "安全": return InterviewQuestion.QuestionCategory.SECURITY;
            default: return InterviewQuestion.QuestionCategory.FRONTEND;
        }
    }

    // 辅助方法：将中文语言转换为枚举
    private InterviewQuestion.ProgrammingLanguage convertLanguageToEnum(String language) {
        switch (language) {
            case "Java": return InterviewQuestion.ProgrammingLanguage.JAVA;
            case "JavaScript": return InterviewQuestion.ProgrammingLanguage.JAVASCRIPT;
            case "Python": return InterviewQuestion.ProgrammingLanguage.PYTHON;
            case "C++": return InterviewQuestion.ProgrammingLanguage.CPP;
            case "C#": return InterviewQuestion.ProgrammingLanguage.CSHARP;
            case "PHP": return InterviewQuestion.ProgrammingLanguage.PHP;
            case "Go": return InterviewQuestion.ProgrammingLanguage.GO;
            case "Rust": return InterviewQuestion.ProgrammingLanguage.RUST;
            case "Swift": return InterviewQuestion.ProgrammingLanguage.SWIFT;
            case "Kotlin": return InterviewQuestion.ProgrammingLanguage.KOTLIN;
            default: return InterviewQuestion.ProgrammingLanguage.JAVA;
        }
    }
}