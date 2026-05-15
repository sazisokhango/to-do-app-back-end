# Contract: Soft Delete Todo Item (UPDATED)

**Method**: `DELETE`
**Path**: `/api/todo/{id}`

> ⚠️ **Breaking change from `001-todo-task-management`**: This endpoint previously
> permanently removed the task. It now soft-deletes it — the task is hidden but
> preserved and can be restored. The HTTP status codes are unchanged.

---

## Request

**Path Parameters**:

| Parameter | Type | Description            |
|-----------|------|------------------------|
| id        | Long | Unique task identifier |

**Example**: `DELETE /api/todo/1`

---

## Responses

### 204 No Content

Empty body. The task has been soft-deleted (hidden from active list, preserved in database).

### 404 Not Found

```json
{ "status": 404, "error": "Not Found", "message": "Could not find the item" }
```

Triggers: no active task exists with the given `id` (either never existed, or already soft-deleted).
