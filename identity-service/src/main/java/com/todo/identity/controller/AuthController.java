package com.todo.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.identity.dto.AuthRequest;
import com.todo.identity.dto.UserCredentialDto;
import com.todo.identity.dto.UserResponseDto;
import com.todo.identity.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserCredentialDto> addNewUser(@RequestBody UserCredentialDto user) {
        return new ResponseEntity<UserCredentialDto>(service.saveUser(user),HttpStatus.OK);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUserName());
        } else {
            throw new RuntimeException("invalid access");
        }
    }
    @PostMapping("/login")
    public UserResponseDto getUser(@RequestBody AuthRequest authRequest) {    	
    	
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        UserResponseDto user = new UserResponseDto();
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        if (authenticate.isAuthenticated()) {
            String token = service.generateToken(authRequest.getUserName());
        	user = new UserResponseDto().builder()
        			.name(userDetails.getUsername())
        			.token(token)
        			.build();

        } else {
            throw new RuntimeException("invalid access");
        }
		return user;
    } 
}