package com.learning.taskplanner.controllers;

import com.learning.taskplanner.controllers.TaskController;
import com.learning.taskplanner.interfaces.SubTaskService;
import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private SubTaskService subTaskService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private User currentUser;

    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskController = new TaskController(taskService, subTaskService);
    }

    @Test
    public void testShowTasks() {
        // Arrange
        List<Task> tasksWithUpcomingDeadline = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        List<Long> tasksWithIncompleteSubTasks = new ArrayList<>();

        when(taskService.getTasksWithUpcomingDeadline(currentUser)).thenReturn(tasksWithUpcomingDeadline);
        when(taskService.getTasksByUserExcludeCompleted(currentUser)).thenReturn(tasks);
        when(taskService.areAllSubTasksCompleted(anyLong())).thenReturn(false);

        // Act
        String viewName = taskController.showTasks(model, currentUser);

        // Assert
        assertEquals("tasklist", viewName);
        verify(model, times(4)).addAttribute(anyString(), any());
    }

    @Test
    public void testShowTaskDetails() {
        // Arrange
        Long taskId = 1L;
        Task task = new Task();
        List<SubTask> subTasks = new ArrayList<>();

        when(taskService.findById(taskId)).thenReturn(task);
        when(subTaskService.getSubTasksByTask(task)).thenReturn(subTasks);

        // Act
        String viewName = taskController.showTaskDetails(taskId, model);

        // Assert
        assertEquals("taskDetail", viewName);
        verify(model, times(2)).addAttribute(anyString(), any());
    }

    @Test
    public void testCreateTask() {
        // Arrange
        Task newTask = new Task();

        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = taskController.createTask(newTask, bindingResult, currentUser);

        // Assert
        assertEquals("redirect:/tasks", viewName);
        verify(taskService, times(1)).createTask(newTask, currentUser.getUserId());
    }

    @Test
    public void testUpdateTaskStatus() {
        // Arrange
        Long taskId = 1L;
        TaskStatus status = TaskStatus.COMPLETED;

        // Act
        String viewName = taskController.updateTaskStatus(taskId, status, redirectAttributes);

        // Assert
        assertEquals("redirect:/tasks", viewName);
        verify(taskService, times(1)).updateTaskStatus(taskId, status);
    }

    @Test
    public void testSearchTasks() {
        // Arrange
        String title = "Test";
        TaskStatus status = TaskStatus.IN_PROGRESS;
        LocalDate deadline = LocalDate.now();
        List<Task> searchResults = new ArrayList<>();
        List<Task> tasksWithUpcomingDeadline = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        when(taskService.searchTasks(title, status, null, deadline, currentUser)).thenReturn(searchResults);
        when(taskService.getTasksWithUpcomingDeadline(currentUser)).thenReturn(tasksWithUpcomingDeadline);
        when(taskService.getTasksByUserExcludeCompleted(currentUser)).thenReturn(tasks);

        // Act
        String viewName = taskController.searchTasks(title, status, null, deadline, currentUser, model);

        // Assert
        assertEquals("tasklist", viewName);
        verify(model, times(4)).addAttribute(anyString(), any());
    }

    @Test
    public void testAddSubTask() {
        // Arrange
        Long taskId = 1L;
        SubTask subTask = new SubTask();

        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = taskController.addSubTask(taskId, subTask, bindingResult);

        // Assert
        assertEquals("redirect:/tasks/" + taskId, viewName);
        verify(subTaskService, times(1)).addSubTask(taskId, subTask);
    }

    @Test
    public void testUpdateSubTaskStatus() {
        // Arrange
        Long subTaskId = 1L;
        TaskStatus status = TaskStatus.COMPLETED;
        SubTask subTask = new SubTask();
        Task task = new Task();
        task.setTaskId(2L);
        subTask.setTask(task);

        when(subTaskService.findById(subTaskId)).thenReturn(subTask);

        // Act
        String viewName = taskController.updateSubTaskStatus(subTaskId, status);

        // Assert
        assertEquals("redirect:/tasks/2", viewName);
        verify(subTaskService, times(1)).updateSubTaskStatus(subTaskId, status);
    }

    @Test
    public void testShowTaskProgress() {
        // Arrange
        List<Task> completedTasks = new ArrayList<>();
        List<Task> incompleteTasks = new ArrayList<>();

        when(taskService.getCompletedTasks(currentUser)).thenReturn(completedTasks);
        when(taskService.getIncompleteTasks(currentUser)).thenReturn(incompleteTasks);

        // Act
        String viewName = taskController.showTaskProgress(model, currentUser);

        // Assert
        assertEquals("taskprogress", viewName);
        verify(model, times(2)).addAttribute(anyString(), any());
    }
}