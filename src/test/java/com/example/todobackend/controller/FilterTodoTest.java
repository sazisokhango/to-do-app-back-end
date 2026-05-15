package com.example.todobackend.controller;

import com.example.todobackend.entity.TodoItem;
import com.example.todobackend.enums.Priority;
import com.example.todobackend.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FilterTodoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
        saveTodo("High incomplete", Priority.HIGH, false);
        saveTodo("High complete",   Priority.HIGH, true);
        saveTodo("Medium incomplete", Priority.MEDIUM, false);
        saveTodo("Low complete",    Priority.LOW,  true);
    }

    @Test
    void filterByPriority_returnsOnlyMatchingPriority() throws Exception {
        mockMvc.perform(get("/api/todo").param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].priority").value("HIGH"))
                .andExpect(jsonPath("$[1].priority").value("HIGH"));
    }

    @Test
    void filterByCompleted_returnsOnlyIncompleteTasks() throws Exception {
        mockMvc.perform(get("/api/todo").param("completed", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[1].completed").value(false));
    }

    @Test
    void filterByPriorityAndCompleted_returnsOnlyMatchingTasks() throws Exception {
        mockMvc.perform(get("/api/todo")
                        .param("priority", "HIGH")
                        .param("completed", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("High incomplete"));
    }

    @Test
    void filterByInvalidPriority_returns400() throws Exception {
        mockMvc.perform(get("/api/todo").param("priority", "URGENT"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    private void saveTodo(String title, Priority priority, boolean completed) {
        TodoItem item = new TodoItem();
        item.setTitle(title);
        item.setPriority(priority);
        item.setCompleted(completed);
        todoRepository.save(item);
    }
}
