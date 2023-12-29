package com.learning.taskplanner.interfaces;

import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.enums.TaskStatus;

public interface SubTaskService {
    void addSubTask(Long taskId, SubTask subTask);
    void updateSubTaskStatus(Long subTaskId, TaskStatus newStatus);
}
