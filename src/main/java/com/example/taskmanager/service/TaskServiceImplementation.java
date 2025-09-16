package com.example.taskmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.taskmanager.dto.*;
import com.example.taskmanager.model.*;
import org.springframework.stereotype.*;
import com.example.taskmanager.repository.*;
import com.example.taskmanager.exception.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class TaskServiceImplementation implements TaskService {

    private final TaskRepository repo;
    private final UserRepository repouser;

    public TaskServiceImplementation(TaskRepository repo, UserRepository repouser) {
        this.repo = repo;
        this.repouser = repouser;
    }

    private User getCurrentUser( ) {
        String username = SecurityContextHolder.getContext( ).getAuthentication( ).getName( );
        return repouser.findByUsername(username).orElseThrow(( ) -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public TaskDTO getTaskByID(Long id) {
        User user = getCurrentUser( );
        Task task = repo.findByUserAndId(user, id).orElseThrow(( ) -> new ResourceNotFoundException("Task not found with id: " + id));
        return mapToDTO(task);
    }

    @Override
    public List<TaskDTO> getAllTasks( ) {
        User user = getCurrentUser( );
        List<Task> tasks = repo.findByUser(user);
        return tasks.stream( ).map(this::mapToDTO).collect(Collectors.toList( ));
    }

    @Override
    public TaskDTO createTask(CreateTaskRequest request) {
        Task task = new Task( );
        task.setUser(getCurrentUser( ));
        task.setTitle(request.getTitle( ));
        task.setDescription(request.getDescription( ));

        Task created = repo.save(task);
        return mapToDTO(created);
    }

    @Override
    public TaskDTO updateTask(Long id, UpdateTaskRequest request) {
        User user = getCurrentUser( );
        Task task = repo.findByUserAndId(user, id).orElseThrow(( ) -> new ResourceNotFoundException("Task not found with id: " + id));
        if (request.getTitle( ) != null) task.setTitle(request.getTitle( ));
        if (request.getDescription( ) != null) task.setDescription(request.getDescription( ));
        if (request.isCompleted( ) != null) task.setCompleted(request.isCompleted( ));

        Task updated = repo.save(task);
        return mapToDTO(updated);
    }

    @Override
    public void deleteTask(Long id) {
        User user = getCurrentUser( );
        Task task = repo.findByUserAndId(user, id).orElseThrow(( ) -> new ResourceNotFoundException("Task not found with id: " + id));
        repo.delete(task);
    }

    // mapper utility
    private TaskDTO mapToDTO(Task task) {
        return new TaskDTO(
            task.getId( ),
            task.getTitle( ),
            task.getDescription( ),
            task.isCompleted( ),
            task.getCreatedAt( ),
            task.getUpdatedAt( )
        );
    }
}
