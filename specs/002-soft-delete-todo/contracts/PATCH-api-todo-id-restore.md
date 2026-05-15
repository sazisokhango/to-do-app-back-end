# Contract: Restore Deleted Todo Item (NEW)

**Method**: `PATCH`
**Path**: `/api/todo/{id}/restore`

---

## Request

**Path Parameters**:

| Parameter | Type | Description            |
|-----------|------|------------------------|
| id        | Long | Unique task identifier |

No request body.

**Example**: `PATCH /api/todo/1/restore`

---

## Responses

### 200 OK

Returns the restored task as an active `TodoResponse`. `deletedAt` is `null`.

```json
{
  "id": 1,
  "title": "Buy groceries",
  "description": "Milk and eggs",
  "completed": false,
  "priority": "HIGH",
  "dueDate": "2026-05-20",
  "createdAt": "2026-05-15T10:30:00",
  "updatedAt": "2026-05-15T10:30:00",
  "deletedAt": null
}
```

### 404 Not Found

```json
{ "status": 404, "error": "Not Found", "message": "Could not find the item" }
```

Triggers:
- No task exists with the given `id`
- The task exists but is already active (not soft-deleted)
