package com.example.elearning.controller;

import com.example.elearning.entity.Role;
import com.example.elearning.entity.User;
import com.example.elearning.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Home page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Login
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // -------------------------------
    // STUDENT REGISTER PAGE
    // -------------------------------
    @GetMapping("/register/student")
    public String studentRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", "STUDENT");
        return "auth/register";   // uses same register page
    }

    // -------------------------------
    // TEACHER REGISTER PAGE
    // -------------------------------
    @GetMapping("/register/teacher")
    public String teacherRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", "TEACHER");
        return "auth/register";   // uses same register page
    }

    // -------------------------------
    // SUBMIT REGISTER FORM
    // -------------------------------
    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") User user,
            @RequestParam("role") String role,
            Model model) {

        // Check email exists
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("role", role);
            return "auth/register";
        }

        // Convert String to Enum
        user.setRole(Role.valueOf(role));

        // Save user
        userService.register(user);

        return "redirect:/login?registered";
    }
}
