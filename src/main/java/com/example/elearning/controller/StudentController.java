package com.example.elearning.controller;

import com.example.elearning.entity.Course;
import com.example.elearning.entity.Enrollment;
import com.example.elearning.entity.Lesson;
import com.example.elearning.entity.User;
import com.example.elearning.service.CourseService;
import com.example.elearning.service.EnrollmentService;
import com.example.elearning.service.LessonService;
import com.example.elearning.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final LessonService lessonService;

    public StudentController(CourseService courseService, EnrollmentService enrollmentService,
                             UserService userService, LessonService lessonService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.lessonService = lessonService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        String email = auth.getName();
        User student = userService.findByEmail(email).orElse(null);

        List<Enrollment> myEnrollments = enrollmentService.findByStudent(student);
        List<Course> enrolledCourses = myEnrollments.stream().map(Enrollment::getCourse).collect(Collectors.toList());

        model.addAttribute("allCourses", courseService.findAll());
        model.addAttribute("enrolledCourses", enrolledCourses);
        return "student/dashboard";
    }

    @PostMapping("/enroll/{courseId}")
    public String enroll(@PathVariable Long courseId, Authentication auth, Model model) {
        String email = auth.getName();
        User student = userService.findByEmail(email).orElse(null);
        Course course = courseService.findById(courseId).orElse(null);
        if (student == null || course == null) {
            return "redirect:/student/dashboard";
        }
        if (!enrollmentService.isEnrolled(student, course)) {
            enrollmentService.enroll(student, course);
        }
        return "redirect:/student/dashboard";
    }

    @GetMapping("/course/{id}")
    public String viewCourse(@PathVariable Long id, Authentication auth, Model model) {
        String email = auth.getName();
        User student = userService.findByEmail(email).orElse(null);
        Course course = courseService.findById(id).orElse(null);
        if (student == null || course == null) {
            return "redirect:/student/dashboard";
        }
        // check enrollment
        if (!enrollmentService.isEnrolled(student, course)) {
            return "redirect:/student/dashboard";
        }
        List<Lesson> lessons = lessonService.findByCourse(course);
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        return "student/course-details";
    }
}
