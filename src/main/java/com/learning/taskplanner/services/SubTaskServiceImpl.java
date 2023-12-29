package com.learning.taskplanner.services;

import com.learning.taskplanner.interfaces.SubTaskService;
import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.enums.TaskStatus;
import com.learning.taskplanner.repositories.SubTaskRepository;
import com.learning.taskplanner.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubTaskServiceImpl implements SubTaskService {

    private final SubTaskRepository subTaskRepository;

    @Override
    public void updateSubTaskStatus(Long subTaskId, TaskStatus newStatus) {
        SubTask subTask = subTaskRepository.findById(subTaskId)
                .orElseThrow(() -> new RuntimeException("SubTask not found"));
        subTask.setStatus(newStatus);
        subTaskRepository.save(subTask);
    }
}
