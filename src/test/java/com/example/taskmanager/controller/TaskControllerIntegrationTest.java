package com.example.taskmanager.controller;

import org.springframework.http.MediaType;
import com.example.taskmanager.model.User;
import com.example.taskmanager.dto.AuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private UserRepository repo;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder encoder;

    private String token;

    @BeforeEach
    void setup( ) throws Exception {
        repo.deleteAll( );

        User user = new User("testuser", encoder.encode("password"));
        repo.save(user);

        AuthRequest loginRequest = new AuthRequest( );
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        String loginJson = mapper.writeValueAsString(loginRequest);

        String response = mock.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status( ).isOk( ))
                .andReturn( )
                .getResponse( )
                .getContentAsString( );

        token = mapper.readTree(response).get("token").asText( );
    }

    @Test
    void createTask_ShouldReturn201AndTask( ) throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Test Task");
        request.setDescription("Description");

        String taskJson = mapper.writeValueAsString(request);

        mock.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status( ).isCreated( ))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void updateTask_ShouldReturn200AndUpdatedTask( ) throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Test Task");
        request.setDescription("Description");

        String taskJson = mapper.writeValueAsString(request);

        String response = mock.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andReturn( )
                .getResponse( )
                .getContentAsString( );

        Long id = mapper.readTree(response).get("id").asLong( );

        UpdateTaskRequest updateRequest = new UpdateTaskRequest("New Title");
        updateRequest.setDescription("New Description");
        updateRequest.setCompleted(true);
        String updateJson = mapper.writeValueAsString(updateRequest);

        mock.perform(put("/api/tasks/{id}", id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
            .andExpect(status( ).isOk( ))
            .andExpect(jsonPath("$.title").value("New Title"))
            .andExpect(jsonPath("$.description").value("New Description"))
            .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void getAllTasks_ShouldReturn200AndTasks( ) throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Test Task");
        request.setDescription("Description");

        String taskJson = mapper.writeValueAsString(request);

        mock.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson));

        mock.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + token))
            .andExpect(status( ).isOk( ))
            .andExpect(jsonPath("$[0].title").value("Test Task"))
            .andExpect(jsonPath("$[0].description").value("Description"));
    }

    @Test
    void getTaskById_ShouldReturn200AndTask( ) throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Test Task");
        request.setDescription("Description");

        String taskJson = mapper.writeValueAsString(request);

        String response = mock.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andReturn( )
                .getResponse( )
                .getContentAsString( );

        Long id = mapper.readTree(response).get("id").asLong( );

        mock.perform(get("/api/tasks/{id}", id)
                        .header("Authorization", "Bearer " + token))
            .andExpect(status( ).isOk( ))
            .andExpect(jsonPath("$.title").value("Test Task"))
            .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void getNonExistentTask_ShouldReturn404( ) throws Exception {
        mock.perform(get("/api/tasks/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status( ).isNotFound( ))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Task not found with id: 999"));
    }

     @Test
    void deleteTask_ShouldReturn204( ) throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Test Task");
        request.setDescription("Description");

        String taskJson = mapper.writeValueAsString(request);

        String response = mock.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andReturn( )
                .getResponse( )
                .getContentAsString( );

        Long id = mapper.readTree(response).get("id").asLong( );

        mock.perform(delete("/api/tasks/{id}", id)
                        .header("Authorization", "Bearer " + token))
            .andExpect(status( ).isNoContent( ));
    }

}
