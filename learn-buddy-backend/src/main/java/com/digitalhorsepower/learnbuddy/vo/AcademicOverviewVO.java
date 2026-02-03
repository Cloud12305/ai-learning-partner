package com.digitalhorsepower.learnbuddy.vo;

import lombok.Data;
import java.util.List;

@Data
public class AcademicOverviewVO {

    // 当前学年
    private String currentAcademicYear;

    // 当前学期
    private Integer currentSemester;

    // 本学期绩点
    private Double currentSemesterGPA;

    // 累计绩点
    private Double overallGPA;

    // 总学分
    private Double totalCredits;

    // 实验项目数量
    private Long labProjectsCount;

    // 借阅书籍数量
    private Long borrowedBooksCount;

    // 各学期详情
    private List<SemesterGPA> semesterGPAs;

    @Data
    public static class SemesterGPA {
        // 学年
        private String academicYear;

        // 学期
        private Integer semester;

        // 总学分
        private Double totalCredits;

        // 平均绩点
        private Double averageGPA;

        // 课程数
        private Integer courseCount;

        // 总绩点（学分*绩点）
        private Double totalGradePoints;
    }
}