package com.learning.taskplanner.model;

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

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
