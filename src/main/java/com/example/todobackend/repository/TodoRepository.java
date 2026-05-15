package com.example.todobackend.repository;

import com.example.todobackend.entity.TodoItem;
import com.example.todobackend.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByPriority(Priority priority);
    List<TodoItem> findByCompleted(boolean completed);
    List<TodoItem> findByPriorityAndCompleted(Priority priority, boolean completed);
}
