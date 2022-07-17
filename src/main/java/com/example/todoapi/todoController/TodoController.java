package com.example.todoapi.todoController;

import com.example.todoapi.todoService.TodoService;
import com.example.todoapi.todoVo.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/users/{name}/todos")
    public List<Todo> retrieveTodos(@PathVariable String name){
        System.out.println(name);
        return todoService.retrieveTodos(name);
    }

    @GetMapping("/users/{name}/todos/{id}")
    public Todo retrieveTodo(@PathVariable String name, @PathVariable int id){
        return todoService.retrieveTodo(id);
    }

    @PostMapping("/users/{name}/todos")
    ResponseEntity<?> add(@PathVariable String name, @RequestBody Todo todo){
        Todo createTodo = todoService.addTodo(name, todo.getDesc(), todo.getTargetDate(), todo.isDone());

        if(createTodo == null){
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                    .path("/{id}")
                                                    .buildAndExpand(createTodo.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/test")
    public String test(){
        return "hello world";
    }

}
