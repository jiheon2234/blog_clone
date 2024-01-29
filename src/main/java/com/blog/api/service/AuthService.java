package com.blog.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.api.crypto.PasswordEncoder;
import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsEmailException;
import com.blog.api.exception.InvalidSigninInformation;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Login;
import com.blog.api.request.Signup;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder encoder;

	@Transactional
	public Long signIn(Login login) {

		User user = userRepository.findByEmail(login.getEmail())
			.orElseThrow(InvalidSigninInformation::new);

		var matches = encoder.matches(login.getPassword(), user.getPassword());
		if (!matches) {
			throw new InvalidSigninInformation();
		}

		return user.getId();
	}

	public void signup(Signup signup) {
		userRepository.findByEmail(signup.getEmail())
			.ifPresent(u -> {
				throw new AlreadyExistsEmailException();
			});

		String encryptedPassword = encoder.encrypt(signup.getPassword());

		var user = User.builder()
			.name(signup.getName())
			.password(encryptedPassword)
			.email(signup.getEmail())
			.build();

		userRepository.save(user);
	}
}
