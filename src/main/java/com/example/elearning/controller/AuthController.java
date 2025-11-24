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

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "auth/register";
        }
        // default new registrants are STUDENT (for demo).
        user.setRole(Role.STUDENT);
        userService.register(user);
        return "redirect:/login?registered";
    }
}
