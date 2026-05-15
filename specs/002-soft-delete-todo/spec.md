# Feature Specification: Soft Delete Todo Item

**Feature Branch**: `feature/soft-delete-todo`

**Created**: 2026-05-15

**Status**: Draft

**Supersedes**: The assumption in `specs/001-todo-task-management/spec.md` that states
*"Soft-delete (archive) is out of scope; deletion is permanent."* — this spec replaces
that assumption.

**Input**: When a user deletes a task, it should be hidden rather than permanently removed,
and should be recoverable.

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 — Soft Delete a Task (Priority: P1)

A user wants to delete a task from their active list without permanently losing it.
The task disappears from view but can be brought back later if needed.

**Why this priority**: This is the core behaviour change. Without it, the remaining
stories have nothing to build on.

**Independent Test**: A user deletes a task. The task no longer appears in their
active task list, but it still exists and can be retrieved by a user who knows it
was deleted.

**Acceptance Scenarios**:

1. **Given** an active task exists, **When** a user deletes it, **Then** the task
   disappears from the active task list.

2. **Given** a task has been soft-deleted, **When** a user requests all active tasks,
   **Then** the deleted task is not included in the results.

3. **Given** a task has been soft-deleted, **When** a user requests that task by its
   identifier, **Then** they receive a "not found" response (it is not visible as
   an active item).

4. **Given** no task exists with a given identifier, **When** a user attempts to
   delete it, **Then** they receive a clear "not found" error.

---

### User Story 2 — View Deleted Tasks (Priority: P2)

A user wants to see the tasks they have previously deleted, so they can decide
whether to restore or permanently remove them.

**Why this priority**: Without being able to see deleted tasks, restore (US3) has
no meaningful entry point.

**Independent Test**: After soft-deleting a task, a user can retrieve a list of
deleted tasks and see the deleted task in it.

**Acceptance Scenarios**:

1. **Given** one or more tasks have been soft-deleted, **When** a user requests
   the deleted task list, **Then** they receive a list of all soft-deleted tasks.

2. **Given** no tasks have been soft-deleted, **When** a user requests the deleted
   task list, **Then** they receive an empty list (not an error).

3. **Given** a mix of active and deleted tasks exist, **When** a user requests the
   deleted task list, **Then** only deleted tasks are returned — active tasks are
   not included.

---

### User Story 3 — Restore a Deleted Task (Priority: P3)

A user wants to recover a previously deleted task and return it to their active list.

**Why this priority**: Restore is the key value of soft delete over hard delete.
It is only meaningful once US1 and US2 are in place.

**Independent Test**: A user restores a soft-deleted task. The task reappears in
the active task list and no longer appears in the deleted task list.

**Acceptance Scenarios**:

1. **Given** a task has been soft-deleted, **When** a user restores it, **Then**
   the task reappears in the active task list with all its original data intact.

2. **Given** a task has been restored, **When** a user requests all active tasks,
   **Then** the restored task is included.

3. **Given** a task has been restored, **When** a user requests the deleted task
   list, **Then** the restored task is no longer included.

4. **Given** no task exists or the task is already active, **When** a user attempts
   to restore it, **Then** they receive a clear "not found" error.

---

### Edge Cases

- Filtering (`?priority=HIGH`, `?completed=false`) must continue to work correctly
  and must never surface soft-deleted tasks.
- A soft-deleted task's data (title, priority, due date, timestamps) MUST be fully
  preserved so it is identical when restored.
- Updating or completing a soft-deleted task MUST NOT be possible — it must be
  restored first.

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST hide a deleted task from the active task list without
  permanently removing it.
- **FR-002**: The system MUST record the date and time when a task was deleted.
- **FR-003**: The system MUST exclude soft-deleted tasks from all active task
  responses (list, get by ID, filters).
- **FR-004**: The system MUST provide a way to retrieve the list of soft-deleted tasks.
- **FR-005**: The system MUST allow a soft-deleted task to be restored to active status.
- **FR-006**: The system MUST clear the deletion timestamp when a task is restored.
- **FR-007**: The system MUST return a "not found" error when a user attempts to
  update or view a soft-deleted task as if it were active.
- **FR-008**: The system MUST return a "not found" error when attempting to delete
  or restore a task identifier that does not exist.

### Key Entities

- **Task** (updated): Gains a `deletedAt` timestamp. When null, the task is active.
  When set, the task is soft-deleted.

---

## Success Criteria *(mandatory)*

- **SC-001**: A deleted task is never returned in any active task response, including
  filtered results.
- **SC-002**: A restored task is indistinguishable from a task that was never deleted —
  all original data is preserved.
- **SC-003**: 100% of operations that target a soft-deleted task (view, update, filter)
  treat it as non-existent from the user's perspective.
- **SC-004**: The deleted task list accurately reflects all and only soft-deleted tasks.

---

## Assumptions

- Permanently deleting a task (removing it from the database entirely) is out of scope
  for this feature — a future feature can address this.
- There is no expiry or auto-purge of soft-deleted tasks in this version.
- Authentication and user ownership are still out of scope — all users share the same
  task space.
- The existing delete endpoint behaviour changes — it now soft-deletes instead of
  hard-deletes. This is a breaking change for any client that relies on the old
  permanent delete behaviour.
