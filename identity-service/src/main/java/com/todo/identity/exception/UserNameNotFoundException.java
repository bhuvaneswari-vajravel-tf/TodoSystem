package com.todo.identity.exception;


import lombok.Data;

@Data
public class UserNameNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -7250712727440041037L;

	String message;
	
	public UserNameNotFoundException() {}
	
	public UserNameNotFoundException(String msg){
        super(msg);
        this.message=msg;
    }
}