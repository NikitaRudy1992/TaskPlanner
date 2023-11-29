package com.learning.taskplanner.controllers;

import com.learning.taskplanner.interfaces.UserService;
import com.learning.taskplanner.model.User;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        userService.registerNewUser(user);
        return "redirect:/login?registrationSuccess";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}

