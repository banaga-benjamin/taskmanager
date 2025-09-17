package com.example.taskmanager.model;


import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable =  false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Task> tasks = new HashSet< >( );

    public User( ) { }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getID( ) { return this.id; }

    public String getUsername( ) { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword( ) { return this.password; }
    public void setPassword(String password) { this.password = password; }

    public Set<Task> getTasks( ) { return this.tasks; }
}
