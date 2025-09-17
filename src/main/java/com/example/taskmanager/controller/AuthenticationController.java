package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import com.example.taskmanager.dto.AuthRequest;
import com.example.taskmanager.dto.AuthResponse;
import com.example.taskmanager.security.JwtUtil;
import com.example.taskmanager.dto.UpdatePasswordRequest;
import com.example.taskmanager.exception.BadRequestException;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.UserRepository;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private JwtUtil jwtutil;
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthenticationController(UserRepository repo, PasswordEncoder encoder, JwtUtil jwtutil) {
        this.repo = repo;
        this.jwtutil = jwtutil;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        if (repo.existsByUsername(request.getUsername( ))) {
            throw new BadRequestException("Username already taken");
        }

        repo.save(new User(request.getUsername( ), encoder.encode(request.getPassword( ))));
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = repo.findByUsername(request.getUsername( ))
            .orElseThrow(( ) -> new ResourceNotFoundException("User not found"));

        if (!encoder.matches(request.getPassword( ), user.getPassword( ))) {
            throw new ResourceNotFoundException("User not found.");
        }

        AuthResponse response = new AuthResponse( );
        String token = jwtutil.generateToken(user.getUsername( ));
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest passwordRequest) {
        String username = SecurityContextHolder.getContext( ).getAuthentication( ).getName( );
        User user = repo.findByUsername(username).orElseThrow(( ) -> new ResourceNotFoundException("User not found"));

        if (!encoder.matches(passwordRequest.getOldPassword( ), user.getPassword( ))) {
            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(encoder.encode(passwordRequest.getNewPassword( )));
        repo.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }
}
