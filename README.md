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

| Method  | Path                      | Description              | Status |
|---------|---------------------------|--------------------------|--------|
| POST    | `/api/todo`               | Create a task            | 201    |
| GET     | `/api/todo`               | Get all active tasks     | 200    |
| GET     | `/api/todo/{id}`          | Get active task by ID    | 200    |
| PUT     | `/api/todo/{id}`          | Update a task            | 200    |
| DELETE  | `/api/todo/{id}`          | Soft delete a task       | 204    |
| GET     | `/api/todo/deleted`       | Get all deleted tasks    | 200    |
| PATCH   | `/api/todo/{id}/restore`  | Restore a deleted task   | 200    |

> ⚠️ **Breaking change**: `DELETE /api/todo/{id}` now soft-deletes (hides) the task
> instead of permanently removing it. Use `PATCH /api/todo/{id}/restore` to recover it.

### Filtering

```
GET /api/todo?priority=HIGH
GET /api/todo?completed=false
GET /api/todo?priority=HIGH&completed=false
```

Note: Filters only apply to active tasks — soft-deleted tasks are never included.

## Example Requests

```bash
# Create a task
curl -X POST http://localhost:8080/api/todo \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries","priority":"HIGH","dueDate":"2099-12-31"}'

# Get all active tasks
curl http://localhost:8080/api/todo

# Get by ID
curl http://localhost:8080/api/todo/1

# Update (mark complete)
curl -X PUT http://localhost:8080/api/todo/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Buy groceries","completed":true}'

# Soft delete a task
curl -X DELETE http://localhost:8080/api/todo/1

# View deleted tasks
curl http://localhost:8080/api/todo/deleted

# Restore a deleted task
curl -X PATCH http://localhost:8080/api/todo/1/restore
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
