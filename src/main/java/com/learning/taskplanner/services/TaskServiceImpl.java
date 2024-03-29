package com.learning.taskplanner.services;

import com.learning.taskplanner.interfaces.TaskService;
import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskPriority;
import com.learning.taskplanner.model.enums.TaskStatus;
import com.learning.taskplanner.repositories.TaskRepository;
import com.learning.taskplanner.repositories.UserRepository;
import com.learning.taskplanner.specifications.TaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    @Override
    public void createTask(Task task, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
        task.setStatus(TaskStatus.NEW);
        taskRepository.save(task);
    }
    @Override
    public void updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (newStatus == TaskStatus.COMPLETED && !areAllSubTasksCompleted(taskId)) {
            throw new RuntimeException("Subtasks not completed");
        }
        if (newStatus == TaskStatus.CANCELLED) {
            taskRepository.delete(task);
        } else {
            task.setStatus(newStatus);
            taskRepository.save(task);
        }
    }

    @Override
    public List<Task> searchTasks(String title, TaskStatus status, TaskPriority priority, LocalDate deadline, User user) {
        Specification<Task> spec = TaskSpecification.findByCriteria(title, status, priority, deadline, user);
        return taskRepository.findAll(spec);
    }

    @Override
    public boolean areAllSubTasksCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return task.getSubTasks().stream().allMatch(SubTask::isCompleted);
    }
    @Override
    public List<Task> getCompletedTasks(User user) {
        return taskRepository.findByUserAndStatus(user, TaskStatus.COMPLETED);
    }

    @Override
    public List<Task> getIncompleteTasks(User user) {
        return taskRepository.findByUserAndStatusNot(user, TaskStatus.COMPLETED);
    }
    @Override
    public Task findById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
    @Override
    public List<Task> getTasksByUserExcludeCompleted(User user) {
        return taskRepository.findByUserAndStatusNot(user, TaskStatus.COMPLETED);
    }

    @Override
    public List<Task> getTasksWithUpcomingDeadline(User user) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Task> tasks = taskRepository.findByUserAndDeadline(user, tomorrow);

        if (!tasks.isEmpty()) {
            sendDeadlineNotification(user, tasks);
        }

        return tasks;
    }
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
    private void sendDeadlineNotification(User user, List<Task> tasks) {
        String email = user.getEmail();
        String subject = "Уведомление о дедлайне задач";
        StringBuilder messageBuilder = new StringBuilder("Уважаемый(ая) ")
                .append(user.getUsername())
                .append(", у вас есть следующие задачи с дедлайном на завтра: \n");

        for (Task task : tasks) {
            messageBuilder.append("- ").append(task.getTitle()).append(" (дедлайн: ")
                    .append(task.getDeadline().toString()).append(")\n");
        }

        emailService.sendEmail(email, subject, messageBuilder.toString());
    }
}