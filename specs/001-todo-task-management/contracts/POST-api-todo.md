# Contract: Create To-Do Item

**Method**: `POST`
**Path**: `/api/todo`

---

## Request

**Headers**: `Content-Type: application/json`

**Body** (`TodoRequest`):
```json
{
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "priority": "HIGH",
  "dueDate": "2026-05-20"
}
```

| Field       | Required | Notes                                  |
|-------------|----------|----------------------------------------|
| title       | Yes      | Max 255 characters                     |
| description | No       | Free text                              |
| priority    | No       | `LOW`, `MEDIUM`, `HIGH`. Defaults to `MEDIUM` |
| dueDate     | No       | ISO-8601 date. Must not be in the past |

---

## Responses

### 201 Created

```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "completed": false,
  "priority": "HIGH",
  "dueDate": "2026-05-20",
  "createdAt": "2026-05-15T10:30:00",
  "updatedAt": "2026-05-15T10:30:00"
}
```

### 400 Bad Request — Validation failure

```json
{ "status": 400, "error": "Bad Request", "message": "title must not be blank" }
```

Triggers:
- `title` is blank or missing
- `title` exceeds 255 characters
- `dueDate` is in the past
