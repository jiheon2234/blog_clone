package com.blog.api.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.blog.api.crypto.PasswordEncoder;
import com.blog.api.domain.User;
import com.blog.api.exception.AlreadyExistsEmailException;
import com.blog.api.repository.UserRepository;
import com.blog.api.request.Signup;


@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private PasswordEncoder encoder;

	@AfterEach
	void clean() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원가입 성공")
	void test1() {
		//given
		Signup signup = Signup.builder()
			.password("0000")
			.email("jiheon2234@naver.com")
			.name("jiheon")
			.build();
		//when
		authService.signup(signup);
		//then
		assertThat(userRepository.count()).isEqualTo(1);

		User user = userRepository.findAll().iterator().next();
		assertEquals("jiheon2234@naver.com", user.getEmail());
	}

	@Test
	@DisplayName("회원가입 중복된 이메일")
	void test2() {

		User user = User.builder()
			.email("jiheon2234@naver.com")
			.password("1234")
			.name("test")
			.build();
		userRepository.save(user);

		//given
		Signup signup = Signup.builder()
			.password("0000")
			.email("jiheon2234@naver.com")
			.name("jiheon")
			.build();
		//whenthen
		Assertions.assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
	}




}