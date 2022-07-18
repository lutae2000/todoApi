package com.example.todoapi.todoTest;

import com.example.todoapi.todoController.TodoController;
import com.example.todoapi.todoService.TodoService;
import com.example.todoapi.todoVo.Todo;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private static final int CREATED_TODO_ID = 4;
    private static final int UPATED_TODO_ID = 4;

    @Test
    public void retrieveTodos() throws Exception{
        //Given
        List<Todo> mockList = Arrays.asList(
                new Todo(1, "Jack", "Learn Spring MVC", new Date(), false),
                new Todo(2, "Jack", "Learn struts", new Date(), false)
        );

        when(todoService.retrieveTodos(anyString())).thenReturn(mockList);

        //when
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/users/Jack/todos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
       //Then
       String expected = "[{id:1, user:Jack, desc:\"Learn Spring MVC\", isDone:false}," +
                                "{id:2, user:Jack, desc:\"Learn struts\", isDone:false}]";
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void retrieveTodo() throws Exception{
        Todo mockTodo = new Todo(1, "Jack", "Learn Spring MVC", new Date(), false);

        when(todoService.retrieveTodo(anyInt())).thenReturn(mockTodo);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/Jack/todos/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String expected = "{id:1, user:Jack, desc:\"Learn Spring MVC\", done:false}";
                JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void createTodo() throws Exception{
        Todo mockTodo = new Todo(CREATED_TODO_ID, "Jack", "Learn Spring MVC", new Date(), false);
        String todo = "{\"user\":\"Jack\", \"desc\":\"Learn Spring MVC\",\"done\":\"false\"}";

        when(todoService.addTodo(anyString(), anyString(), isNull(), anyBoolean())).thenReturn(mockTodo);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/Jack/todos")
                        .content(todo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/users/Jack/todos/" + CREATED_TODO_ID)));
    }

    @Test
    public void createTodo_withValidationError() throws Exception{
        Todo mockTodo = new Todo(CREATED_TODO_ID, "Jack", "Learn Spring MVC", new Date(), false);
        String todo = "{\"user\":\"Jack\", \"desc\":\"Learn\", \"done\":\"false\"}";

        when(todoService.addTodo(anyString(), anyString(), isNull(), anyBoolean())).thenReturn(mockTodo);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/users/Jack/todos")
                        .content(todo)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andReturn();
    }

    @Test
    public void updateTodo() throws Exception{
        Todo mockTodo = new Todo(UPATED_TODO_ID, "Jack", "Learn Spring MVC 5", new Date(), false);
        String todo = "{\"user\":\"Jack\", \"desc\":\"learn spring mvc 5\", \"done\":false}";

        when(todoService.updateTodo(mockTodo)).thenReturn(mockTodo);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.put("/users/Jack/todos/"+UPATED_TODO_ID)
                        .content(todo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
            JSONAssert.assertEquals(todo, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void deleteTodo() throws Exception{
        Todo mockTodo = new Todo(1, "Jack", "Learn Spring MVC", new Date(), false);
        when(todoService.deleteById(anyInt())).thenReturn(mockTodo);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/Jack/todos/" + mockTodo.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    public void deleteTodo_error() throws Exception{
        when(todoService.deleteById(anyInt())).thenReturn(null);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/Jack/todos/1")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }
}
