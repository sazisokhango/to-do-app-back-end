# Contract: Get To-Do Item by ID

**Method**: `GET`
**Path**: `/api/todo/{id}`

---

## Request

**Path Parameters**:

| Parameter | Type | Description          |
|-----------|------|----------------------|
| id        | Long | Unique task identifier |

**Example**: `GET /api/todo/1`

---

## Responses

### 200 OK

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

### 404 Not Found

```json
{ "status": 404, "error": "Not Found", "message": "Could not find the item" }
```

Triggers: no task exists with the given `id`.
