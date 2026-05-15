# To-Do Back End

A RESTful To-Do Task Management API built with Spring Boot 3.5, Java 21, and PostgreSQL.

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

## Run

```bash
./mvnw spring-boot:run
```

API is available at `http://localhost:8080/api/todo`.

## Endpoints

| Method | Path              | Description         | Status |
|--------|-------------------|---------------------|--------|
| POST   | `/api/todo`       | Create a task       | 201    |
| GET    | `/api/todo`       | Get all tasks       | 200    |
| GET    | `/api/todo/{id}`  | Get task by ID      | 200    |
| PUT    | `/api/todo/{id}`  | Update a task       | 200    |
| DELETE | `/api/todo/{id}`  | Delete a task       | 204    |

### Filtering

```
GET /api/todo?priority=HIGH
GET /api/todo?completed=false
GET /api/todo?priority=HIGH&completed=false
```

## Example Requests

```bash
# Create a task
curl -X POST http://localhost:8080/api/todo \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries","priority":"HIGH","dueDate":"2026-12-31"}'

# Get all tasks
curl http://localhost:8080/api/todo

# Get by ID
curl http://localhost:8080/api/todo/1

# Update (mark complete)
curl -X PUT http://localhost:8080/api/todo/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries","completed":true}'

# Delete
curl -X DELETE http://localhost:8080/api/todo/1
```

## Validation Rules

| Field     | Rule                                  |
|-----------|---------------------------------------|
| `title`   | Required, max 255 characters          |
| `priority`| Optional, defaults to `MEDIUM`        |
| `dueDate` | Optional, must not be in the past     |

## Error Response Shape

```json
{ "status": 404, "error": "Not Found", "message": "Could not find the item" }
```
