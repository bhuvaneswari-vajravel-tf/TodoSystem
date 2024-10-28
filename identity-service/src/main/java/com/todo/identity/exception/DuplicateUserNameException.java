package com.todo.identity.exception;

import lombok.Data;

@Data
public class DuplicateUserNameException extends RuntimeException{

	private static final long serialVersionUID = 311863722138676077L;
	
	private String message;

	    public DuplicateUserNameException() {}

	    public DuplicateUserNameException(String msg) {
	        super(msg);
	        this.message = msg;
	    }
}