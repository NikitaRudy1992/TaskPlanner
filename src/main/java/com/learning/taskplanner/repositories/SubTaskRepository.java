package com.learning.taskplanner.repositories;

import com.learning.taskplanner.model.SubTask;
import com.learning.taskplanner.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    List<SubTask> findByTask(Task task);
}