package com.blog.api.Controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.blog.api.repository.UserRepository;
import com.blog.api.request.Signup;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;


	@BeforeEach
	void clean() {
		userRepository.deleteAll();
	}


	@Test
	@DisplayName("회원가입")
	void test6() throws Exception{
		// //given

		Signup signup = Signup.builder()
			.password("0000")
			.email("jiheon2234@naver.com")
			.name("jiheon")
			.build();

		// expected
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
				.content(objectMapper.writeValueAsString(signup))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print());
	}
}