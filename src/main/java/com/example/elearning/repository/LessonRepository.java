package com.example.elearning.repository;

import com.example.elearning.entity.Lesson;
import com.example.elearning.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourse(Course course);
}
