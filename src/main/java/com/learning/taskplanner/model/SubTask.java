package com.learning.taskplanner.model;

import com.learning.taskplanner.model.enums.TaskStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "subtasks")
@Data
public class SubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtask_id")
    private Long subtaskId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "completed")
    private boolean completed = false;
}
