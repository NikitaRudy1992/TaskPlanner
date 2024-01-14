package com.learning.taskplanner.controllers;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.interfaces.UserService;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void adminDashboardTest() {
        Task mockTask1 = new Task();
        Task mockTask2 = new Task();
        List<Task> mockTasks = Arrays.asList(mockTask1, mockTask2);

        User mockUser1 = new User();
        User mockUser2 = new User();
        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);

        when(taskService.getAllTasks()).thenReturn(mockTasks);
        when(userService.getAllUsers()).thenReturn(mockUsers);

        String view = adminController.adminDashboard(model);

        assertEquals("adminDashboard", view);

        verify(model).addAttribute("tasks", mockTasks);
        verify(model).addAttribute("users", mockUsers);
    }
}