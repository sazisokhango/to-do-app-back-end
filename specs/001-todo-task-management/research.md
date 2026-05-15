# Research: To-Do Task Management

**Feature**: 001-todo-task-management
**Date**: 2026-05-15

---

## Decision 1: Data Access Strategy

**Decision**: Spring Data JPA with `JpaRepository`

**Rationale**: Spring Data JPA integrates directly with PostgreSQL via Hibernate,
eliminates boilerplate CRUD code, and provides derived query methods for filtering
(e.g. `findByPriority`, `findByCompleted`, `findByPriorityAndCompleted`). This
satisfies the Repository layer requirement from the constitution with minimal
complexity.

**Alternatives considered**:
- JDBC Template: More control but significantly more boilerplate. Not justified for
  this scope.
- jOOQ: Type-safe SQL DSL but adds complexity. Not justified.

---

## Decision 2: Input Validation

**Decision**: Jakarta Bean Validation (`jakarta.validation`) annotations on the
`TodoRequest` DTO, triggered by `@Valid` in the controller.

**Rationale**: Spring Boot includes the Hibernate Validator implementation by
default. Annotations (`@NotBlank`, `@Size`, `@FutureOrPresent`) map directly to
all validation rules in the constitution. Validation fires before the service layer
is invoked, satisfying Principle II.

**Alternatives considered**:
- Manual validation in service layer: Violates the principle that validation must
  stop invalid data before it reaches the service. Rejected.

---

## Decision 3: Filtering Strategy

**Decision**: Spring Data JPA derived query methods in the repository with service-level
dispatch based on which filter parameters are present.

**Rationale**: The constitution requires no complexity beyond Controller → Service →
Repository. Derived query methods (`findByPriority`, `findByCompleted`,
`findByPriorityAndCompleted`, `findAll`) cover all four filter combinations without
additional abstractions. A Specification or Criteria API pattern would be
over-engineering for two filter fields.

**Alternatives considered**:
- `JpaSpecificationExecutor`: Flexible but adds an abstraction layer the constitution
  requires to be justified. Rejected for this scope.

---

## Decision 4: Centralised Error Handling

**Decision**: `@RestControllerAdvice` class with `@ExceptionHandler` methods for
`TodoNotFoundException` (→ 404) and `MethodArgumentNotValidException` (→ 400).

**Rationale**: Spring's `@RestControllerAdvice` intercepts all unhandled exceptions
across all controllers. A custom `TodoNotFoundException` (extending
`RuntimeException`) provides a clean signal from the service layer. This satisfies
Principle IV exactly.

**Alternatives considered**:
- `ResponseStatusException` thrown directly from controllers: Bypasses the
  centralised handler. Rejected.

---

## Decision 5: CORS Configuration

**Decision**: `WebMvcConfigurer` bean in a `CorsConfig` class, allowing
`http://localhost:4200` on all `/api/**` endpoints.

**Rationale**: Programmatic CORS configuration via `WebMvcConfigurer` is the
idiomatic Spring Boot approach and gives full control over allowed origins, methods,
and headers. Satisfies Principle V.

**Alternatives considered**:
- `@CrossOrigin` annotation per controller: Scatters configuration. Rejected in
  favour of centralised config.

---

## Resolved Unknowns

All technical unknowns resolved. No NEEDS CLARIFICATION markers remain.
