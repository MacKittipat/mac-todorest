package com.mackittipat.mactodorest.jpa.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mackittipat.mactodorest.jpa.entity.Todo;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoRepoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepo todoRepo;

    @After
    public void clear(){
        todoRepo.deleteAll();
    }

    @Test
    public void testInsert() throws Exception {

        String title = "Title1";

        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDesc("Desc1");
        todo.setDueDate(LocalDateTime.now());
        mockMvc.perform(post("/todos").content(toJsonString(todo)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(title)));
    }

    @Test
    public void testUpdate() throws Exception {

        String title = "Title1";
        String title2 = "Title2";

        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDesc("Desc1");
        todo.setDueDate(LocalDateTime.now());
        mockMvc.perform(post("/todos").content(toJsonString(todo)))
                .andExpect(status().isCreated());

        todo.setTitle(title2);
        mockMvc.perform(put("/todos/1").content(toJsonString(todo)));

        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(title2)));

    }

    @Test
    public void testDelete() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Test Title");
        todo.setDesc("Test Desc");
        todo.setDueDate(LocalDateTime.now());
        mockMvc.perform(post("/todos").content(toJsonString(todo)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/todos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByTitle() throws Exception {
        String title = "Title1";

        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDesc("Desc1");
        todo.setDueDate(LocalDateTime.now());
        mockMvc.perform(post("/todos").content(toJsonString(todo)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("http://localhost:8080/todos/search/findByTitle?title=" + title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.todos[0].title", is(title)));
    }

    @Test
    public void testFindByDueDate() throws Exception {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String title = "Title1";

        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDesc("Desc1");
        todo.setDueDate(now);
        mockMvc.perform(post("/todos").content(toJsonString(todo)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("http://localhost:8080/todos/search/findByDueDate?dueDate=" + now.format(formatter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.todos[0].title", is(title)));
    }

    private static String toJsonString(Todo todo) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String result = objectMapper.writeValueAsString(todo);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}