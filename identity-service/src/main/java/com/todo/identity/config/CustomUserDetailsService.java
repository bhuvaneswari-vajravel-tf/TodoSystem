package com.todo.identity.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.todo.identity.entity.UserCredential;
import com.todo.identity.repo.UserCredentialRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> credential = repository.findByNameIgnoreCase(username);
        return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found with name :" + username));
    }
}