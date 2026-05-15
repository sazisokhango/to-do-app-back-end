# Contract: Update To-Do Item

**Method**: `PUT`
**Path**: `/api/todo/{id}`

---

## Request

**Path Parameters**:

| Parameter | Type | Description          |
|-----------|------|----------------------|
| id        | Long | Unique task identifier |

**Headers**: `Content-Type: application/json`

**Body** (`TodoRequest`):
```json
{
  "title": "Buy groceries and cook dinner",
  "description": "Milk, eggs, bread, pasta",
  "priority": "HIGH",
  "dueDate": "2026-05-21"
}
```

| Field       | Required | Notes                                         |
|-------------|----------|-----------------------------------------------|
| title       | Yes      | Max 255 characters                            |
| description | No       | Free text                                     |
| priority    | No       | `LOW`, `MEDIUM`, `HIGH`. Defaults to `MEDIUM` |
| dueDate     | No       | ISO-8601 date. Must not be in the past        |

**Note**: To mark a task as completed, include `"completed": true` — this field is
accepted on update even though it is not part of the create request.

---

## Responses

### 200 OK

```json
{
  "id": 1,
  "title": "Buy groceries and cook dinner",
  "description": "Milk, eggs, bread, pasta",
  "completed": false,
  "priority": "HIGH",
  "dueDate": "2026-05-21",
  "createdAt": "2026-05-15T10:30:00",
  "updatedAt": "2026-05-15T14:00:00"
}
```

Note: `updatedAt` is refreshed. `createdAt` is unchanged.

### 400 Bad Request — Validation failure

```json
{ "status": 400, "error": "Bad Request", "message": "title must not be blank" }
```

### 404 Not Found

```json
{ "status": 404, "error": "Not Found", "message": "Could not find the item" }
```
