package com.learning.taskplanner.controllers;

import com.learning.taskplanner.interfaces.SubTaskService;
import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskPriority;
import com.learning.taskplanner.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final SubTaskService subTaskService;

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

    @GetMapping("/search")
    public String searchTasks(@RequestParam(required = false) String title,
                              @RequestParam(required = false) TaskStatus status,
                              @RequestParam(required = false) TaskPriority priority,
                              @RequestParam(required = false) LocalDate deadline,
                              @AuthenticationPrincipal User currentUser,
                              Model model) {
        List<Task> searchResults = taskService.searchTasks(title, status, priority, deadline, currentUser);
        model.addAttribute("searchResults", searchResults);
        List<Task> tasks = taskService.getTasksByUser(currentUser);
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new Task());
        return "tasklist";
    }

    @PostMapping("/subtasks/updateStatus/{subTaskId}")
    public String updateSubTask(@PathVariable Long subTaskId,
                                      @RequestParam("status") TaskStatus status) {
        subTaskService.updateSubTaskStatus(subTaskId, status);
        return "redirect:/tasks";
    }

    @PostMapping("/{taskId}/addSubTask")
    public String addSubTask(@PathVariable Long taskId, @ModelAttribute("subTask") SubTask subTask, BindingResult result) {
        if (result.hasErrors()) {
            return "taskDetail"; // Предполагается наличие страницы taskDetail
        }

        subTaskService.addSubTask(taskId, subTask);
        return "redirect:/tasks/" + taskId;
    }

    // Обновление статуса подзадачи
    @PostMapping("/subtasks/updateStatus/{subTaskId}")
    public String updateSubTaskStatus(@PathVariable Long subTaskId, @RequestParam("status") TaskStatus status) {
        subTaskService.updateSubTaskStatus(subTaskId, status);
        return "redirect:/tasks"; // Укажите соответствующий URL для перенаправления
    }
}