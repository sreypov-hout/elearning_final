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

    public Lesson create(Lesson lesson) {
        return lessonRepo.save(lesson);
    }

    public List<Lesson> findByCourse(Course course) {
        return lessonRepo.findByCourse(course);
    }

    public Optional<Lesson> findById(Long id) {
        return lessonRepo.findById(id);
    }
}
