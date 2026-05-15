# Data Model: To-Do Task Management

**Feature**: 001-todo-task-management
**Date**: 2026-05-15

---

## Entities

### TodoItem

The primary persistence entity, mapped to the `todo_items` table.

| Field       | Java Type        | DB Column      | Constraints                              |
|-------------|------------------|----------------|------------------------------------------|
| id          | `Long`           | `id` (PK)      | Auto-generated, not null                 |
| title       | `String`         | `title`        | Not null, max 255 characters             |
| description | `String`         | `description`  | Nullable                                 |
| completed   | `boolean`        | `completed`    | Not null, default `false`                |
| priority    | `Priority` (enum)| `priority`     | Not null, default `MEDIUM`               |
| dueDate     | `LocalDate`      | `due_date`     | Nullable, must not be in the past        |
| createdAt   | `LocalDateTime`  | `created_at`   | Not null, set on insert, never updated   |
| updatedAt   | `LocalDateTime`  | `updated_at`   | Not null, set on insert and every update |

**State transitions**:
- `completed` starts as `false` on creation.
- `completed` can be set to `true` or back to `false` via update.
- `createdAt` is immutable after creation (`@Column(updatable = false)`).
- `updatedAt` is refreshed on every update (`@PreUpdate`).

---

## Enums

### Priority

```
LOW | MEDIUM | HIGH
```

Stored as a `STRING` in the database (not ordinal) to remain stable if enum
order changes.

---

## DTOs

### TodoRequest (inbound)

Used for both `POST` (create) and `PUT` (update) requests.

| Field       | Java Type   | Validation                                    |
|-------------|-------------|-----------------------------------------------|
| title       | `String`    | `@NotBlank`, `@Size(max = 255)`               |
| description | `String`    | Optional, no constraints                      |
| priority    | `Priority`  | Optional, defaults to `MEDIUM` in service     |
| dueDate     | `LocalDate` | Optional, `@FutureOrPresent` when provided    |

### TodoResponse (outbound)

Returned for all successful read/create/update operations.

| Field       | Java Type        |
|-------------|------------------|
| id          | `Long`           |
| title       | `String`         |
| description | `String`         |
| completed   | `boolean`        |
| priority    | `Priority`       |
| dueDate     | `LocalDate`      |
| createdAt   | `LocalDateTime`  |
| updatedAt   | `LocalDateTime`  |

---

## Error Response

Returned for all failure scenarios (4xx). Defined by the constitution.

| Field   | Java Type | Example         |
|---------|-----------|-----------------|
| status  | `int`     | `404`           |
| error   | `String`  | `"Not Found"`   |
| message | `String`  | `"Could not find the item"` |

---

## Validation Rules Summary

| Field    | Rule                                     | HTTP Response on failure |
|----------|------------------------------------------|--------------------------|
| title    | Required, max 255 characters             | 400 Bad Request          |
| priority | Defaults to `MEDIUM` if omitted          | N/A (no failure)         |
| dueDate  | Must be today or in the future           | 400 Bad Request          |
| id (path)| Must exist in database                   | 404 Not Found            |
