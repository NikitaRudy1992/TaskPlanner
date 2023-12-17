package com.learning.taskplanner.services;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskStatus;
import com.learning.taskplanner.repositories.TaskRepository;
import com.learning.taskplanner.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Override
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    @Override
    public void createTask(Task task, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        task.setStatus(TaskStatus.NEW);
        taskRepository.save(task);
    }
    @Override
    public void updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(newStatus);
        taskRepository.save(task);
    }
}