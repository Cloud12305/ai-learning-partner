package com.digitalhorsepower.learnbuddy.service;

import com.digitalhorsepower.learnbuddy.entity.InterviewQuestion;
import com.digitalhorsepower.learnbuddy.repository.InterviewQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final InterviewQuestionRepository questionRepository;

    public List<InterviewQuestion> getRandomQuestions(int count) {
        return questionRepository.findRandomQuestions(count);
    }

    public List<InterviewQuestion> getQuestionsByCategory(InterviewQuestion.QuestionCategory category, int count) {
        return questionRepository.findRandomByCategory(category.name(), count);
    }

    public List<InterviewQuestion> getQuestionsByLanguage(InterviewQuestion.ProgrammingLanguage language, int count) {
        return questionRepository.findRandomByLanguage(language.name(), count);
    }

    public Map<String, Object> validateAnswers(Map<Long, String> userAnswers) {
        int correctCount = 0;
        int totalCount = userAnswers.size();
        Map<Long, Boolean> results = new java.util.HashMap<>();

        for (Map.Entry<Long, String> entry : userAnswers.entrySet()) {
            Long questionId = entry.getKey();
            String userAnswer = entry.getValue();

            InterviewQuestion question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(userAnswer);
                results.put(questionId, isCorrect);
                if (isCorrect) {
                    correctCount++;
                }
            }
        }

        double score = (double) correctCount / totalCount * 100;

        return Map.of(
                "totalQuestions", totalCount,
                "correctAnswers", correctCount,
                "score", Math.round(score),
                "results", results
        );
    }

    public InterviewQuestion saveQuestion(InterviewQuestion question) {
        return questionRepository.save(question);
    }
}