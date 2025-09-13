package com.example.taskmanager.dto;

import jakarta.validation.constraints.*;

public class CreateTaskRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must be at most 1000 characters long")
    private String description;

    public CreateTaskRequest(String title) {
        this.title = title;
    }

    public String getTitle( ) { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription( ) { return this.description; }
    public void setDescription(String description) { this.description = description; }
}
