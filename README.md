# Task Manager (Spring Boot + PostgreSQL)

A simple Task Manager REST API built with **Spring Boot**, **Spring Data JPA**, and **PostgreSQL**.  
Supports creating, reading, updating, and deleting tasks.

---

## 🚀 Features
- Create, read, update, and delete tasks
- RESTful API with JSON payloads
- Task completion tracking
- PostGreSQL persistence
- Integration tests for service and controller layers

---

## 📦 Setup

### 1. Clone and Build
```bash
git clone https://github.com/banaga-benjamin/taskmanager
cd taskmanager
mvn clean install
```

### 2. Configure PostGreSQL
Login to PostgreSQL and create the database and user:
```sql
CREATE DATABASE taskdb;
CREATE USER taskuser WITH PASSWORD 'taskpass';
GRANT ALL PRIVILEGES ON DATABASE taskdb TO taskuser;

\c taskdb;
GRANT ALL PRIVILEGES ON SCHEMA public TO taskuser;
```

### 3. Application Properties
The application is configured with the following defaults in
`src/main/resources/application.properties`:
```
# Server settings
server.port=8080

# PostgreSQL datasource
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
spring.datasource.username=taskuser
spring.datasource.password=taskpass

# JPA settings
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 4. Run the Application
```
mvn spring-boot:run
```

### 5, Run Tests
Configure PostGreSQL
```sql
CREATE DATABASE taskdb_test;
CREATE USER taskuser WITH PASSWORD 'taskpass';
GRANT ALL PRIVILEGES ON DATABASE taskdb_test TO taskuser;

\c taskdb_test;
GRANT ALL PRIVILEGES ON SCHEMA public TO taskuser;
```

Run Tests

```
mvn test
```

---

## 📖 API Documentation

### Base URL
```bash
http://localhost:8080/api/tasks
```

### Endpoints (endpoint and request body)

1. Get All Tasks:
- GET `/api/tasks`

2. Get Task by ID:
- GET `/api/tasks/{id}`

3. Delete Task by ID:
- DELETE `/api/tasks/{id}`

4. Update Task by ID:
- PUT `/api/tasks/{id}`
- Request Body:
    ```json
    {
        "title": "updated title",
        "description": "updated description",
        "completed": true / false
    }
    ```

5. Create Task:
- POST `/api/tasks`
- Request Body:
    ```json
    {
        "title": "task title",
        "description": "task description"
    }
    ```
