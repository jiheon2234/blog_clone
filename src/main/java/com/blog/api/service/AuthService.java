package com.blog.api.service;

import org.springframework.stereotype.Service;

import com.blog.api.crypto.PasswordEncoder;
import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsEmailException;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Signup;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder encoder;



	public void signup(Signup signup) {
		userRepository.findByEmail(signup.getEmail())
			.ifPresent(u -> {
				throw new AlreadyExistsEmailException();
			});

		String encryptedPassword = encoder.encrpyt(signup.getPassword());

		var user = User.builder()
			.name(signup.getName())
			.password(encryptedPassword)
			.email(signup.getEmail())
			.build();

		userRepository.save(user);
	}
}
