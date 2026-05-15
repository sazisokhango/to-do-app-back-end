# Quickstart: Soft Delete Feature

## Soft Delete a Task

```bash
# Delete (soft) a task
curl -X DELETE http://localhost:8080/api/todo/1
# Returns 204 — task is hidden, not gone

# Confirm it's gone from active list
curl http://localhost:8080/api/todo
# Task 1 no longer appears
```

## View Deleted Tasks

```bash
curl http://localhost:8080/api/todo/deleted
# Returns array of soft-deleted tasks with deletedAt timestamp
```

## Restore a Task

```bash
curl -X PATCH http://localhost:8080/api/todo/1/restore
# Returns 200 with the restored task (deletedAt = null)

# Confirm it's back in the active list
curl http://localhost:8080/api/todo
# Task 1 reappears
```

## Error Cases

```bash
# Attempt to GET a soft-deleted item
curl http://localhost:8080/api/todo/1
# Returns 404

# Attempt to restore an active (non-deleted) item
curl -X PATCH http://localhost:8080/api/todo/1/restore
# Returns 404
```
