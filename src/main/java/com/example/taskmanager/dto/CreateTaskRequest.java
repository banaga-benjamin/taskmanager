package com.example.taskmanager.dto;

import jakarta.validation.constraints.*;

public class CreateTaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    public CreateTaskRequest(String title) {
        this.title = title;
    }

    public String getTitle( ) { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription( ) { return this.description; }
    public void setDescription(String description) { this.description = description; }
}
