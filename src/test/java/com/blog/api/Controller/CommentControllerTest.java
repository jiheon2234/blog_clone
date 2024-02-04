package com.blog.api.Controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.blog.api.config.MyMockUser;
import com.blog.api.domain.Comment;
import com.blog.api.domain.Post;
import com.blog.api.domain.User;
import com.blog.api.repository.UserRepository;
import com.blog.api.repository.comment.CommentRepository;
import com.blog.api.repository.post.PostRepository;
import com.blog.api.request.comment.CommentCreate;
import com.blog.api.request.comment.CommentDelete;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@AfterEach
	void clean() {
		postRepository.deleteAll();
		userRepository.deleteAll();
		commentRepository.deleteAll();
	}

	@Test
	@MyMockUser
	@DisplayName("댓글 작성")
	void test1() throws Exception {
		//given

		User user = User.builder()
			.email("jiheon2234@naver.com")
			.password("1234")
			.name("test")
			.build();
		userRepository.save(user);

		Post post = Post.builder()
			.title("testtest")
			.content("bar")
			.user(user)
			.build();
		postRepository.save(post);

		CommentCreate request = CommentCreate.builder()
			.author("ttttt")
			.password("123456")
			.content("댓글1010101010")
			.build();

		String json = objectMapper.writeValueAsString(request);

		//expected
		mockMvc.perform(post("/posts/{postId}/comments", post.getId())
				.contentType(APPLICATION_JSON_VALUE)
				.content(json))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isOk());

		Comment savedComment = commentRepository.findAll().get(0);
		assertEquals("ttttt", savedComment.getAuthor());
		assertNotEquals("123456", savedComment.getPassword());
	}

	@Test
	@MyMockUser
	@DisplayName("댓글 삭")
	void test2() throws Exception {
		//given

		User user = User.builder()
			.email("jiheon2234@naver.com")
			.password("1234")
			.name("test")
			.build();
		userRepository.save(user);

		Post post = Post.builder()
			.title("testtest")
			.content("bar")
			.user(user)
			.build();
		postRepository.save(post);

		Comment comment = Comment.builder()
			.author("jiheon")
			.password(passwordEncoder.encode("123456"))
			.content("123456789011")
			.build();

		comment.setPost(post);
		commentRepository.save(comment);

		CommentDelete request = new CommentDelete("123456");
		String json = objectMapper.writeValueAsString(request);

		//expected
		mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
				.contentType(APPLICATION_JSON_VALUE)
				.content(json))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isOk());

	}

}