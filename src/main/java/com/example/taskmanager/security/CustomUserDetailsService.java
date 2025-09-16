package com.example.taskmanager.security;

import com.example.taskmanager.model.User;
import org.springframework.stereotype.Service;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.taskmanager.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repo.findByUsername(username).orElseThrow(( ) -> new ResourceNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword( ))
                .authorities("USER")
                .build( );
    }
}
