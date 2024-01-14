package com.learning.taskplanner.controllers;

import com.learning.taskplanner.exceptions.CustomUsernameExistsException;
import com.learning.taskplanner.interfaces.UserService;
import com.learning.taskplanner.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showRegistrationFormTest() {
        String view = authController.showRegistrationForm(model);
        verify(model).addAttribute(eq("user"), any(User.class));
        assertEquals("register", view);
    }

    @Test
    void registerUserWithErrorsTest() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = authController.registerUser(new User(), bindingResult, model);

        assertEquals("register", view);
    }

    @Test
    void registerUserSuccessTest() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = authController.registerUser(new User(), bindingResult, model);

        assertEquals("redirect:/login?registrationSuccess", view);
    }

    @Test
    void registerUserUsernameExistsTest() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new CustomUsernameExistsException("Username already exists."))
                .when(userService).registerNewUser(any(User.class));

        String view = authController.registerUser(new User(), bindingResult, model);

        verify(model).addAttribute("registrationError", "Username already exists.");
        assertEquals("register", view);
    }

    @Test
    void showLoginFormTest() {
        String view = authController.showLoginForm();
        assertEquals("login", view);
    }
}
