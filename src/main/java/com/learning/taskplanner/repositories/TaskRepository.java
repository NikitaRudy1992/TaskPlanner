package com.learning.taskplanner.repositories;

import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task>{
    List<Task> findByUser(User user);
}
