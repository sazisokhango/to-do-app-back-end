# Contract: Get Deleted Todo Items (NEW)

**Method**: `GET`
**Path**: `/api/todo/deleted`

---

## Request

No parameters.

**Example**: `GET /api/todo/deleted`

---

## Responses

### 200 OK — with deleted items

```json
[
  {
    "id": 1,
    "title": "Buy groceries",
    "description": "Milk and eggs",
    "completed": false,
    "priority": "HIGH",
    "dueDate": "2026-05-20",
    "createdAt": "2026-05-15T10:30:00",
    "updatedAt": "2026-05-15T10:30:00",
    "deletedAt": "2026-05-15T14:00:00"
  }
]
```

### 200 OK — no deleted items

```json
[]
```

Note: `deletedAt` is always set (non-null) for items returned by this endpoint.
Active tasks are never included in this response.
