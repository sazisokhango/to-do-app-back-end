# Implementation Plan: To-Do Task Management

**Branch**: `feature/speckit-constitution` | **Date**: 2026-05-15 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/001-todo-task-management/spec.md`

---

## Summary

Build a RESTful To-Do Task Management API that allows users to create, view, update,
delete, and filter tasks. The system enforces strict input validation, returns
structured error responses, and is accessible from a local Angular front-end. Built
with Spring Boot 3.5.x, Java 21, PostgreSQL, and Maven.

---

## Technical Context

**Language/Version**: Java 21

**Primary Dependencies**:
- `spring-boot-starter-web` — REST controllers
- `spring-boot-starter-data-jpa` — JPA / Hibernate ORM
- `spring-boot-starter-validation` — Jakarta Bean Validation
- `postgresql` — JDBC driver

**Storage**: PostgreSQL — database `to_do`, user `to_do_user`

**Testing**: JUnit 5 via `spring-boot-starter-test`

**Target Platform**: Local development server (Linux)

**Project Type**: REST web service

**Performance Goals**: All CRUD operations respond in under 1 second under local load

**Constraints**: CORS restricted to `http://localhost:4200`

**Scale/Scope**: Single-user, local development context

---

## Constitution Check

*GATE: Must pass before implementation begins. Re-checked after Phase 1 design.*

| Principle | Check | Status |
|-----------|-------|--------|
| I. REST API First | All 5 endpoints use correct HTTP methods and status codes per constitution | ✅ PASS |
| II. Strict Input Validation | `@Valid` on `TodoRequest` DTO; `@NotBlank`, `@Size(max=255)`, `@FutureOrPresent` annotations; validation fires in controller before service | ✅ PASS |
| III. Layered Architecture | `TodoController` → `TodoService` → `TodoRepository`. DTOs used at API boundary. Entity never returned directly. | ✅ PASS |
| IV. Consistent Error Handling | `@RestControllerAdvice` (`GlobalExceptionHandler`) handles `TodoNotFoundException` (404) and `MethodArgumentNotValidException` (400) | ✅ PASS |
| V. Frontend Compatibility | `CorsConfig` (implements `WebMvcConfigurer`) allows `http://localhost:4200` on all `/api/**` paths | ✅ PASS |

All gates pass. Implementation may proceed.

---

## Project Structure

### Documentation (this feature)

```text
specs/001-todo-task-management/
├── plan.md              ← This file
├── research.md          ← Phase 0 output
├── data-model.md        ← Phase 1 output
├── quickstart.md        ← Phase 1 output
├── contracts/
│   ├── POST-api-todo.md
│   ├── GET-api-todo.md
│   ├── GET-api-todo-id.md
│   ├── PUT-api-todo-id.md
│   └── DELETE-api-todo-id.md
└── tasks.md             ← Phase 2 output (/speckit-tasks)
```

### Source Code

```text
src/main/java/com/example/todobackend/
├── controller/
│   └── TodoController.java         ← REST endpoints, delegates to service
├── service/
│   └── TodoService.java            ← Business logic, validation, mapping
├── repository/
│   └── TodoRepository.java         ← JpaRepository + derived query methods
├── entity/
│   └── TodoItem.java               ← JPA entity, mapped to todo_items table
├── dto/
│   ├── TodoRequest.java            ← Inbound DTO with validation annotations
│   └── TodoResponse.java           ← Outbound DTO
├── enums/
│   └── Priority.java               ← LOW | MEDIUM | HIGH
├── exception/
│   ├── TodoNotFoundException.java  ← Thrown by service when item not found
│   └── GlobalExceptionHandler.java ← @RestControllerAdvice, error shaping
└── config/
    └── CorsConfig.java             ← WebMvcConfigurer, allows localhost:4200

src/main/resources/
└── application.properties          ← DB config, JPA settings

README.md                           ← Required by constitution
```

**Structure Decision**: Single-project layout. No sub-modules needed for this scope.

---

## Complexity Tracking

No constitution violations. No additional abstraction layers introduced.
All decisions documented in `research.md`.
