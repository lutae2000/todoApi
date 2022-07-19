package com.example.todoapi.todoController;

import com.example.todoapi.exception.TodoNotFoundException;
import com.example.todoapi.todoService.TodoService;
import com.example.todoapi.todoVo.Todo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
public class TodoController {

    @Autowired
    private TodoService todoService;

    @ApiOperation(value = "Retrieve all todo for a user by passing in his name", responseContainer = "List", produces = "application/json", response = Todo.class, consumes = "application/json")
    @GetMapping("/users/{name}/todos")
    public List<Todo> retrieveTodos(@PathVariable String name){
        return todoService.retrieveTodos(name);
    }

    @GetMapping("/users/{name}/todos/{id}")
    public EntityModel<Todo> retrieveTodo(@PathVariable String name, @PathVariable int id){
        Todo todo = todoService.retrieveTodo(id);

        if(todo == null){
            throw new TodoNotFoundException("Todo not found");
        }

        //HATEOS 구현부
        EntityModel<Todo> entityModel = EntityModel.of(todo);
        WebMvcLinkBuilder webMvcLinkBuilder = linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).retrieveTodos(name));
        entityModel.add(webMvcLinkBuilder.withRel("parent"));

        return entityModel;
    }


    @PostMapping("/users/{name}/todos")
    ResponseEntity<?> add(@PathVariable String name, @Valid @RequestBody Todo todo){
        Todo createTodo = todoService.addTodo(name, todo.getDesc(), todo.getTargetDate(), todo.isDone());

        if(createTodo == null){
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                    .path("/{id}")
                                                    .buildAndExpand(createTodo.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/users/{name}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String name, @PathVariable int id, @RequestBody Todo todo){
        todoService.updateTodo(todo);
        return new ResponseEntity<Todo>(todo, HttpStatus.OK);
    }

    @DeleteMapping("/users/{name}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String name, @PathVariable int id){
        Todo todo = todoService.deleteById(id);

        if(todo != null)
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/test")
    public String test(){
        return "hello world";
    }



}
