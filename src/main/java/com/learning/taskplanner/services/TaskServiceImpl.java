package com.learning.taskplanner.services;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskStatus;
import com.learning.taskplanner.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    @Override
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    @Override
    public void createTask(Task task) {
        task.setStatus(TaskStatus.NEW);
        taskRepository.save(task);
    }
}