package com.learning.taskplanner.interfaces;

import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskPriority;
import com.learning.taskplanner.model.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    List<Task> getTasksByUser(User user);
    void createTask(Task task, Long userId);
    void updateTaskStatus(Long taskId, TaskStatus newStatus);
    List<Task> searchTasks(String title, TaskStatus status, TaskPriority priority, LocalDate deadline, User user);
    boolean areAllSubTasksCompleted(Long taskId);
    List<Task> getCompletedTasks(User user);
    List<Task> getIncompleteTasks(User user);
    Task findById(Long taskId);
    List<Task> getTasksByUserExcludeCompleted(User user);
    List<Task> getTasksWithUpcomingDeadline(User user);
    List<Task> getAllTasks();
    void deleteTask(Long taskId);
}
