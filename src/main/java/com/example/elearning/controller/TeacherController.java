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
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    /** ---------------- DASHBOARD ---------------- **/
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        User teacher = userService.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courseService.findByTeacher(teacher));
        return "teacher/dashboard";
    }

    /** ---------------- COURSE CRUD ---------------- **/

    // CREATE COURSE FORM
    @GetMapping("/create-course")
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "teacher/create-course";
    }

    // CREATE COURSE POST
    @PostMapping("/create-course")
    public String createCourse(@ModelAttribute("course") Course course, Authentication auth) {
        User teacher = userService.findByEmail(auth.getName()).orElseThrow();
        course.setTeacher(teacher);
        courseService.save(course);
        return "redirect:/teacher/dashboard";
    }

    // EDIT COURSE FORM
    @GetMapping("/edit-course/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id).orElseThrow();
        model.addAttribute("course", course);
        return "teacher/edit-course";
    }

    // UPDATE COURSE POST
    @PostMapping("/edit-course/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute("course") Course updatedCourse) {
        Course course = courseService.findById(id).orElseThrow();
        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());
        courseService.save(course);
        return "redirect:/teacher/dashboard";
    }

    // DELETE COURSE
    @GetMapping("/delete-course/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteById(id);
        return "redirect:/teacher/dashboard";
    }

    /** ---------------- LESSON CRUD ---------------- **/

    // CREATE LESSON FORM
    @GetMapping("/course/{courseId}/create-lesson")
    public String createLessonForm(@PathVariable Long courseId, Model model) {
        Lesson lesson = new Lesson();
        lesson.setCourse(courseService.findById(courseId).orElseThrow());
        model.addAttribute("lesson", lesson);
        model.addAttribute("courseId", courseId);
        return "teacher/create-lesson";
    }

    // CREATE LESSON POST
    @PostMapping("/course/{courseId}/create-lesson")
    public String createLesson(
            @PathVariable Long courseId,
            @ModelAttribute("lesson") Lesson formLesson,
            @RequestParam("videoFile") MultipartFile videoFile
    ) {
        Lesson lesson = new Lesson();
        lesson.setCourse(courseService.findById(courseId).orElseThrow());
        lesson.setTitle(formLesson.getTitle());
        lesson.setContent(formLesson.getContent());

        if (!videoFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/videos");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.copy(videoFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                lesson.setVideoUrl("/videos/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        lessonService.save(lesson);
        return "redirect:/teacher/dashboard";
    }

    // EDIT LESSON FORM
    @GetMapping("/course/{courseId}/edit-lesson/{lessonId}")
    public String editLessonForm(@PathVariable Long courseId, @PathVariable Long lessonId, Model model) {
        Lesson lesson = lessonService.findById(lessonId).orElseThrow();
        model.addAttribute("lesson", lesson);
        model.addAttribute("courseId", courseId);
        return "teacher/edit-lesson";
    }

    // UPDATE LESSON POST
    @PostMapping("/course/{courseId}/edit-lesson/{lessonId}")
    public String updateLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @ModelAttribute("lesson") Lesson formLesson,
            @RequestParam("videoFile") MultipartFile videoFile
    ) {
        Lesson lesson = lessonService.findById(lessonId).orElseThrow();
        lesson.setTitle(formLesson.getTitle());
        lesson.setContent(formLesson.getContent());

        if (!videoFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/videos");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.copy(videoFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                lesson.setVideoUrl("/videos/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        lessonService.save(lesson);
        return "redirect:/teacher/dashboard";
    }

    // DELETE LESSON
    @GetMapping("/course/{courseId}/delete-lesson/{lessonId}")
    public String deleteLesson(@PathVariable Long courseId, @PathVariable Long lessonId) {
        lessonService.deleteById(lessonId);
        return "redirect:/teacher/dashboard";
    }
}
