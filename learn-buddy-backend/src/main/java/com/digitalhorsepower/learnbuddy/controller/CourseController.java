package com.digitalhorsepower.learnbuddy.controller;

import com.digitalhorsepower.learnbuddy.dto.CourseDTO;
import com.digitalhorsepower.learnbuddy.dto.CourseSearchRequest;
import com.digitalhorsepower.learnbuddy.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    /**
     * 获取推荐课程
     * GET /api/courses/recommended
     */
    @GetMapping("/recommended")
    public ResponseEntity<List<CourseDTO>> getRecommendedCourses() {
        try {
            log.info("收到获取推荐课程请求");
            List<CourseDTO> courses = courseService.getRecommendedCourses();
            log.info("返回推荐课程数量: {}", courses.size());
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("获取推荐课程失败", e);
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    /**
     * 手动抓取课程
     * POST /api/courses/scrape
     */
    @PostMapping("/scrape")
    public ResponseEntity<List<CourseDTO>> scrapeCourses() {
        try {
            log.info("收到手动抓取课程请求");
            List<CourseDTO> courses = courseService.getRecommendedCourses();
            log.info("抓取课程返回数量: {}", courses.size());
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("抓取课程失败", e);
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    /**
     * 搜索课程
     * GET /api/courses/search?keyword=xxx&category=xxx&difficulty=xxx
     */
    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String university
    ) {
        try {
            log.info("收到搜索课程请求 - keyword: {}, category: {}, difficulty: {}",
                    keyword, category, difficulty);

            CourseSearchRequest request = new CourseSearchRequest();
            request.setKeyword(keyword);
            request.setCategory(category);
            request.setDifficulty(difficulty);
            request.setUniversity(university);

            List<CourseDTO> courses = courseService.searchCourses(request);
            log.info("搜索结果数量: {}", courses.size());
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("搜索课程失败", e);
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
}