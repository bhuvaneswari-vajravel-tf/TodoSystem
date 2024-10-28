package com.todo.task.system.exception;

import lombok.Data;

@Data
public class DuplicateTaskNameException extends RuntimeException{

	private static final long serialVersionUID = 311863722138676077L;
	
	private String message;

	    public DuplicateTaskNameException() {}

	    public DuplicateTaskNameException(String msg) {
	        super(msg);
	        this.message = msg;
	    }
}