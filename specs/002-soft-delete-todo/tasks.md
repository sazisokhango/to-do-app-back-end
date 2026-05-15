---
description: "Task list for Soft Delete Todo Item feature implementation"
---

# Tasks: Soft Delete Todo Item

**Input**: Design documents from `specs/002-soft-delete-todo/`

**Prerequisites**: plan.md ✅ | spec.md ✅ | research.md ✅ | data-model.md ✅ | contracts/ ✅

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1–US3)
- All file paths are relative to the project root

---

## Phase 1: Setup

**Purpose**: Prepare existing files for soft delete changes.

- [x] T001 Add `deletedAt` (`LocalDateTime`, nullable) field with `@Column(name = "deleted_at")` to `src/main/java/com/example/todobackend/entity/TodoItem.java`
- [x] T002 [P] Add `deletedAt` (`LocalDateTime`, nullable) field to `src/main/java/com/example/todobackend/dto/TodoResponse.java`
- [x] T003 [P] Add getter `getDeletedAt()` to `src/main/java/com/example/todobackend/entity/TodoItem.java` and setter `setDeletedAt(LocalDateTime)`

**Note**: T001 and T003 modify the same file — run sequentially. T002 is independent.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Update the repository with new query methods. All user stories depend on this.

**⚠️ CRITICAL**: Must complete before any service or controller changes.

- [x] T004 Replace `findAll()` usage with `findAllByDeletedAtIsNull()` — add method to `src/main/java/com/example/todobackend/repository/TodoRepository.java`
- [x] T005 [P] Replace `findByPriority(Priority)` with `findByPriorityAndDeletedAtIsNull(Priority)` in `src/main/java/com/example/todobackend/repository/TodoRepository.java`
- [x] T006 [P] Replace `findByCompleted(boolean)` with `findByCompletedAndDeletedAtIsNull(boolean)` in `src/main/java/com/example/todobackend/repository/TodoRepository.java`
- [x] T007 [P] Replace `findByPriorityAndCompleted(Priority, boolean)` with `findByPriorityAndCompletedAndDeletedAtIsNull(Priority, boolean)` in `src/main/java/com/example/todobackend/repository/TodoRepository.java`
- [x] T008 [P] Add `findByIdAndDeletedAtIsNull(Long)` to `src/main/java/com/example/todobackend/repository/TodoRepository.java`
- [x] T009 [P] Add `findAllByDeletedAtIsNotNull()` to `src/main/java/com/example/todobackend/repository/TodoRepository.java`
- [x] T010 [P] Add `findByIdAndDeletedAtIsNotNull(Long)` to `src/main/java/com/example/todobackend/repository/TodoRepository.java`

**Checkpoint**: Repository ready — service changes can now begin.

---

## Phase 3: User Story 1 — Soft Delete a Task (Priority: P1) 🎯

**Goal**: `DELETE /api/todo/{id}` hides the task instead of permanently removing it.
Active task queries no longer surface deleted items.

**Independent Test**: Delete a task, then `GET /api/todo` — task not in list.
`GET /api/todo/{id}` for deleted task returns 404.

- [x] T011 [US1] Update `deleteTodo(Long id)` in `TodoService` to set `deletedAt = LocalDateTime.now()` and save instead of calling `deleteById` in `src/main/java/com/example/todobackend/service/TodoService.java`
- [x] T012 [US1] Update `getTodoById(Long id)` in `TodoService` to use `findByIdAndDeletedAtIsNull` in `src/main/java/com/example/todobackend/service/TodoService.java`
- [x] T013 [US1] Update `getTodos()` in `TodoService` to use `DeletedAtIsNull` query variants for all four filter combinations in `src/main/java/com/example/todobackend/service/TodoService.java`
- [x] T014 [US1] Update `updateTodo(Long id, TodoRequest)` in `TodoService` to use `findByIdAndDeletedAtIsNull` so soft-deleted items cannot be updated in `src/main/java/com/example/todobackend/service/TodoService.java`
- [x] T015 [US1] Update `toResponse()` mapping in `TodoService` to include `deletedAt` field in `src/main/java/com/example/todobackend/service/TodoService.java`

**Checkpoint**: US1 functional — soft delete works, deleted items hidden from all active queries.

---

## Phase 4: User Story 2 — View Deleted Tasks (Priority: P2)

**Goal**: `GET /api/todo/deleted` returns all soft-deleted tasks. Active tasks
are never included. Empty list returns `[]`.

**Independent Test**: Soft-delete a task, then `GET /api/todo/deleted` — task appears
with `deletedAt` timestamp set.

- [x] T016 [US2] Add `getDeletedTodos()` method to `TodoService` using `findAllByDeletedAtIsNotNull()` in `src/main/java/com/example/todobackend/service/TodoService.java`
- [x] T017 [US2] Add `GET /api/todo/deleted` endpoint to `TodoController` returning `ResponseEntity<List<TodoResponse>>` with status `200` in `src/main/java/com/example/todobackend/controller/TodoController.java`

**Checkpoint**: US1 and US2 functional — deleted tasks visible via dedicated endpoint.

---

## Phase 5: User Story 3 — Restore a Deleted Task (Priority: P3)

**Goal**: `PATCH /api/todo/{id}/restore` clears `deletedAt`, returning the task
to the active list. Returns 404 if task not found or already active.

**Independent Test**: Soft-delete a task, restore it, then `GET /api/todo` — task
reappears. `GET /api/todo/deleted` — task no longer listed.

- [x] T018 [US3] Add `restoreTodo(Long id)` method to `TodoService` using `findByIdAndDeletedAtIsNotNull`, setting `deletedAt = null` and saving in `src/main/java/com/example/todobackend/service/TodoService.java`
- [x] T019 [US3] Add `PATCH /api/todo/{id}/restore` endpoint to `TodoController` returning `ResponseEntity<TodoResponse>` with status `200` in `src/main/java/com/example/todobackend/controller/TodoController.java`

**Checkpoint**: All 3 user stories functional — full soft delete lifecycle works.

---

## Phase 6: Polish

- [x] T020 Update `README.md` to document the new endpoints (`GET /api/todo/deleted`, `PATCH /api/todo/{id}/restore`) and the breaking change to `DELETE`
- [x] T021 [P] Run smoke tests from `specs/002-soft-delete-todo/quickstart.md` to verify full lifecycle works end-to-end

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately
- **Foundational (Phase 2)**: Depends on Phase 1 (T001–T003)
- **US1 (Phase 3)**: Depends on Foundational — T011–T015 can follow T004–T010
- **US2 (Phase 4)**: Depends on US1 (T016 uses the updated `toResponse()` from T015)
- **US3 (Phase 5)**: Depends on US2 (restore is only meaningful once deleted list works)
- **Polish (Phase 6)**: Depends on all stories complete

### Within Phase 2 (Repository)

- T004–T010 all modify `TodoRepository.java` — run sequentially to avoid conflicts

### Within Phase 3 (Service — US1)

- T011–T015 all modify `TodoService.java` — run sequentially
- T015 (toResponse update) must be last — all other service methods depend on it

---

## Parallel Opportunities

```bash
# Phase 1 — T002 is independent of T001/T003:
T001 + T003  Add deletedAt to entity (same file, sequential)
T002         Add deletedAt to TodoResponse (different file, parallel)
```

---

## Notes

- `deletedAt` is **never** set from a request body — it is entirely service-managed
- Existing `DeleteTodoTest` in feature 001 will need updating — `DELETE` no longer permanently removes, so the "delete then GET returns 404" test still passes but the row is not gone from the DB
- `GET /api/todo/deleted` uses a literal path — Spring MVC resolves it before `GET /api/todo/{id}`, so no routing conflict
- Priority defaulting and all existing validation rules are unchanged
