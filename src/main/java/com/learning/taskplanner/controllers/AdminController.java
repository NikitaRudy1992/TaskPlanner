package com.learning.taskplanner.controllers;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final TaskService taskService;
    private final UserService userService;

    // Конструктор

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("users", userService.getAllUsers());
        return "adminDashboard";
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return "redirect:/admin";
    }

    @GetMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }
}
