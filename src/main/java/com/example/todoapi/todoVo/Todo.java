package com.example.todoapi.todoVo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class Todo {
    private int id;
    @NotNull
    private String user;
    @Size(min = 9, message = "Enter at least 10 charaters")
    private String desc;
    private Date targetDate;
    private boolean isDone;

    public Todo() {
    }

    public Todo(int id, String user, String desc, Date targetDate, boolean isDone) {
        super();
        this.id = id;
        this.user = user;
        this.desc = desc;
        this.targetDate = targetDate;
        this.isDone = isDone;
    }
}
