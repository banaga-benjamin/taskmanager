# Task Manager (Spring Boot + PostgreSQL)

A simple Task Manager REST API built with **Spring Boot**, **Spring Data JPA**, and **PostgreSQL**.  
Supports creating, reading, updating, and deleting tasks.

---

## 🚀 Features
- Create, read, update, and delete tasks
- RESTful API with JSON payloads
- Task completion tracking
- PostGreSQL persistence
- Data security with JWT authentication
- Integration tests for task management and authentication controller layers

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
---

## 📦 Testing

### 1, Configure PostGreSQL
Configure PostGreSQL
```sql
CREATE DATABASE taskdb_test;
CREATE USER taskuser WITH PASSWORD 'taskpass';
GRANT ALL PRIVILEGES ON DATABASE taskdb_test TO taskuser;

\c taskdb_test;
GRANT ALL PRIVILEGES ON SCHEMA public TO taskuser;
```

### 2. Application Properties
For testing, the application is configured with the following defaults in
`src/test/resources/application.properties`:
```
# Server settings
server.port=8080

# PostgreSQL datasource
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb_test
spring.datasource.username=taskuser
spring.datasource.password=taskpass

# JPA settings
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Run Tests

```
mvn test
```

---

## 📖 API Documentation

### Base URL and Login

The base url for accessing tasks is:
```
http://localhost:8080/api/tasks
```

For the register, login, and pasword change endpoints, the base url is:
```
http://localhost:8080/api/auth
```

### Register, Login, & Password Change Endpoints (endpoint and request body)
1. Register:
- POST `/api/auth/register`
- Request Body:
    ```json
    {
        "username": "user name",
        "password": "user password"
    }
    ```

2. Login:
- POST `/api/auth/login`
- Request Body:
    ```json
    {
        "username": "user name",
        "password": "user password"
    }
    ```

3. Password Change:
- PUT `/api/auth/update-password`
- Request Body:
    ```json
    {
        "oldPassword": "old password",
        "newPassword": "new password"
    }
    ```
    
### Task Endpoints (endpoint and request body)

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
        "completed": true or false
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
### JWT Authentication

The API uses JWT for authentication. When accessing the task endpoints, a valid token must be present as the value of the Authorizaiton header of the request body.

```json
    {
        "Authorization": "Bearer [valid-token]",
    }
```