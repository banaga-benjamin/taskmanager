package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import org.springframework.http.MediaType;
import com.example.taskmanager.dto.AuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.dto.UpdatePasswordRequest;
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
public class AuthenticationControllerTest {

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
    void updatePassword_ShouldChangePassword( ) throws Exception {
        UpdatePasswordRequest passwordRequest = new UpdatePasswordRequest( );
        passwordRequest.setOldPassword("password");
        passwordRequest.setNewPassword("newpassword");
        String passwordJson = mapper.writeValueAsString(passwordRequest);

        mock.perform(put("/api/auth/update-password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(passwordJson))
                .andExpect(status( ).isOk( ));

        AuthRequest loginRequest = new AuthRequest( );
        loginRequest.setPassword("newpassword");
        loginRequest.setUsername("testuser");
        String loginJson = mapper.writeValueAsString(loginRequest);

        mock.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status( ).isOk( ))
                .andExpect(jsonPath("$.token").exists( ));
    }

}
