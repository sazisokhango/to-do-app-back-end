# Contract: Delete To-Do Item

**Method**: `DELETE`
**Path**: `/api/todo/{id}`

---

## Request

**Path Parameters**:

| Parameter | Type | Description          |
|-----------|------|----------------------|
| id        | Long | Unique task identifier |

**Example**: `DELETE /api/todo/1`

---

## Responses

### 204 No Content

Empty body. The task has been permanently deleted.

### 404 Not Found

```json
{ "status": 404, "error": "Not Found", "message": "Could not find the item" }
```

Triggers: no task exists with the given `id`.
