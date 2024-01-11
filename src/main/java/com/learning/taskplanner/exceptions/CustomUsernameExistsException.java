package com.learning.taskplanner.exceptions;

public class CustomUsernameExistsException extends RuntimeException {
    public CustomUsernameExistsException(String message) {
        super(message);
    }
}
