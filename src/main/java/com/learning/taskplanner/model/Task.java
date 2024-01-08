package com.learning.taskplanner.model;

import com.learning.taskplanner.model.enums.TaskPriority;
import com.learning.taskplanner.model.enums.TaskStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//требуется валидация
@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @NotBlank(message = "Title cannot be empty")
    @Column(name = "title")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Deadline cannot be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "deadline")
    private LocalDate deadline;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubTask> subTasks = new ArrayList<>();
}
