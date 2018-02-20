package com.mackittipat.mactodorest.jpa.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mackittipat.mactodorest.jpa.entity.Todo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = false)
public class TodoRepoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepo todoRepo;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Before
    public void clear(){
        todoRepo.deleteAll();
    }

    @Test
    public void testInsert() throws Exception {

        Todo todo = new Todo();
        todo.setTitle("Title1");
        todo.setDesc("Desc1");
        todo.setDueDate(LocalDateTime.now());

        List<Todo> todoResultList = (List<Todo>) todoRepo.findAll();
        Assert.assertEquals(0, todoResultList.size());

        mockMvc.perform(post("/todos").content(toJsonString(todo)))
                .andExpect(status().isCreated());

        todoResultList = (List<Todo>) todoRepo.findAll();
        Assert.assertEquals(1, todoResultList.size());
    }

    @Test
    public void testUpdate() throws Exception {

        Todo todo = new Todo();
        todo.setTitle("Title1");
        todo.setDesc("Desc1");
        todo.setDueDate(LocalDateTime.now());

        todo = todoRepo.save(todo);

        todo.setTitle("Title2");
        mockMvc.perform(put("/todos/" + todo.getId()).content(toJsonString(todo)));

        Todo todoResult = todoRepo.findOne(todo.getId());
        Assert.assertEquals(todo.getTitle(), todoResult.getTitle());
    }

    @Test
    public void testDelete() throws Exception {

        Todo todo = new Todo();
        todo.setTitle("Title1");
        todo.setDesc("Desc1");
        todo.setDueDate(LocalDateTime.now());

        todo = todoRepo.save(todo);

        Todo todoResult = todoRepo.findOne(todo.getId());
        Assert.assertEquals(todo.getId(), todoResult.getId());

        mockMvc.perform(delete("/todos/" + todo.getId()))
                .andExpect(status().isNoContent());

        todoResult = todoRepo.findOne(todo.getId());
        Assert.assertNull(todoResult);
    }

    @Test
    public void testFindByTitle() throws Exception {

        Todo todo = new Todo();
        todo.setTitle("Title1");
        todo.setDesc("Desc1");
        todo.setDueDate(LocalDateTime.now());

        todo = todoRepo.save(todo);

        mockMvc.perform(get("/todos/search/findByTitle?title=" + todo.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.todos[0].title", is(todo.getTitle())))
                .andExpect(jsonPath("$._embedded.todos[0].desc", is(todo.getDesc())))
                .andExpect(jsonPath("$._embedded.todos[0].dueDate", is(todo.getDueDate().format(formatter))))
        ;
    }

    @Test
    public void testFindByDueDate() throws Exception {

        LocalDateTime now = LocalDateTime.now();

        Todo todo = new Todo();
        todo.setTitle("Title1");
        todo.setDesc("Desc1");
        todo.setDueDate(LocalDateTime.parse(now.format(formatter), formatter));

        todo = todoRepo.save(todo);

        mockMvc.perform(get("/todos/search/findByDueDate?dueDate=" + now.format(formatter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.todos[0].title", is(todo.getTitle())))
                .andExpect(jsonPath("$._embedded.todos[0].desc", is(todo.getDesc())))
                .andExpect(jsonPath("$._embedded.todos[0].dueDate", is(todo.getDueDate().format(formatter))))
        ;
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