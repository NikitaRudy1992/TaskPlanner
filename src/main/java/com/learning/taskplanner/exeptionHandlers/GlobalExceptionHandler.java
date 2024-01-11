package com.learning.taskplanner.exeptionHandlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        if ("All subtasks must be completed first".equals(ex.getMessage())) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            modelAndView.setViewName("redirect:/tasks");
        } else {
            modelAndView.addObject("errorMessage", "An error occurred: " + ex.getMessage());
            modelAndView.setViewName("redirect:/tasks");
        }

        return modelAndView;
    }
}