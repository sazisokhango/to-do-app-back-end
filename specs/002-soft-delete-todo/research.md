# Research: Soft Delete Todo Item

**Feature**: 002-soft-delete-todo
**Date**: 2026-05-15

---

## Decision 1: Soft Delete Field

**Decision**: Add `deletedAt` (`LocalDateTime`, nullable) to `TodoItem` entity.

**Rationale**: A nullable timestamp is the standard soft-delete pattern — `null` means
active, a value means soft-deleted. It preserves the deletion time for auditability
and requires no schema migration beyond adding a single nullable column.

**Alternatives considered**:
- Boolean `deleted` flag: simpler but loses deletion timestamp. Rejected — timestamp
  is more informative and the spec requires recording when deletion occurred (FR-002).
- Separate `deleted_items` table: over-engineering for this scope. Rejected.

---

## Decision 2: Filtering Active vs Deleted Items

**Decision**: Replace existing repository query methods with `deletedAtIsNull`
variants for all active item queries, and add `findAllByDeletedAtIsNotNull()` for
the deleted list.

**Rationale**: Spring Data JPA derived query methods keep the implementation within
the existing repository pattern — no new abstraction layers required. This satisfies
the constitution's complexity constraint. The existing four query methods become four
new methods with `AndDeletedAtIsNull` appended.

**Alternatives considered**:
- Hibernate `@Where(clause = "deleted_at IS NULL")` on the entity: automatically
  filters all queries but is a Hibernate-specific annotation (deprecated in Hibernate 6
  used by Spring Boot 3.x). Rejected — too fragile.
- `@SQLRestriction` (Hibernate 6 replacement for `@Where`): applies globally to all
  queries, which would make the "get deleted list" query impossible without a workaround.
  Rejected — conflicts with US2.
- Spring Data JPA Specifications: adds an abstraction layer not justified for two
  filter states. Rejected per constitution.

---

## Decision 3: New Endpoints

**Decision**:
- `GET /api/todo/deleted` — returns all soft-deleted tasks (200)
- `PATCH /api/todo/{id}/restore` — restores a soft-deleted task (200)

**Rationale**: `GET /api/todo/deleted` is a resource-oriented path consistent with
the existing `/api/todo` base. `PATCH` is semantically correct for a partial update
(changing only `deletedAt` to null). Both follow Principle I of the constitution.

Note: Spring MVC resolves literal path `/api/todo/deleted` before the path variable
`/api/todo/{id}` — no routing conflict.

**Alternatives considered**:
- `PUT /api/todo/{id}/restore`: semantically implies a full replacement. Rejected.
- Query parameter: `DELETE /api/todo/{id}?soft=true`: mixes concerns. Rejected.

---

## Decision 4: Breaking Change to DELETE Endpoint

**Decision**: `DELETE /api/todo/{id}` now sets `deletedAt` instead of calling
`deleteById`. The HTTP contract (204 on success, 404 on not found) is unchanged.

**Rationale**: The response shape and status codes do not change — only the internal
behaviour. Clients that call `DELETE /api/todo/{id}` and check for `204` will
continue to work. The spec documents this as a breaking change only for clients that
verified permanent removal by confirming the row is gone from the database.

---

## Decision 5: TodoResponse for Deleted Items

**Decision**: Add `deletedAt` (`LocalDateTime`, nullable) to `TodoResponse`.

**Rationale**: Returning `deletedAt` in the response for deleted items allows the
front-end to display when the item was deleted. For active items it is `null`.
Adding it to `TodoResponse` keeps a single DTO for both active and deleted tasks.

---

## Resolved Unknowns

All technical unknowns resolved. No NEEDS CLARIFICATION markers remain.
