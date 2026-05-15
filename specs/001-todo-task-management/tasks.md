---
description: "Task list for To-Do Task Management feature implementation — updated to include tests"
---

# Tasks: To-Do Task Management

**Input**: Design documents from `specs/001-todo-task-management/`

**Prerequisites**: plan.md ✅ | spec.md ✅ | research.md ✅ | data-model.md ✅ | contracts/ ✅

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1–US5)
- All source paths relative to project root

---

## Phase 1: Setup

- [x] T001 Add `spring-boot-starter-validation` and `postgresql` driver dependencies to `pom.xml`
- [x] T002 Configure PostgreSQL connection in `src/main/resources/application.properties`
- [x] T026 Add `h2` dependency (test scope) to `pom.xml` for in-memory test database
- [x] T027 Create `src/test/resources/application.properties` configuring H2 in-memory datasource and `spring.jpa.hibernate.ddl-auto=create-drop`

---

## Phase 2: Foundational

- [x] T003 Create `Priority` enum in `src/main/java/com/example/todobackend/enums/Priority.java`
- [x] T004 [P] Create `TodoItem` JPA entity in `src/main/java/com/example/todobackend/entity/TodoItem.java`
- [x] T005 [P] Create `TodoRequest` DTO in `src/main/java/com/example/todobackend/dto/TodoRequest.java`
- [x] T006 [P] Create `TodoResponse` DTO in `src/main/java/com/example/todobackend/dto/TodoResponse.java`
- [x] T007 [P] Create `TodoNotFoundException` in `src/main/java/com/example/todobackend/exception/TodoNotFoundException.java`
- [x] T008 Create `GlobalExceptionHandler` in `src/main/java/com/example/todobackend/exception/GlobalExceptionHandler.java`
- [x] T009 [P] Create `CorsConfig` in `src/main/java/com/example/todobackend/config/CorsConfig.java`
- [x] T010 Create `TodoRepository` in `src/main/java/com/example/todobackend/repository/TodoRepository.java`

**Checkpoint**: Foundation complete.

---

## Phase 3: User Story 1 — Create a Task (Priority: P1) 🎯 MVP

**Goal**: A user can submit a new task and receive it back with a generated ID, default priority, and timestamps.

**Independent Test**: `POST /api/todo` with `{"title":"Test"}` returns `201` with `completed=false`, `priority="MEDIUM"`.

### Tests for User Story 1 ⚠️ Write these FIRST — ensure they FAIL before implementation

- [x] T028 [P] [US1] Write test: `POST /api/todo` with valid body returns `201` and correct `TodoResponse` in `src/test/java/com/example/todobackend/controller/CreateTodoTest.java`
- [x] T029 [P] [US1] Write test: `POST /api/todo` without `priority` returns `201` with `priority=MEDIUM` in `src/test/java/com/example/todobackend/controller/CreateTodoTest.java`
- [x] T030 [P] [US1] Write test: `POST /api/todo` with past `dueDate` returns `400` with error body in `src/test/java/com/example/todobackend/controller/CreateTodoTest.java`
- [x] T031 [P] [US1] Write test: `POST /api/todo` with blank `title` returns `400` in `src/test/java/com/example/todobackend/controller/CreateTodoTest.java`
- [x] T032 [P] [US1] Write test: `POST /api/todo` with `title` > 255 chars returns `400` in `src/test/java/com/example/todobackend/controller/CreateTodoTest.java`

### Implementation for User Story 1

- [x] T011 [US1] Create `TodoService.createTodo()` in `src/main/java/com/example/todobackend/service/TodoService.java`
- [x] T012 [US1] Create `TodoController` `POST /api/todo` in `src/main/java/com/example/todobackend/controller/TodoController.java`

**Checkpoint**: US1 tests pass, endpoint functional.

---

## Phase 4: User Story 2 — View Tasks (Priority: P2)

**Goal**: A user can retrieve all tasks or a single task by ID.

**Independent Test**: `GET /api/todo` returns list; `GET /api/todo/{id}` returns task; unknown ID returns `404`.

### Tests for User Story 2 ⚠️ Write these FIRST

- [x] T033 [P] [US2] Write test: `GET /api/todo` returns `200` with list of tasks in `src/test/java/com/example/todobackend/controller/ViewTodoTest.java`
- [x] T034 [P] [US2] Write test: `GET /api/todo` with no tasks returns `200` with empty array in `src/test/java/com/example/todobackend/controller/ViewTodoTest.java`
- [x] T035 [P] [US2] Write test: `GET /api/todo/{id}` with existing ID returns `200` with correct task in `src/test/java/com/example/todobackend/controller/ViewTodoTest.java`
- [x] T036 [P] [US2] Write test: `GET /api/todo/{id}` with unknown ID returns `404` with error body in `src/test/java/com/example/todobackend/controller/ViewTodoTest.java`

### Implementation for User Story 2

- [x] T013 [US2] Add `getAllTodos()` to `TodoService`
- [x] T014 [US2] Add `getTodoById()` to `TodoService`
- [x] T015 [US2] Add `GET /api/todo` and `GET /api/todo/{id}` to `TodoController`

**Checkpoint**: US2 tests pass, view endpoints functional.

---

## Phase 5: User Story 3 — Update a Task (Priority: P3)

**Goal**: A user can update any field including `completed`. `updatedAt` refreshes.

**Independent Test**: `PUT /api/todo/1` returns `200` with updated fields and refreshed `updatedAt`.

### Tests for User Story 3 ⚠️ Write these FIRST

- [x] T037 [P] [US3] Write test: `PUT /api/todo/{id}` with valid body returns `200` with updated fields in `src/test/java/com/example/todobackend/controller/UpdateTodoTest.java`
- [x] T038 [P] [US3] Write test: `PUT /api/todo/{id}` with `completed=true` returns `200` with `completed=true` in `src/test/java/com/example/todobackend/controller/UpdateTodoTest.java`
- [x] T039 [P] [US3] Write test: `PUT /api/todo/{id}` with unknown ID returns `404` in `src/test/java/com/example/todobackend/controller/UpdateTodoTest.java`
- [x] T040 [P] [US3] Write test: `PUT /api/todo/{id}` with blank title returns `400` in `src/test/java/com/example/todobackend/controller/UpdateTodoTest.java`

### Implementation for User Story 3

- [x] T016 [US3] Add `updateTodo()` to `TodoService`
- [x] T017 [US3] Add `PUT /api/todo/{id}` to `TodoController`

**Checkpoint**: US3 tests pass, update endpoint functional.

---

## Phase 6: User Story 4 — Delete a Task (Priority: P4)

**Goal**: A user can permanently delete a task. Returns `204` on success, `404` if not found.

**Independent Test**: `DELETE /api/todo/1` returns `204`; subsequent `GET /api/todo/1` returns `404`.

### Tests for User Story 4 ⚠️ Write these FIRST

- [x] T041 [P] [US4] Write test: `DELETE /api/todo/{id}` with existing ID returns `204` with empty body in `src/test/java/com/example/todobackend/controller/DeleteTodoTest.java`
- [x] T042 [P] [US4] Write test: `DELETE /api/todo/{id}` followed by `GET /api/todo/{id}` returns `404` in `src/test/java/com/example/todobackend/controller/DeleteTodoTest.java`
- [x] T043 [P] [US4] Write test: `DELETE /api/todo/{id}` with unknown ID returns `404` in `src/test/java/com/example/todobackend/controller/DeleteTodoTest.java`

### Implementation for User Story 4

- [x] T018 [US4] Add `deleteTodo()` to `TodoService`
- [x] T019 [US4] Add `DELETE /api/todo/{id}` to `TodoController`

**Checkpoint**: US4 tests pass, delete endpoint functional.

---

## Phase 7: User Story 5 — Filter Tasks (Priority: P5)

**Goal**: `GET /api/todo` accepts optional `priority` and `completed` query parameters.

**Independent Test**: `GET /api/todo?priority=HIGH` returns only HIGH priority tasks.

### Tests for User Story 5 ⚠️ Write these FIRST

- [x] T044 [P] [US5] Write test: `GET /api/todo?priority=HIGH` returns only `HIGH` priority tasks in `src/test/java/com/example/todobackend/controller/FilterTodoTest.java`
- [x] T045 [P] [US5] Write test: `GET /api/todo?completed=false` returns only incomplete tasks in `src/test/java/com/example/todobackend/controller/FilterTodoTest.java`
- [x] T046 [P] [US5] Write test: `GET /api/todo?priority=HIGH&completed=false` returns only matching tasks in `src/test/java/com/example/todobackend/controller/FilterTodoTest.java`
- [x] T047 [P] [US5] Write test: `GET /api/todo?priority=INVALID` returns `400` in `src/test/java/com/example/todobackend/controller/FilterTodoTest.java`

### Implementation for User Story 5

- [x] T020 [US5] Add filter query methods to `TodoRepository`
- [x] T021 [US5] Add `getTodos()` filter method to `TodoService`
- [x] T022 [US5] Add query params to `GET /api/todo` in `TodoController`

**Checkpoint**: US5 tests pass, filtering functional.

---

## Phase 8: Polish

- [x] T023 Create `README.md`
- [x] T024 [P] Run smoke test from `specs/001-todo-task-management/quickstart.md`
- [x] T025 [P] Verify CORS headers include `Access-Control-Allow-Origin` for `localhost:4200`

---

## Dependencies & Execution Order

- T026, T027 must complete before any test tasks (T028–T047)
- Test tasks within each story are independent [P] — write them all before implementing
- Implementation tasks are already complete [x] — tests validate what was built

## Parallel Opportunities

```bash
# All test tasks within a story can be written in parallel:
T028, T029, T030, T031, T032  ← US1 tests (all in CreateTodoTest.java)
T033, T034, T035, T036        ← US2 tests (all in ViewTodoTest.java)
T037, T038, T039, T040        ← US3 tests (all in UpdateTodoTest.java)
T041, T042, T043              ← US4 tests (all in DeleteTodoTest.java)
T044, T045, T046, T047        ← US5 tests (all in FilterTodoTest.java)
```

## Notes

- Tests use `@SpringBootTest` + `@AutoConfigureMockMvc` + H2 in-memory DB (T026, T027)
- Each test class uses `@Transactional` to reset DB state between tests
- Error response shape is fixed by the constitution — tests must assert `status`, `error`, `message` fields
- Implementation is already done — tests verify it matches the spec's acceptance scenarios
