package com.learning.taskplanner.controllers;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public String showTasks(Model model, @AuthenticationPrincipal User currentUser) {
        List<Task> tasks = taskService.getTasksByUser(currentUser);
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new Task());
        return "tasklist";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute("task") Task task, BindingResult result, @AuthenticationPrincipal User currentUser) {
        if (result.hasErrors()) {
            return "tasklist";
        }

        taskService.createTask(task, currentUser.getUserId());

        return "redirect:/tasks";
    }
    @PostMapping("/updateStatus/{taskId}")
    public String updateTaskStatus(@PathVariable Long taskId, @RequestParam("status") TaskStatus status) {
        taskService.updateTaskStatus(taskId, status);
        return "redirect:/tasks";
    }
}