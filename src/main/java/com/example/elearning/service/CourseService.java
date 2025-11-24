package com.example.elearning.service;

import com.example.elearning.entity.Course;
import com.example.elearning.entity.User;
import com.example.elearning.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepo;

    public CourseService(CourseRepository courseRepo) {
        this.courseRepo = courseRepo;
    }

    public Course create(Course course) {
        return courseRepo.save(course);
    }

    public Optional<Course> findById(Long id) {
        return courseRepo.findById(id);
    }

    public List<Course> findAll() {
        return courseRepo.findAll();
    }

    public List<Course> findByTeacher(User teacher) {
        return courseRepo.findByTeacher(teacher);
    }
}
