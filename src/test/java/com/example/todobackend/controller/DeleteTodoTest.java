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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeleteTodoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void deleteTodo_exists_returns204() throws Exception {
        TodoItem saved = saveTodo("Delete me", Priority.LOW);

        mockMvc.perform(delete("/api/todo/{id}", saved.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void deleteTodo_thenGet_returns404() throws Exception {
        TodoItem saved = saveTodo("Gone soon", Priority.LOW);

        mockMvc.perform(delete("/api/todo/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/todo/{id}", saved.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteTodo_notFound_returns404() throws Exception {
        mockMvc.perform(delete("/api/todo/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    private TodoItem saveTodo(String title, Priority priority) {
        TodoItem item = new TodoItem();
        item.setTitle(title);
        item.setPriority(priority);
        return todoRepository.save(item);
    }
}
