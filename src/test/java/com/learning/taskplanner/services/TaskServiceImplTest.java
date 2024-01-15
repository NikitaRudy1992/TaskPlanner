package com.learning.taskplanner.services;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.*;
import com.learning.taskplanner.model.enums.TaskPriority;
import com.learning.taskplanner.model.enums.TaskStatus;
import com.learning.taskplanner.repositories.TaskRepository;
import com.learning.taskplanner.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private TaskService taskService;

    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskServiceImpl(taskRepository, userRepository, emailService);
    }

    @Test
    public void testGetTasksByUser() {
        User user = new User();
        when(taskRepository.findByUser(user)).thenReturn(new ArrayList<>());

        List<Task> tasks = taskService.getTasksByUser(user);

        assertNotNull(tasks);
    }

    @Test
    public void testCreateTask() {
        User user = new User();
        Task task = new Task();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        taskService.createTask(task, 1L);

        assertNotNull(task.getUser());
        assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    public void testUpdateTaskStatusCompleted() {
        Task task = new Task();
        task.setStatus(TaskStatus.IN_PROGRESS);

        SubTask subTask = new SubTask();
        subTask.setCompleted(true);

        List<SubTask> subTasks = new ArrayList<>();
        subTasks.add(subTask);

        task.setSubTasks(subTasks);

        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        assertDoesNotThrow(() -> taskService.updateTaskStatus(1L, TaskStatus.COMPLETED));
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    public void testUpdateTaskStatusCancelled() {
        Task task = new Task();
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        taskService.updateTaskStatus(1L, TaskStatus.CANCELLED);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    public void testSearchTasks() {
        User user = new User();
        when(taskRepository.findAll(any(Specification.class))).thenReturn(new ArrayList<>());

        List<Task> tasks = taskService.searchTasks("Title", TaskStatus.NEW, TaskPriority.HIGH, LocalDate.now(), user);

        assertNotNull(tasks);
    }

    @Test
    public void testAreAllSubTasksCompleted() {
        Task task = new Task();
        List<SubTask> subTasks = new ArrayList<>();

        SubTask subTask1 = new SubTask();
        subTask1.setCompleted(true);

        SubTask subTask2 = new SubTask();
        subTask2.setCompleted(true);

        subTasks.add(subTask1);
        subTasks.add(subTask2);

        task.setSubTasks(subTasks);

        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        boolean result = taskService.areAllSubTasksCompleted(1L);

        assertTrue(result);
    }

    @Test
    public void testGetCompletedTasks() {
        User user = new User();
        when(taskRepository.findByUserAndStatus(user, TaskStatus.COMPLETED)).thenReturn(new ArrayList<>());

        List<Task> tasks = taskService.getCompletedTasks(user);

        assertNotNull(tasks);
    }

    @Test
    public void testGetIncompleteTasks() {
        User user = new User();
        when(taskRepository.findByUserAndStatusNot(user, TaskStatus.COMPLETED)).thenReturn(new ArrayList<>());

        List<Task> tasks = taskService.getIncompleteTasks(user);

        assertNotNull(tasks);
    }

    @Test
    public void testFindById() {
        Task task = new Task();
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        Task foundTask = taskService.findById(1L);

        assertNotNull(foundTask);
    }

    @Test
    public void testGetTasksByUserExcludeCompleted() {
        User user = new User();
        when(taskRepository.findByUserAndStatusNot(user, TaskStatus.COMPLETED)).thenReturn(new ArrayList<>());

        List<Task> tasks = taskService.getTasksByUserExcludeCompleted(user);

        assertNotNull(tasks);
    }

    @Test
    public void testGetTasksWithUpcomingDeadline() {
        User user = new User();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        when(taskRepository.findByUserAndDeadline(user, tomorrow)).thenReturn(new ArrayList<>());

        List<Task> tasks = taskService.getTasksWithUpcomingDeadline(user);

        assertNotNull(tasks);
    }

    @Test
    public void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        List<Task> tasks = taskService.getAllTasks();

        assertNotNull(tasks);
    }

    @Test
    public void testDeleteTask() {
        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}
