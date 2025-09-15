package com.example.taskmanager.service;

import java.util.List;
import com.example.taskmanager.dto.*;
import com.example.taskmanager.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService service;

    @Autowired
    private TaskRepository repo;

    // clear database
    @BeforeEach
    void setup( ) {
        repo.deleteAll( );
    }

    // --- CREATE ---
    @Test
    void createTask_ShouldSaveAndReturnTaskDTO( ) {
        CreateTaskRequest request = new CreateTaskRequest("Integration Test Task");
        request.setDescription("Testing with Postgres");
        TaskDTO result = service.createTask(request);

        assertNotNull(result.getId( ));
        assertFalse(result.isCompleted( ));
        assertEquals("Integration Test Task", result.getTitle( ));
        assertEquals("Testing with Postgres", result.getDescription( ));

        assertTrue(repo.findById(result.getId( )).isPresent( ));
    }

    // --- READ ---
    @Test
    void getTaskById_ShouldReturnTaskDTO( ) {
        CreateTaskRequest request = new CreateTaskRequest("Read Test Task");
        request.setDescription("Read operation");
        TaskDTO created = service.createTask(request);

        // then fetch
        TaskDTO fetched = service.getTaskByID(created.getId( ));

        assertNotNull(fetched);
        assertEquals("Read Test Task", fetched.getTitle( ));
        assertEquals("Read operation", fetched.getDescription( ));
    }

    // --- READ ALL ---
    @Test
    void getAllTasks_ShouldReturnListOfTaskDTOs( ) {
        CreateTaskRequest req1 = new CreateTaskRequest("Task 1");
        req1.setDescription("First");
        service.createTask(req1);

        CreateTaskRequest req2 = new CreateTaskRequest("Task 2");
        req2.setDescription("Second");
        service.createTask(req2);

        List<TaskDTO> tasks = service.getAllTasks( );

        assertEquals(2, tasks.size( ));
        assertTrue(tasks.stream( ).anyMatch(t -> t.getTitle( ).equals("Task 1")));
        assertTrue(tasks.stream( ).anyMatch(t -> t.getTitle( ).equals("Task 2")));
    }

    // --- UPDATE ---
    @Test
    void updateTask_ShouldModifyAndReturnTaskDTO( ) {
        CreateTaskRequest request = new CreateTaskRequest("Original Title");
        request.setDescription("Original Description");
        TaskDTO created = service.createTask(request);

        UpdateTaskRequest updateReq = new UpdateTaskRequest("Updated Title");
        updateReq.setDescription("Updated Description");
        updateReq.setCompleted(true);

        TaskDTO updated = service.updateTask(created.getId( ), updateReq);

        assertNotNull(updated);
        assertTrue(updated.isCompleted( ));
        assertEquals("Updated Title", updated.getTitle( ));
        assertEquals("Updated Description", updated.getDescription( ));
    }

    // --- DELETE ---
    @Test
    void deleteTask_ShouldRemoveTask( ) {
        CreateTaskRequest request = new CreateTaskRequest("Delete Me");
        request.setDescription("Delete operation");
        TaskDTO created = service.createTask(request);

        service.deleteTask(created.getId( ));

        assertFalse(repo.findById(created.getId( )).isPresent( ));
    }
}
