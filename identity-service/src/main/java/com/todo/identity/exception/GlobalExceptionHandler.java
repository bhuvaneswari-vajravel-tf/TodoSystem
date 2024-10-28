package com.todo.identity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DuplicateUserNameException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handleDuplicatUserException(DuplicateUserNameException ex) {
    	
    	ErrorResponse response = ErrorResponse.builder().code(HttpStatus.CONFLICT.value()).message(ex.getMessage()).timeStamp(System.currentTimeMillis()).build();
    	
        return new ResponseEntity<ErrorResponse>(response,HttpStatus.CONFLICT);
    }
    

    @ExceptionHandler(value = BadRequestException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
    	
    	ErrorResponse response = ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.value()).message(ex.getMessage()).timeStamp(System.currentTimeMillis()).build();
    	
        return new ResponseEntity<ErrorResponse>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNameNotFoundException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNameNotFoundException ex) {
    	
    	ErrorResponse response = ErrorResponse.builder().code(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).timeStamp(System.currentTimeMillis()).build();
    	
        return new ResponseEntity<ErrorResponse>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handleBadCredException(Exception ex) {
    	ErrorResponse response = ErrorResponse.builder().code(HttpStatus.UNAUTHORIZED.value()).message(ex.getMessage()).timeStamp(System.currentTimeMillis()).build();
    	
        return new ResponseEntity<ErrorResponse>(response,HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(value = Exception.class)
    public @ResponseBody ResponseEntity<ErrorResponse> handleException(Exception ex) {
    	ErrorResponse response = ErrorResponse.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ex.getMessage()).timeStamp(System.currentTimeMillis()).build();
    	
        return new ResponseEntity<ErrorResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}

