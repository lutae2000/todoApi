package com.example.todoapi.exception;

public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException(String msg){
        super(msg);
    }
}
