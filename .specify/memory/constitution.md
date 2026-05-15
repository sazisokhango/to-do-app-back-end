<!--
SYNC IMPACT REPORT
==================
Version change: (new) → 1.0.0
Added sections:
  - Core Principles (I–V)
  - Technology Stack
  - API & Development Standards
  - Governance
Modified principles: N/A (initial ratification)
Removed sections: N/A
Templates requiring updates:
  - .specify/templates/plan-template.md ✅ aligned (Spring Boot / PostgreSQL context reflected)
  - .specify/templates/spec-template.md ✅ aligned (no mandatory section changes required)
  - .specify/templates/tasks-template.md ✅ aligned (task categories reflect layered architecture)
Follow-up TODOs: none — all placeholders resolved
-->

# To-Do Back End Constitution

## Core Principles

### I. REST API First

Every feature MUST be exposed exclusively through a RESTful HTTP API. HTTP methods
and status codes are non-negotiable:

- `POST   /api/todo`      → `201 Created`
- `GET    /api/todo`      → `200 OK`
- `GET    /api/todo/{id}` → `200 OK`
- `PUT    /api/todo/{id}` → `200 OK`
- `DELETE /api/todo/{id}` → `204 No Content`
- Item not found          → `404 Not Found`
- Invalid request body    → `400 Bad Request`

All error responses MUST follow this exact JSON shape:

```json
{ "status": 404, "error": "Not Found", "message": "Could not find the item" }
```

No endpoint MAY return a different error structure.

### II. Strict Input Validation

All incoming data MUST be validated before it reaches the service layer:

- `title` MUST be present and MUST NOT exceed 255 characters.
- `priority` MUST default to `MEDIUM` when omitted; valid values are `LOW`, `MEDIUM`, `HIGH`.
- `dueDate` MUST NOT be a date in the past.

Validation failures MUST produce a `400 Bad Request` response. No invalid data may
reach the database.

### III. Layered Architecture (NON-NEGOTIABLE)

The codebase MUST follow a strict three-layer separation:

```
Controller  →  Service  →  Repository
```

- **Controller**: HTTP boundary only — maps requests/responses, delegates to service.
- **Service**: Business logic, validation orchestration, exception handling.
- **Repository**: Data access only — no business logic permitted.

DTOs (`TodoRequest`, `TodoResponse`) MUST be used at the API boundary. The `TodoItem`
entity MUST NOT be exposed directly in controller responses.

### IV. Consistent Error Handling

A single, centralised exception handler (e.g. `@ControllerAdvice`) MUST intercept all
unhandled exceptions and translate them into the standard error JSON shape. No
controller method MAY return raw exception messages or stack traces to the client.

### V. Frontend Compatibility

CORS MUST be configured to allow requests from `http://localhost:4200`. No other
cross-origin policy change may be introduced without amending this constitution.

## Technology Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.x
- **Build Tool**: Maven
- **Database**: PostgreSQL
  - Database name: `to_do`
  - Username: `to_do_user`
  - Password: `password`
- **Base package**: `com.example.todobackend`

No alternative language, framework, or database may be substituted without a
constitution amendment.

## API & Development Standards

- Base path: `/api/todo`
- All date-time fields serialised as ISO-8601 (`LocalDateTime` / `LocalDate`).
- The `Priority` enum values are exactly: `LOW`, `MEDIUM`, `HIGH`.
- Filtering on `GET /api/todo` MUST support query parameters `priority` and `completed`.
- A `README.md` MUST be maintained at the project root explaining how to run the application.
- Complexity MUST be justified. Any pattern beyond Controller → Service → Repository
  (e.g., additional abstraction layers, event systems) requires explicit documentation
  in the plan.

## Governance

This constitution supersedes all other development practices for this project.
Amendments require:

1. A documented reason for the change.
2. An updated version number following semantic versioning:
   - **MAJOR**: removal or redefinition of a principle.
   - **MINOR**: new principle or section added.
   - **PATCH**: clarifications or wording fixes.
3. `LAST_AMENDED_DATE` updated to the date of the amendment.

All implementation plans, task lists, and code reviews MUST verify compliance with
these principles before proceeding. Refer to `specs/PRD.md` for the originating
product requirements.

**Version**: 1.0.0 | **Ratified**: 2026-05-15 | **Last Amended**: 2026-05-15
