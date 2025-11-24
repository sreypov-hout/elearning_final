package com.example.elearning.service;

import com.example.elearning.entity.Course;
import com.example.elearning.entity.Enrollment;
import com.example.elearning.entity.User;
import com.example.elearning.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
// import java.util.Optional;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepo;

    public EnrollmentService(EnrollmentRepository enrollmentRepo) {
        this.enrollmentRepo = enrollmentRepo;
    }

    public Enrollment enroll(User student, Course course) {
        Enrollment e = new Enrollment();
        e.setStudent(student);
        e.setCourse(course);
        return enrollmentRepo.save(e);
    }

    public boolean isEnrolled(User student, Course course) {
        return enrollmentRepo.findByStudentAndCourse(student, course).isPresent();
    }

    public List<Enrollment> findByStudent(User student) {
        return enrollmentRepo.findByStudent(student);
    }
}
