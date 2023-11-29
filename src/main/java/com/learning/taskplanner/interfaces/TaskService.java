package com.learning.taskplanner.interfaces;

import com.learning.taskplanner.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    void createTask(Task task);
}
