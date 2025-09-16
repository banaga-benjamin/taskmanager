package com.example.taskmanager.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    public Task( ) { }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    @PrePersist
    protected void onCreate( ) {
        this.createdAt = LocalDateTime.now( );
        this.updatedAt = LocalDateTime.now( );
    }

    @PreUpdate
    protected void onUpdate( ) {
        this.updatedAt = LocalDateTime.now( );
    }

    public Long getId( ) { return id; }

    public String getTitle( ) { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription( ) { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted( ) { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public LocalDateTime getCreatedAt( ) { return createdAt; }
    public LocalDateTime getUpdatedAt( ) { return updatedAt; }
}
