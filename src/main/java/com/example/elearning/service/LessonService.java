package com.example.elearning.service;

import com.example.elearning.entity.Lesson;
import com.example.elearning.entity.Course;
import com.example.elearning.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private final LessonRepository lessonRepo;

    public LessonService(LessonRepository lessonRepo) {
        this.lessonRepo = lessonRepo;
    }

    // CREATE or UPDATE
    public Lesson save(Lesson lesson) {
        return lessonRepo.save(lesson);
    }

    // FIND by ID
    public Optional<Lesson> findById(Long id) {
        return lessonRepo.findById(id);
    }

    // FIND all lessons of a course
    public List<Lesson> findByCourse(Course course) {
        return lessonRepo.findByCourse(course);
    }

    // DELETE
    public void deleteById(Long id) {
        lessonRepo.deleteById(id);
    }

    
}
