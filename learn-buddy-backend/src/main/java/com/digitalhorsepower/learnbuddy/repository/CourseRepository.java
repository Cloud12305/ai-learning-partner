// CourseRepository.java
package com.digitalhorsepower.learnbuddy.repository;

import com.digitalhorsepower.learnbuddy.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
}