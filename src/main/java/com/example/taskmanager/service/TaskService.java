package com.example.taskmanager.service;

import java.util.List;
import com.example.taskmanager.dto.*;

public interface TaskService {
    TaskDTO getTaskByID(Long id);
    List<TaskDTO> getAllTasks( );

    TaskDTO createTask(CreateTaskRequest request);
    TaskDTO updateTask(Long id, UpdateTaskRequest request);
    void deleteTask(Long id);
}
