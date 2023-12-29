package com.learning.taskplanner.interfaces;

import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.enums.TaskStatus;

import java.util.List;

public interface SubTaskService {
    void addSubTask(Long taskId, SubTask subTask);
    void updateSubTaskStatus(Long subTaskId, TaskStatus newStatus);
    List<SubTask> getSubTasksByTask(Task task);
    SubTask findById(Long subTaskId);
}
