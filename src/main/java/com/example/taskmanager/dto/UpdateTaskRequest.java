package com.example.taskmanager.dto;

import jakarta.validation.constraints.*;

public class UpdateTaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private boolean completed;

    public UpdateTaskRequest(String title) {
        this.title = title;
    }

    public String getTitle( ) { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription( ) { return this.description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean isCompleted( ) { return this.completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
