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
class ViewTodoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void getAllTodos_withTasks_returns200WithList() throws Exception {
        saveTodo("Task one", Priority.HIGH);
        saveTodo("Task two", Priority.LOW);

        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllTodos_noTasks_returns200WithEmptyList() throws Exception {
        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getTodoById_exists_returns200WithTask() throws Exception {
        TodoItem saved = saveTodo("Find me", Priority.MEDIUM);

        mockMvc.perform(get("/api/todo/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.title").value("Find me"));
    }

    @Test
    void getTodoById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/todo/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists());
    }

    private TodoItem saveTodo(String title, Priority priority) {
        TodoItem item = new TodoItem();
        item.setTitle(title);
        item.setPriority(priority);
        return todoRepository.save(item);
    }
}
