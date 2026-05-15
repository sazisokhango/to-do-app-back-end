# Contract: Get All To-Do Items

**Method**: `GET`
**Path**: `/api/todo`

---

## Request

**Query Parameters** (all optional):

| Parameter | Type    | Values                        | Description              |
|-----------|---------|-------------------------------|--------------------------|
| priority  | String  | `LOW`, `MEDIUM`, `HIGH`       | Filter by priority level |
| completed | Boolean | `true`, `false`               | Filter by completion status |

**Examples**:
- `GET /api/todo` — all items
- `GET /api/todo?priority=HIGH` — high priority items only
- `GET /api/todo?completed=false` — incomplete items only
- `GET /api/todo?priority=HIGH&completed=false` — high priority incomplete items

---

## Responses

### 200 OK — with results

```json
[
  {
    "id": 1,
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "completed": false,
    "priority": "HIGH",
    "dueDate": "2026-05-20",
    "createdAt": "2026-05-15T10:30:00",
    "updatedAt": "2026-05-15T10:30:00"
  },
  {
    "id": 2,
    "title": "Call dentist",
    "description": null,
    "completed": true,
    "priority": "MEDIUM",
    "dueDate": null,
    "createdAt": "2026-05-14T09:00:00",
    "updatedAt": "2026-05-14T11:00:00"
  }
]
```

### 200 OK — empty list

```json
[]
```

### 400 Bad Request — invalid filter value

```json
{ "status": 400, "error": "Bad Request", "message": "Invalid priority value: URGENT" }
```
