package com.example.elearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseController {

    @GetMapping("/courses")
    public String coursesPage(Model model) {
        // later you can load courses here
        return "courses";  // This returns courses.html
    }
}
