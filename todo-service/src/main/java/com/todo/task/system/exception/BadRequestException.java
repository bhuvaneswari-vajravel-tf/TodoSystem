package com.todo.task.system.exception;

import lombok.Data;

@Data
public class BadRequestException extends RuntimeException{

	private static final long serialVersionUID = -7250712727440041037L;

	String message;
	
	public BadRequestException() {}
	
	public BadRequestException(String message){
        super(message);
        this.message=message;
    }
}