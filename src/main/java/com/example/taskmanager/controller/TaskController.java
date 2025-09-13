package com.example.taskmanager.controller;

import java.net.URI;
import java.util.List;
import com.example.taskmanager.dto.*;
import com.example.taskmanager.service.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTaskByID(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks( ) {
        return ResponseEntity.ok(service.getAllTasks( ));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskRequest request) {
        TaskDTO task = service.createTask(request);
        URI location = URI.create(String.format("/api/tasks/%d", task.getId( )));
        return ResponseEntity.created(location).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(service.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent( ).build( );
    }
}
