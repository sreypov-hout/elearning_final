package com.example.elearning.repository;

import com.example.elearning.entity.Enrollment;
import com.example.elearning.entity.Course;
import com.example.elearning.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(User student);
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    List<Enrollment> findByCourse(Course course);
}
