package com.example.todoapi.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResponse {
    private Date timestamp = new Date();
    private String message;
    private String details;

    public ExceptionResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }
}
