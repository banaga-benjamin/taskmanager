package com.example.taskmanager.controller;

import com.example.taskmanager.dto.*;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private TaskRepository repo;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setup( ) {
        repo.deleteAll( );
    }

    @Test
    void createTask_ShouldReturn201AndTask( ) throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Test task");
        request.setDescription("Description");

        mock.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
            .andExpect(status( ).isCreated( ))
            .andExpect(jsonPath("$.id").exists( ))
            .andExpect(jsonPath("$.title").value("Test task"))
            .andExpect(jsonPath("$.description").value("Description"))
            .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void getAllTasks_ShouldReturn200AndTasks( ) throws Exception {
        repo.save(new Task("Task A", "First task"));

        mock.perform(get("/api/tasks"))
            .andExpect(status( ).isOk( ))
            .andExpect(jsonPath("$[0].title").value("Task A"));
    }

    @Test
    void getTaskById_ShouldReturn200AndTask( ) throws Exception {
        Task task = repo.save(new Task("Task B", "Second task"));

        mock.perform(get("/api/tasks/{id}", task.getId( )))
            .andExpect(status( ).isOk( ))
            .andExpect(jsonPath("$.title").value("Task B"));
    }

    @Test
    void updateTask_ShouldReturn200AndUpdatedTask( ) throws Exception {
        Task task = repo.save(new Task("Old Title", "Old Description"));

        String updateJson = """
            { "title": "New Title", "description": "New Description", "completed": true }
            """;

        mock.perform(put("/api/tasks/{id}", task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
            .andExpect(status( ).isOk( ))
            .andExpect(jsonPath("$.title").value("New Title"))
            .andExpect(jsonPath("$.description").value("New Description"))
            .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void deleteTask_ShouldReturn204( ) throws Exception {
        Task task = repo.save(new Task("Task C", "To delete"));

        mock.perform(delete("/api/tasks/{id}", task.getId( )))
            .andExpect(status( ).isNoContent( ));
    }
}
