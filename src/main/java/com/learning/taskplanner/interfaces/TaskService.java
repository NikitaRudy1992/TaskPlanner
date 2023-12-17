package com.learning.taskplanner.interfaces;

import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    List<Task> getTasksByUser(User user);
    void createTask(Task task, Long userId);
    void updateTaskStatus(Long taskId, TaskStatus newStatus);
}
