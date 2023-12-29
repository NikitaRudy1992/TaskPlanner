package com.learning.taskplanner.services;

import com.learning.taskplanner.interfaces.SubTaskService;
import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.enums.TaskStatus;
import com.learning.taskplanner.repositories.SubTaskRepository;
import com.learning.taskplanner.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SubTaskServiceImpl implements SubTaskService {

    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;

    @Override
    public void addSubTask(Long taskId, SubTask subTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        subTask.setTask(task);
        subTask.setStatus(TaskStatus.NEW);
        subTaskRepository.save(subTask);
    }

    @Override
    public void updateSubTaskStatus(Long subTaskId, TaskStatus newStatus) {
        SubTask subTask = subTaskRepository.findById(subTaskId)
                .orElseThrow(() -> new RuntimeException("SubTask not found"));//подумать над исключением
        subTask.setStatus(newStatus);
        if (newStatus == TaskStatus.COMPLETED) {
            subTask.setCompleted(true);
        } //продумать или удаление после выполнения из списка или динамическое изменение completed boolean

        subTaskRepository.save(subTask);
    }
    @Override
    public List<SubTask> getSubTasksByTask(Task task) {
        return subTaskRepository.findByTask(task);
    }
    @Override
    public SubTask findById(Long subTaskId) {
        return subTaskRepository.findById(subTaskId)
                .orElseThrow(() -> new RuntimeException("SubTask not found"));
    }
}