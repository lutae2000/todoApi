package com.example.todoapi.todoService;


import com.example.todoapi.exception.TodoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.todoapi.todoVo.Todo;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


@Service
@Slf4j
public class TodoService {
    private static List<Todo> todos = new ArrayList<Todo>();
    private static int todoCount = 3;

    static {
        todos.add(new Todo(1, "Jack", "Learn Spring MVC", new Date(), false));
        todos.add(new Todo(2, "Jack", "Learn Struts", new Date(), false));
        todos.add(new Todo(3, "Jack", "Learn Hibernate", new Date(), false));
    }

    public List<Todo> retrieveTodos(String user){
        //주어진 사용자에 대한 모든 todo 반환
        List<Todo> todoList = new ArrayList<Todo>();
        for(Todo todo : todos){
            if(todo.getUser().equals(user))
                todoList.add(todo);
        }
        return todoList;
    }

    public Todo addTodo(String name, String desc, Date targetDate, boolean isDone){
        //todo를 추가하기위한 로직
        Todo todo = new Todo(todos.size()+1, name, desc, targetDate, isDone);
        todos.add(todo);
        return todo;
    }

    public Todo retrieveTodo(int id){
        //todo 가져오기
        for(Todo todo : todos){
            if(todo.getId() == id){
                return todo;
            }
        }
        return null;
    }

    public void updateTodo(Todo todo){
        todos.remove(todo);
        todos.add(todo);
    }

    public Todo deleteById(int id){
        //todo 삭제
        Todo todo = retrieveTodo(id);

        if(todo == null)
            throw new TodoNotFoundException("not found data");

        if(todos.remove(todo))
            return todo;

        throw new RuntimeException("Delete failed");
    }
}
