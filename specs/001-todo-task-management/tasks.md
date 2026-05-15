---
description: "Task list for To-Do Task Management feature implementation"
---

# Tasks: To-Do Task Management

**Input**: Design documents from `specs/001-todo-task-management/`

**Prerequisites**: plan.md ✅ | spec.md ✅ | research.md ✅ | data-model.md ✅ | contracts/ ✅

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1–US5)
- All file paths are relative to the project root

## Path Conventions

All source files live under `src/main/java/com/example/todobackend/`

---

## Phase 1: Setup

**Purpose**: Project dependencies and configuration

- [ ] T001 Add `spring-boot-starter-validation` and `postgresql` driver dependencies to `pom.xml`
- [ ] T002 Configure PostgreSQL connection in `src/main/resources/application.properties` (url, username, password, JPA DDL auto, show-sql)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Shared infrastructure that ALL user stories depend on. No user story work can begin until this phase is complete.

**⚠️ CRITICAL**: Complete in order — Priority enum must exist before entities and DTOs.

- [ ] T003 Create `Priority` enum (`LOW`, `MEDIUM`, `HIGH`) in `src/main/java/com/example/todobackend/enums/Priority.java`
- [ ] T004 [P] Create `TodoItem` JPA entity with all fields, `@PrePersist`/`@PreUpdate` for timestamps, and `@Enumerated(STRING)` for priority in `src/main/java/com/example/todobackend/entity/TodoItem.java`
- [ ] T005 [P] Create `TodoRequest` DTO with `@NotBlank`, `@Size(max=255)` on title and `@FutureOrPresent` on dueDate in `src/main/java/com/example/todobackend/dto/TodoRequest.java`
- [ ] T006 [P] Create `TodoResponse` DTO with all fields matching the contract in `src/main/java/com/example/todobackend/dto/TodoResponse.java`
- [ ] T007 [P] Create `TodoNotFoundException` extending `RuntimeException` in `src/main/java/com/example/todobackend/exception/TodoNotFoundException.java`
- [ ] T008 Create `GlobalExceptionHandler` (`@RestControllerAdvice`) handling `TodoNotFoundException` → 404 and `MethodArgumentNotValidException` → 400, both returning the standard error JSON shape in `src/main/java/com/example/todobackend/exception/GlobalExceptionHandler.java`
- [ ] T009 [P] Create `CorsConfig` implementing `WebMvcConfigurer` allowing all methods and headers from `http://localhost:4200` on `/api/**` in `src/main/java/com/example/todobackend/config/CorsConfig.java`
- [ ] T010 Create `TodoRepository` extending `JpaRepository<TodoItem, Long>` in `src/main/java/com/example/todobackend/repository/TodoRepository.java`

**Checkpoint**: Foundation ready — all shared classes exist. User story implementation can now begin.

---

## Phase 3: User Story 1 — Create a Task (Priority: P1) 🎯 MVP

**Goal**: A user can submit a new task and receive it back with a generated ID, default priority if omitted, and timestamps.

**Independent Test**: `POST /api/todo` with `{"title":"Test"}` returns `201` with `id`, `completed=false`, `priority="MEDIUM"`.

- [ ] T011 [US1] Create `TodoService` with a `createTodo(TodoRequest)` method that defaults priority to `MEDIUM` if null and maps to/from `TodoItem` entity in `src/main/java/com/example/todobackend/service/TodoService.java`
- [ ] T012 [US1] Create `TodoController` with `POST /api/todo` endpoint annotated `@Valid`, delegating to `TodoService`, returning `ResponseEntity<TodoResponse>` with status `201` in `src/main/java/com/example/todobackend/controller/TodoController.java`

**Checkpoint**: User Story 1 fully functional. `POST /api/todo` creates and returns a task.

---

## Phase 4: User Story 2 — View Tasks (Priority: P2)

**Goal**: A user can retrieve all tasks as a list, or a single task by ID. Empty list returns `[]`, unknown ID returns `404`.

**Independent Test**: `GET /api/todo` returns `200` with a list; `GET /api/todo/1` returns the task; `GET /api/todo/999` returns `404`.

- [ ] T013 [US2] Add `getAllTodos()` method to `TodoService` returning `List<TodoResponse>` in `src/main/java/com/example/todobackend/service/TodoService.java`
- [ ] T014 [US2] Add `getTodoById(Long id)` method to `TodoService` throwing `TodoNotFoundException` when not found in `src/main/java/com/example/todobackend/service/TodoService.java`
- [ ] T015 [US2] Add `GET /api/todo` and `GET /api/todo/{id}` endpoints to `TodoController` in `src/main/java/com/example/todobackend/controller/TodoController.java`

**Checkpoint**: User Stories 1 and 2 independently functional.

---

## Phase 5: User Story 3 — Update a Task (Priority: P3)

**Goal**: A user can update any field of an existing task including `completed`. `updatedAt` is refreshed. Invalid input returns `400`, unknown ID returns `404`.

**Independent Test**: `PUT /api/todo/1` with new title returns `200` with updated title and refreshed `updatedAt`.

- [ ] T016 [US3] Add `updateTodo(Long id, TodoRequest)` method to `TodoService` that applies all fields from the request, saves, and returns `TodoResponse` in `src/main/java/com/example/todobackend/service/TodoService.java`
- [ ] T017 [US3] Add `PUT /api/todo/{id}` endpoint annotated `@Valid` to `TodoController` returning `200` in `src/main/java/com/example/todobackend/controller/TodoController.java`

**Checkpoint**: User Stories 1, 2, and 3 independently functional.

---

## Phase 6: User Story 4 — Delete a Task (Priority: P4)

**Goal**: A user can permanently delete a task by ID. Returns `204` on success, `404` if not found.

**Independent Test**: `DELETE /api/todo/1` returns `204`; subsequent `GET /api/todo/1` returns `404`.

- [ ] T018 [US4] Add `deleteTodo(Long id)` method to `TodoService` throwing `TodoNotFoundException` when not found in `src/main/java/com/example/todobackend/service/TodoService.java`
- [ ] T019 [US4] Add `DELETE /api/todo/{id}` endpoint to `TodoController` returning `ResponseEntity<Void>` with status `204` in `src/main/java/com/example/todobackend/controller/TodoController.java`

**Checkpoint**: User Stories 1–4 independently functional. Full CRUD complete.

---

## Phase 7: User Story 5 — Filter Tasks (Priority: P5)

**Goal**: `GET /api/todo` accepts optional `priority` and `completed` query parameters. Both, either, or neither filter may be applied. Invalid filter values return `400`.

**Independent Test**: `GET /api/todo?priority=HIGH` returns only HIGH priority tasks. `GET /api/todo?completed=false&priority=MEDIUM` returns only incomplete MEDIUM tasks.

- [ ] T020 [US5] Add derived query methods `findByPriority`, `findByCompleted`, and `findByPriorityAndCompleted` to `TodoRepository` in `src/main/java/com/example/todobackend/repository/TodoRepository.java`
- [ ] T021 [US5] Add `getTodos(Priority priority, Boolean completed)` method to `TodoService` that dispatches to the correct repository method based on which parameters are non-null in `src/main/java/com/example/todobackend/service/TodoService.java`
- [ ] T022 [US5] Update `GET /api/todo` endpoint in `TodoController` to accept optional `@RequestParam` `priority` and `completed`, delegating to the new service method in `src/main/java/com/example/todobackend/controller/TodoController.java`

**Checkpoint**: All 5 user stories independently functional.

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Documentation and final validation

- [ ] T023 Create `README.md` at project root documenting prerequisites, database setup, how to run, and example curl commands (see `specs/001-todo-task-management/quickstart.md`)
- [ ] T024 [P] Run smoke test from `specs/001-todo-task-management/quickstart.md` to verify all endpoints respond correctly
- [ ] T025 [P] Verify CORS by confirming a request from `http://localhost:4200` is not blocked (check response headers include `Access-Control-Allow-Origin`)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately
- **Foundational (Phase 2)**: Depends on Phase 1 — **blocks all user stories**
- **US1 (Phase 3)**: Depends on Foundational — MVP increment
- **US2 (Phase 4)**: Depends on Foundational — can start after Phase 2 (independent of US1)
- **US3 (Phase 5)**: Depends on US2 (needs findById logic)
- **US4 (Phase 6)**: Depends on Foundational — can start after Phase 2
- **US5 (Phase 7)**: Depends on US2 (GET /api/todo must exist first)
- **Polish (Phase 8)**: Depends on all user stories complete

### Within Each Phase

- T003 must complete before T004, T005, T006, T010
- T007 must complete before T008
- T011 must complete before T012
- T013, T014 must complete before T015
- T016 must complete before T017
- T018 must complete before T019
- T020, T021 must complete before T022

### Parallel Opportunities

```bash
# Phase 2 — after T003 completes, these can run in parallel:
T004  Create TodoItem entity
T005  Create TodoRequest DTO
T006  Create TodoResponse DTO
T007  Create TodoNotFoundException
T009  Create CorsConfig

# Phase 8 — can run in parallel:
T024  Smoke test
T025  CORS verification
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1 (T011, T012)
4. **STOP and VALIDATE**: `POST /api/todo` works end-to-end
5. Continue with remaining stories

### Incremental Delivery

1. Phases 1–2 → Foundation ready
2. Phase 3 → Create tasks working (MVP)
3. Phase 4 → View tasks working
4. Phase 5 → Update tasks working
5. Phase 6 → Delete tasks working
6. Phase 7 → Filtering working
7. Phase 8 → Polish and documentation

---

## Notes

- `[P]` tasks have no dependencies on incomplete tasks and operate on different files
- Each user story phase is independently completable and testable
- `updatedAt` refresh is handled by `@PreUpdate` on the entity (T004) — no service logic needed
- Priority defaulting to `MEDIUM` is handled in the service layer (T011), not in the DTO
- Filtering dispatch logic in T021 uses `if/else` on null-checks — no Specification pattern needed (per constitution: complexity must be justified)
