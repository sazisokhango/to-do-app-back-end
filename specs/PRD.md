# To-Do Application Specification

## Backend

### Technology
- Java Spring Boot
- PostgreSQL

---

## Spec

Please create a Spring Boot application for a To-Do Application.

### Entities

**TodoItem:**
```json
{
  "id": 1,
  "title": "Example",
  "description": "Example description",
  "completed": false,
  "priority": "MEDIUM",
  "dueDate": "2026-05-20",
  "createdAt": "2026-05-12T10:30:00",
  "updatedAt": "2026-05-12T10:30:00"
}
```

**Priority Enum:**
Values: `LOW`, `MEDIUM`, `HIGH`

---

### DTOs

**TodoRequest:**
```json
{
  "title": "Example",
  "description": "Example description",
  "priority": "MEDIUM",
  "dueDate": "2026-05-20"
}
```

**TodoResponse:**
```json
{
  "id": 1,
  "title": "Example",
  "description": "Example",
  "completed": false,
  "priority": "MEDIUM",
  "dueDate": "2026-05-20",
  "createdAt": "2026-05-12T10:30:00",
  "updatedAt": "2026-05-12T10:30:00"
}
```

---

### Controller

Base URL: `api/todo`

| Method | Endpoint       | Request Body  | Response         | Description                  |
|--------|----------------|---------------|------------------|------------------------------|
| POST   | `api/todo`     | TodoRequest   | 201 Created      | Create a new to-do item      |
| GET    | `api/todo`     | -             | TodoResponse[]   | Get all to-do items          |
| GET    | `api/todo/{id}`| -             | TodoResponse     | Get a specific to-do item    |
| PUT    | `api/todo/{id}`| TodoRequest   | 200 OK           | Update a to-do item          |
| DELETE | `api/todo/{id}`| -             | 204 No Content   | Delete a to-do item          |

**Error Response (item not found):**
```json
{ "status": 404, "error": "Not Found", "message": "Could not find the item" }
```

**Error Codes:**
- `201` – Created (POST)
- `200` – OK (PUT)
- `204` – No Content (DELETE)
- `404` – Not Found (GET/PUT/DELETE when item does not exist)
- `400` – Bad Request (invalid input)

---

### Filtering

Filter GET `api/todo` by:
- `priority` – filter by `LOW`, `MEDIUM`, or `HIGH`
- `completed` – filter by `true` or `false`

---

### Validation Rules

| Field     | Rule                                      |
|-----------|-------------------------------------------|
| title     | Maximum 255 characters, required          |
| priority  | Defaults to `MEDIUM` if omitted           |
| dueDate   | Must not be in the past                   |

---

### Configuration

**CORS:** Allow requests from `http://localhost:4200`

**Database:**
- Name: `to_do`
- Username: `to_do_user`
- Password: `password`

---

### Documentation

Please also create a `README.md` that explains how the project works.
