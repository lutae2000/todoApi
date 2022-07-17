package com.example.todoapi.todoService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.todoapi.todoVo.Todo;


import java.util.ArrayList;
import java.util.Date;
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
        return todos;
    }

    public Todo addTodo(String name, String desc, Date targetDate, boolean isDone){
        //todo를 추가하기위한 로직
        return null;
    }

    public Todo retrieveTodo(int id){
        //todo 업데이트
        return todos.get(id);
    }

    public Todo deleteById(int id){
        //todo 삭제
        return todos.remove(id);
    }
}
