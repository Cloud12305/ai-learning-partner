// src/main/java/com/digitalhorsepower/learnbuddy/dto/QuizResultDTO.java
package com.digitalhorsepower.learnbuddy.dto;

import lombok.Data;
import java.util.Map;

@Data
public class QuizResultDTO {
    private int totalQuestions;
    private int correctAnswers;
    private int score;
    private Map<Long, Boolean> results;

    public QuizResultDTO(int totalQuestions, int correctAnswers, int score, Map<Long, Boolean> results) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.score = score;
        this.results = results;
    }
}