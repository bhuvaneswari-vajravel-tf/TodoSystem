package com.todo.identity.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todo.identity.entity.UserCredential;

public interface UserCredentialRepository  extends JpaRepository<UserCredential,Integer> {
    Optional<UserCredential> findByNameIgnoreCase(String username);
}