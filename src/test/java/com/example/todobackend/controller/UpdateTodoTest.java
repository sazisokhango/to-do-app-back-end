package com.example.todobackend.controller;

import com.example.todobackend.entity.TodoItem;
import com.example.todobackend.enums.Priority;
import com.example.todobackend.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UpdateTodoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void updateTodo_validRequest_returns200WithUpdatedFields() throws Exception {
        TodoItem saved = saveTodo("Original title", Priority.LOW);

        mockMvc.perform(put("/api/todo/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Updated title", "priority": "HIGH"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    @Test
    void updateTodo_setCompleted_returns200WithCompletedTrue() throws Exception {
        TodoItem saved = saveTodo("Complete me", Priority.MEDIUM);

        mockMvc.perform(put("/api/todo/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Complete me", "completed": true}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void updateTodo_notFound_returns404() throws Exception {
        mockMvc.perform(put("/api/todo/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Doesn't exist"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void updateTodo_blankTitle_returns400() throws Exception {
        TodoItem saved = saveTodo("Will fail update", Priority.MEDIUM);

        mockMvc.perform(put("/api/todo/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": ""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    private TodoItem saveTodo(String title, Priority priority) {
        TodoItem item = new TodoItem();
        item.setTitle(title);
        item.setPriority(priority);
        return todoRepository.save(item);
    }
}
