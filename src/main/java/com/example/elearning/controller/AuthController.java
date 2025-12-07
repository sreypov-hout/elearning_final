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

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    // Popup page to choose role
    @GetMapping("/register")
    public String chooseRegister() {
        return "auth/choose-register";
    }

    @GetMapping("/register/student")
    public String studentRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", "STUDENT");
        return "auth/register";
    }

    @GetMapping("/register/teacher")
    public String teacherRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("role", "TEACHER");
        return "auth/register";
    }

    // Save user
    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") User user,
            @RequestParam("role") String role,
            Model model) {

        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            model.addAttribute("role", role);
            return "auth/register";
        }

        user.setRole(Role.valueOf(role));
        userService.register(user);

        return "redirect:/login?registered";
    }
}
