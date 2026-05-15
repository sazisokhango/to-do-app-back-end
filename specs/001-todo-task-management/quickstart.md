# Quickstart: To-Do Back End

## Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL running locally

## Database Setup

```sql
CREATE DATABASE to_do;
CREATE USER to_do_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE to_do TO to_do_user;
```

## Run the Application

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080/api/todo`.

## Quick Smoke Test

```bash
# Create a task
curl -X POST http://localhost:8080/api/todo \
  -H "Content-Type: application/json" \
  -d '{"title":"Test task","priority":"HIGH","dueDate":"2026-12-31"}'

# Get all tasks
curl http://localhost:8080/api/todo

# Filter by priority
curl "http://localhost:8080/api/todo?priority=HIGH"

# Filter by completion
curl "http://localhost:8080/api/todo?completed=false"
```
