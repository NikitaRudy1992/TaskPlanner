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
        List<Task> tasksWithUpcomingDeadline = taskService.getTasksWithUpcomingDeadline(currentUser);
        model.addAttribute("tasksWithUpcomingDeadline", tasksWithUpcomingDeadline);
        List<Task> tasks = taskService.getTasksByUserExcludeCompleted(currentUser);
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new Task());
        return "tasklist";
    }

    @GetMapping("/{taskId}")
    public String showTaskDetails(@PathVariable Long taskId, Model model) {
        Task task = taskService.findById(taskId);
        List<SubTask> subTasks = subTaskService.getSubTasksByTask(task);
        model.addAttribute("task", task);
        model.addAttribute("subTasks", subTasks);
        return "taskDetail";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute("newTask") Task newTask, BindingResult result, @AuthenticationPrincipal User currentUser) {
        if (result.hasErrors()) {
            return "tasklist";
        }
        taskService.createTask(newTask, currentUser.getUserId());
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

    @PostMapping("/{taskId}/addSubTask")
    public String addSubTask(@PathVariable Long taskId, @ModelAttribute("subTask") SubTask subTask, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/tasks/" + taskId;
        }
        subTaskService.addSubTask(taskId, subTask);
        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/subtasks/updateStatus/{subTaskId}")
    public String updateSubTaskStatus(@PathVariable Long subTaskId, @RequestParam("status") TaskStatus status) {
        SubTask subTask = subTaskService.findById(subTaskId);
        subTaskService.updateSubTaskStatus(subTaskId, status);
        Long taskId = subTask.getTask().getTaskId();
        return "redirect:/tasks/" + taskId;
    }
    @GetMapping("/progress")
    public String showTaskProgress(Model model, @AuthenticationPrincipal User currentUser) {
        List<Task> completedTasks = taskService.getCompletedTasks(currentUser);
        List<Task> incompleteTasks = taskService.getIncompleteTasks(currentUser);

        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("incompleteTasks", incompleteTasks);

        return "taskprogress";
    }
}