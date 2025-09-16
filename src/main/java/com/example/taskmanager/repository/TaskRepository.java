package com.example.taskmanager.repository;

import java.util.List;
import java.util.Optional;
import com.example.taskmanager.model.User;
import com.example.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // using only basic CRUD methods for now
    List<Task> findByUser(User user);
    Optional<Task> findByUserAndId(User user, Long id);
}
