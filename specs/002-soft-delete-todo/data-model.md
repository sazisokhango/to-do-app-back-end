# Data Model: Soft Delete Todo Item

**Feature**: 002-soft-delete-todo
**Date**: 2026-05-15

---

## Entity Changes

### TodoItem (updated)

One field is added to the existing entity:

| Field      | Java Type       | DB Column    | Constraints                       |
|------------|-----------------|--------------|-----------------------------------|
| deletedAt  | `LocalDateTime` | `deleted_at` | Nullable. Null = active. Set = soft-deleted. |

**State transitions (updated)**:
- On creation: `deletedAt = null` (active)
- On soft delete: `deletedAt = LocalDateTime.now()`
- On restore: `deletedAt = null`

All other fields (`id`, `title`, `description`, `completed`, `priority`, `dueDate`,
`createdAt`, `updatedAt`) are unchanged.

---

## DTO Changes

### TodoResponse (updated)

One field is added:

| Field     | Java Type       | Notes                              |
|-----------|-----------------|------------------------------------|
| deletedAt | `LocalDateTime` | Nullable. Present for deleted items, null for active items. |

---

## Repository Changes

All existing query methods gain a `DeletedAtIsNull` condition to exclude
soft-deleted items from active queries. New methods are added for deleted items.

| Old Method | New Method (active queries) |
|---|---|
| `findAll()` | `findAllByDeletedAtIsNull()` |
| `findByPriority(Priority)` | `findByPriorityAndDeletedAtIsNull(Priority)` |
| `findByCompleted(boolean)` | `findByCompletedAndDeletedAtIsNull(boolean)` |
| `findByPriorityAndCompleted(Priority, boolean)` | `findByPriorityAndCompletedAndDeletedAtIsNull(Priority, boolean)` |
| `findById(Long)` | `findByIdAndDeletedAtIsNull(Long)` |

**New methods:**

| Method | Purpose |
|---|---|
| `findAllByDeletedAtIsNotNull()` | Returns all soft-deleted tasks |
| `findByIdAndDeletedAtIsNotNull(Long)` | Looks up a specific soft-deleted task (for restore) |

---

## Validation Rules (unchanged)

No new validation rules. The `deletedAt` field is managed entirely by the service
layer — it is never part of any request body.
