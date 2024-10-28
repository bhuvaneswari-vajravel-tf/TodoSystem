package com.todo.task.system.exception;


import lombok.Data;

@Data
public class TaskNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -7250712727440041037L;

	String message;
	
	public TaskNotFoundException() {}
	
	public TaskNotFoundException(String msg){
        super(msg);
        this.message=msg;
    }
}