package com.learning.taskplanner.specifications;

import com.learning.taskplanner.model.Task;
import com.learning.taskplanner.model.User;
import com.learning.taskplanner.model.enums.TaskPriority;
import com.learning.taskplanner.model.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> findByCriteria(String title, TaskStatus status, TaskPriority priority, LocalDate deadline, User user) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (priority != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), priority));
            }
            if (deadline != null) {
                predicates.add(criteriaBuilder.equal(root.get("deadline"), deadline));
            }
            if (user != null) {
                predicates.add(criteriaBuilder.equal(root.get("user"), user));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}