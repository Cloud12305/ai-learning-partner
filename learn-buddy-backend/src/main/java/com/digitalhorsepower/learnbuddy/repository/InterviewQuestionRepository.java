package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    @Query(value = "SELECT * FROM interview_questions ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<InterviewQuestion> findRandomQuestions(@Param("count") int count);

    List<InterviewQuestion> findByCategory(InterviewQuestion.QuestionCategory category);

    List<InterviewQuestion> findByLanguage(InterviewQuestion.ProgrammingLanguage language);

    List<InterviewQuestion> findByCategoryAndLanguage(InterviewQuestion.QuestionCategory category,
                                                      InterviewQuestion.ProgrammingLanguage language);

    @Query(value = "SELECT * FROM interview_questions WHERE category = :category ORDER BY RAND() LIMIT :count",
            nativeQuery = true)
    List<InterviewQuestion> findRandomByCategory(@Param("category") String category,
                                                 @Param("count") int count);

    @Query(value = "SELECT * FROM interview_questions WHERE language = :language ORDER BY RAND() LIMIT :count",
            nativeQuery = true)
    List<InterviewQuestion> findRandomByLanguage(@Param("language") String language,
                                                 @Param("count") int count);

    List<InterviewQuestion> findByType(InterviewQuestion.QuestionType type);

    List<InterviewQuestion> findByDifficultyLevel(Integer difficultyLevel);
}