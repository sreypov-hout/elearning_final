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

    // CREATE or UPDATE
    public Course save(Course course) {
        return courseRepo.save(course);
    }

    // FIND by ID
    public Optional<Course> findById(Long id) {
        return courseRepo.findById(id);
    }

    // FIND all courses
    public List<Course> findAll() {
        return courseRepo.findAll();
    }

    // FIND courses by teacher
    public List<Course> findByTeacher(User teacher) {
        return courseRepo.findByTeacher(teacher);
    }

    // DELETE
    public void deleteById(Long id) {
        courseRepo.deleteById(id);
    }
}
