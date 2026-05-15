package com.example.todobackend.service;

import com.example.todobackend.dto.TodoRequest;
import com.example.todobackend.dto.TodoResponse;
import com.example.todobackend.entity.TodoItem;
import com.example.todobackend.enums.Priority;
import com.example.todobackend.exception.TodoNotFoundException;
import com.example.todobackend.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoResponse createTodo(TodoRequest request) {
        TodoItem item = new TodoItem();
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setPriority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM);
        item.setDueDate(request.getDueDate());
        item.setCompleted(false);
        return toResponse(todoRepository.save(item));
    }

    public List<TodoResponse> getTodos(Priority priority, Boolean completed) {
        if (priority != null && completed != null) {
            return todoRepository.findByPriorityAndCompletedAndDeletedAtIsNull(priority, completed)
                    .stream().map(this::toResponse).toList();
        } else if (priority != null) {
            return todoRepository.findByPriorityAndDeletedAtIsNull(priority)
                    .stream().map(this::toResponse).toList();
        } else if (completed != null) {
            return todoRepository.findByCompletedAndDeletedAtIsNull(completed)
                    .stream().map(this::toResponse).toList();
        }
        return todoRepository.findAllByDeletedAtIsNull().stream().map(this::toResponse).toList();
    }

    public TodoResponse getTodoById(Long id) {
        return toResponse(todoRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new TodoNotFoundException(id)));
    }

    public TodoResponse updateTodo(Long id, TodoRequest request) {
        TodoItem item = todoRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setPriority(request.getPriority() != null ? request.getPriority() : item.getPriority());
        item.setDueDate(request.getDueDate());
        if (request.getCompleted() != null) {
            item.setCompleted(request.getCompleted());
        }
        return toResponse(todoRepository.save(item));
    }

    public void deleteTodo(Long id) {
        TodoItem item = todoRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        item.setDeletedAt(LocalDateTime.now());
        todoRepository.save(item);
    }

    public List<TodoResponse> getDeletedTodos() {
        return todoRepository.findAllByDeletedAtIsNotNull()
                .stream().map(this::toResponse).toList();
    }

    public TodoResponse restoreTodo(Long id) {
        TodoItem item = todoRepository.findByIdAndDeletedAtIsNotNull(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        item.setDeletedAt(null);
        return toResponse(todoRepository.save(item));
    }

    private TodoResponse toResponse(TodoItem item) {
        return new TodoResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.isCompleted(),
                item.getPriority(),
                item.getDueDate(),
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getDeletedAt()
        );
    }
}
