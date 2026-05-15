package com.example.todobackend.repository;

import com.example.todobackend.entity.TodoItem;
import com.example.todobackend.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {

    // Active item queries (deletedAt IS NULL)
    List<TodoItem> findAllByDeletedAtIsNull();
    List<TodoItem> findByPriorityAndDeletedAtIsNull(Priority priority);
    List<TodoItem> findByCompletedAndDeletedAtIsNull(boolean completed);
    List<TodoItem> findByPriorityAndCompletedAndDeletedAtIsNull(Priority priority, boolean completed);
    Optional<TodoItem> findByIdAndDeletedAtIsNull(Long id);

    // Deleted item queries (deletedAt IS NOT NULL)
    List<TodoItem> findAllByDeletedAtIsNotNull();
    Optional<TodoItem> findByIdAndDeletedAtIsNotNull(Long id);
}
