# Implementation Plan: Soft Delete Todo Item

**Branch**: `feature/soft-delete-todo` | **Date**: 2026-05-15 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/002-soft-delete-todo/spec.md`

---

## Summary

Modify the existing delete endpoint to soft-delete tasks (set `deletedAt`) instead
of permanently removing them. Add two new endpoints — one to list deleted tasks and
one to restore them. All existing active-task queries are updated to exclude
soft-deleted items.

---

## Technical Context

**Language/Version**: Java 21

**Primary Changes**:
- `TodoItem` entity: add `deletedAt` (`LocalDateTime`, nullable)
- `TodoResponse` DTO: add `deletedAt` field
- `TodoRepository`: replace existing query methods with `DeletedAtIsNull` variants; add `DeletedAtIsNotNull` methods
- `TodoService`: update `deleteTodo`, `getTodoById`, `getTodos`, `updateTodo`; add `getDeletedTodos`, `restoreTodo`
- `TodoController`: update `DELETE`; add `GET /api/todo/deleted`, `PATCH /api/todo/{id}/restore`

**Storage**: PostgreSQL — `deleted_at` column added to `todo_items` table (nullable, auto-migrated via `ddl-auto=update`)

**Testing**: JUnit 5 + H2 in-memory (same setup as feature 001)

**Breaking change**: `DELETE /api/todo/{id}` no longer permanently removes — documented in contracts.

---

## Constitution Check

| Principle | Check | Status |
|-----------|-------|--------|
| I. REST API First | `DELETE` → 204, `GET /deleted` → 200, `PATCH /restore` → 200, 404 for not found — all match constitution status codes | ✅ PASS |
| II. Strict Input Validation | No new input fields; `deletedAt` is service-managed, never from request body | ✅ PASS |
| III. Layered Architecture | All changes go through Controller → Service → Repository. No shortcuts. | ✅ PASS |
| IV. Consistent Error Handling | Existing `GlobalExceptionHandler` and `TodoNotFoundException` handle all new 404 cases | ✅ PASS |
| V. Frontend Compatibility | No CORS changes needed | ✅ PASS |

All gates pass. Implementation may proceed.

---

## Project Structure

### Documentation (this feature)

```text
specs/002-soft-delete-todo/
├── plan.md              ← This file
├── research.md          ← Phase 0 output
├── data-model.md        ← Phase 1 output
├── quickstart.md        ← Phase 1 output
├── contracts/
│   ├── DELETE-api-todo-id.md      ← Updated (breaking change)
│   ├── GET-api-todo-deleted.md    ← New
│   └── PATCH-api-todo-id-restore.md ← New
└── tasks.md             ← Phase 2 output (/speckit-tasks)
```

### Source Code Changes

```text
src/main/java/com/example/todobackend/
├── entity/
│   └── TodoItem.java              ← ADD: deletedAt field
├── dto/
│   └── TodoResponse.java          ← ADD: deletedAt field
├── repository/
│   └── TodoRepository.java        ← REPLACE: all queries with DeletedAtIsNull variants
│                                    ADD: DeletedAtIsNotNull methods
├── service/
│   └── TodoService.java           ← UPDATE: deleteTodo, getTodoById, getTodos, updateTodo
│                                    ADD: getDeletedTodos, restoreTodo
└── controller/
    └── TodoController.java        ← UPDATE: DELETE endpoint
                                     ADD: GET /deleted, PATCH /{id}/restore
```

**Structure Decision**: All changes are within the existing single-project layout. No new packages needed.

---

## Complexity Tracking

No constitution violations. The `deletedAtIsNull` query method naming convention
is a direct extension of the existing derived query pattern — no new abstraction
layers introduced.
