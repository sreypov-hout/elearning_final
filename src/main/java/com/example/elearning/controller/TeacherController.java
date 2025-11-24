package com.example.elearning.controller;

import com.example.elearning.entity.Course;
import com.example.elearning.entity.Lesson;
import com.example.elearning.entity.User;
import com.example.elearning.service.CourseService;
import com.example.elearning.service.LessonService;
import com.example.elearning.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;

    public TeacherController(CourseService courseService, LessonService lessonService, UserService userService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        String email = auth.getName();
        User teacher = userService.findByEmail(email).orElse(null);
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courseService.findByTeacher(teacher));
        return "teacher/dashboard";
    }

    @GetMapping("/create-course")
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "teacher/create-course";
    }

    @PostMapping("/create-course")
    public String createCourse(@ModelAttribute("course") Course course, Authentication auth) {
        String email = auth.getName();
        User teacher = userService.findByEmail(email).orElse(null);
        course.setTeacher(teacher);
        courseService.create(course);
        return "redirect:/teacher/dashboard";
    }

    @GetMapping("/course/{id}/create-lesson")
    public String createLessonForm(@PathVariable Long id, Model model) {
        Lesson lesson = new Lesson();
        lesson.setCourse(courseService.findById(id).orElse(null));
        model.addAttribute("lesson", lesson);
        return "teacher/create-lesson";
    }

    @PostMapping("/course/{id}/create-lesson")
    public String createLesson(@PathVariable Long id, @ModelAttribute("lesson") Lesson lesson) {
        courseService.findById(id).ifPresent(course -> {
            lesson.setCourse(course);
            lessonService.create(lesson);
        });
        return "redirect:/teacher/dashboard";
    }
}
