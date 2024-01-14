package com.learning.taskplanner.services;

import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.enums.TaskStatus;
import com.learning.taskplanner.repositories.SubTaskRepository;
import com.learning.taskplanner.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SubTaskServiceImplTest {

    @InjectMocks
    private SubTaskServiceImpl subTaskService;

    @Mock
    private SubTaskRepository subTaskRepository;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSubTask() {
        Task task = new Task();
        SubTask subTask = new SubTask();
        subTask.setTask(task);

        when(taskRepository.findById(any())).thenReturn(Optional.of(task));
        when(subTaskRepository.save(any())).thenReturn(subTask);

        subTaskService.addSubTask(1L, subTask);

        assertNotNull(subTask.getTask());
        assertEquals(TaskStatus.NEW, subTask.getStatus());
    }

    @Test
    public void testUpdateSubTaskStatus() {
        SubTask subTask = new SubTask();
        subTask.setStatus(TaskStatus.IN_PROGRESS);

        when(subTaskRepository.findById(any())).thenReturn(Optional.of(subTask));
        when(subTaskRepository.save(any())).thenReturn(subTask);

        subTaskService.updateSubTaskStatus(1L, TaskStatus.COMPLETED);

        assertEquals(TaskStatus.COMPLETED, subTask.getStatus());
        assertTrue(subTask.isCompleted());
    }

    @Test
    public void testGetSubTasksByTask() {
        Task task = new Task();
        SubTask subTask1 = new SubTask();
        SubTask subTask2 = new SubTask();
        subTask1.setTask(task);
        subTask2.setTask(task);

        List<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);

        when(subTaskRepository.findByTask(task)).thenReturn(subTasks);

        List<SubTask> result = subTaskService.getSubTasksByTask(task);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(task, result.get(0).getTask());
        assertEquals(task, result.get(1).getTask());
    }

    @Test
    public void testFindById() {
        SubTask subTask = new SubTask();
        when(subTaskRepository.findById(1L)).thenReturn(Optional.of(subTask));

        SubTask result = subTaskService.findById(1L);

        assertNotNull(result);
    }
}
