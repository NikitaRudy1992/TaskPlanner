package com.learning.taskplanner.controllers;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private TaskService taskService;

    @GetMapping
    public String showTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasklist";
    }

    @GetMapping("/create")
    public String showTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "taskform";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute("task") @Valid Task task, BindingResult result) {
        if (result.hasErrors()) {
            return "taskform";
        }

        taskService.createTask(task);
        return "redirect:/tasks";
    }
}