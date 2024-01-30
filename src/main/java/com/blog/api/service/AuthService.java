package com.blog.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blog.api.domain.Session;
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

	@Transactional
	public Long signIn(Login login) {
		User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
			.orElseThrow(InvalidSigninInformation::new);
		Session session = user.addSession();
		return user.getId();
	}

	public void signup(Signup signup) {
		userRepository.findByEmail(signup.getEmail())
			.ifPresent(u -> {
				throw new AlreadyExistsEmailException();
			});

		var user = User.builder()
			.name(signup.getName())
			.password(signup.getPassword())
			.email(signup.getEmail())
			.build();

		userRepository.save(user);
	}
}
