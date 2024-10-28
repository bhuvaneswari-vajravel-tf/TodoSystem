package com.todo.identity.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todo.identity.dto.UserCredentialDto;
import com.todo.identity.entity.UserCredential;
import com.todo.identity.exception.BadRequestException;
import com.todo.identity.exception.DuplicateUserNameException;
import com.todo.identity.repo.UserCredentialRepository;

@Service
public class AuthService {

	@Autowired
	private UserCredentialRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	public UserCredentialDto saveUser(UserCredentialDto user) {
		UserCredentialDto userDto = new UserCredentialDto();
		if (validateUser(user)) {
			Optional<UserCredential> userCred = repository.findByNameIgnoreCase(user.getName());

			userCred.ifPresent(entity -> {
				throw new DuplicateUserNameException("User name " + user.getName() + " already exist");
			});
			UserCredential credential = UserCredential.builder().email(user.getEmail()).name(user.getName())
					.password(user.getPassword()).build();
			credential.setPassword(passwordEncoder.encode(credential.getPassword()));
			UserCredential userEntity= repository.save(credential);
			userDto =UserCredentialDto.builder()
					.id(userEntity.getId())
					.email(userEntity.getEmail())
					.name(userEntity.getName())
					.build();
		}
		return userDto;
	}

	private boolean validateUser(UserCredentialDto credential) {

		if (credential.getName() == null || credential.getName().isBlank())
			throw new BadRequestException("User name is required");
		if (credential.getPassword() == null || credential.getPassword().isBlank())
			throw new BadRequestException("Password is required");
		return true;
	}

	public String generateToken(String username) {
		return jwtService.generateToken(username);
	}

}