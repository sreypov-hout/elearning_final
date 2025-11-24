package com.example.elearning.controller;

import com.example.elearning.entity.Role;
import com.example.elearning.entity.User;
import com.example.elearning.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/create-teacher")
    public String createTeacherForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/create-teacher";
    }

    @PostMapping("/create-teacher")
    public String createTeacher(@ModelAttribute("user") User user, Model model) {
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "admin/create-teacher";
        }
        user.setRole(Role.TEACHER);
        userService.register(user);
        model.addAttribute("success", "Teacher created");
        return "admin/create-teacher";
    }
}
