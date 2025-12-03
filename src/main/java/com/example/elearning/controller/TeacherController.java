package com.example.elearning.controller;

import com.example.elearning.entity.Course;
import com.example.elearning.entity.Lesson;
import com.example.elearning.entity.User;
import com.example.elearning.service.CourseService;
import com.example.elearning.service.LessonService;
import com.example.elearning.service.UserService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // @GetMapping("/course/{id}/create-lesson")
    // public String createLessonForm(@PathVariable Long id, Model model) {
    //     Lesson lesson = new Lesson();
    //     lesson.setCourse(courseService.findById(id).orElse(null));
    //     model.addAttribute("lesson", lesson);
    //     model.addAttribute("id", id);   // ADD THIS
    //     return "teacher/create-lesson";
    // }

@GetMapping("/course/{id}/create-lesson")
public String createLessonForm(@PathVariable Long id, Model model) {

    Lesson lesson = new Lesson();
    Course course = courseService.findById(id).orElseThrow();
    lesson.setCourse(course);

    model.addAttribute("lesson", lesson);
    model.addAttribute("courseId", id);

    return "teacher/create-lesson";
}

@PostMapping("/course/{id}/create-lesson")
public String createLesson(
        @PathVariable Long id,
        @ModelAttribute("lesson") Lesson formLesson,
        @RequestParam("videoFile") MultipartFile videoFile
) {

    Course course = courseService.findById(id).orElseThrow();

    Lesson lesson = new Lesson();
    lesson.setTitle(formLesson.getTitle());
    lesson.setContent(formLesson.getContent());
    lesson.setCourse(course);

    // Handle the video upload
    if (!videoFile.isEmpty()) {

        try {
            String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
            Path uploadPath = Paths.get("uploads/videos");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(videoFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            lesson.setVideoUrl("/videos/" + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    lessonService.create(lesson);

    return "redirect:/teacher/dashboard";
}


//     @PostMapping("/course/{id}/create-lesson")
// public String createLesson(
//         @PathVariable Long id,
//         @ModelAttribute("lesson") Lesson formLesson) {

//     Course course = courseService.findById(id).orElseThrow();

//     Lesson newLesson = new Lesson();
//     newLesson.setTitle(formLesson.getTitle());
//     newLesson.setContent(formLesson.getContent());
//     newLesson.setCourse(course);

//     lessonService.create(newLesson);

//     return "redirect:/teacher/dashboard";
// }

}
