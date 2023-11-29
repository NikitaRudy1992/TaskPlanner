package com.learning.taskplanner.services;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public void createTask(Task task) {
        taskRepository.save(task);
    }
}
